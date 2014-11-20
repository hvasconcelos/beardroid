/**
 * Copyright (C) 2013
 * Bearstouch Software : <mail@bearstouch.com>
 *
 * This file is part of Bearstouch Android Lib.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bearstouch.beardroid.core.image;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache
{

    private Map<String, Bitmap> cache = Collections
            .synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));
    private long size = 0;// current allocated size
    private long limit = 1000000;// max memory in bytes

    public MemoryCache()
    {
        // use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    /**
     * @param new_limit
     */
    public void setLimit(long new_limit)
    {
        limit = new_limit;
        // Ln.d("MemoryCache will use up to "+limit/1024./1024.+"MB");
    }

    /**
     * @param id
     * @return
     */
    public Bitmap get(String id)
    {
        if (!cache.containsKey(id))
            return null;
        return cache.get(id);
    }

    /**
     * @param id
     * @param bitmap
     */
    public void put(String id, Bitmap bitmap)
    {
        try
        {
            if (cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th)
        {
            th.printStackTrace();
        }
    }

    /**
     *
     */
    private void checkSize()
    {
        // Ln.d("cache size="+size+" length="+cache.size());
        if (size > limit)
        {
            Iterator<Entry<String, Bitmap>> iter = cache.entrySet().iterator();
            while (iter.hasNext())
            {
                Entry<String, Bitmap> entry = iter.next();
                size -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (size <= limit)
                    break;
            }
            // Ln.d("Clean cache. New size "+cache.size());
        }
    }

    /**
     *
     */
    public void clear()
    {
        cache.clear();
        size = 0;
        System.gc();
    }

    long getSizeInBytes(Bitmap bitmap)
    {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}