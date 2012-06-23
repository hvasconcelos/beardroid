package com.bearstouch.android.core.image;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import roboguice.util.Ln;
import android.graphics.Bitmap;

/**
 * 
 *
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache=Collections.synchronizedMap(
            new LinkedHashMap<String, Bitmap>(10,1.5f,true));//Last argument true for LRU ordering
    private long size=0;//current allocated size
    private long limit=1000000;//max memory in bytes

    public MemoryCache(){
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }
    
    /**
     * @param new_limit
     */
    public void setLimit(long new_limit){
        limit=new_limit;
        Ln.d("MemoryCache will use up to "+limit/1024./1024.+"MB");
    }

    /**
     * @param id
     * @return
     */
    public Bitmap get(String id){
        if(!cache.containsKey(id))
            return null;
        return cache.get(id);
    }

    /**
     * @param id
     * @param bitmap
     */
    public void put(String id, Bitmap bitmap){
        try{
            if(cache.containsKey(id))
                size-=getSizeInBytes(cache.get(id));
            cache.put(id, bitmap);
            size+=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    /**
     * 
     */
    private void checkSize() {
        Ln.d("cache size="+size+" length="+cache.size());
        if(size>limit){
            Iterator<Entry<String, Bitmap>> iter=cache.entrySet().iterator();//least recently accessed item will be the first one iterated  
            while(iter.hasNext()){
                Entry<String, Bitmap> entry=iter.next();
                size-=getSizeInBytes(entry.getValue());
                iter.remove();
                if(size<=limit)
                    break;
            }
            Ln.d("Clean cache. New size "+cache.size());
        }
    }

    /**
     * 
     */
    public void clear() {   	  
        cache.clear();
        size=0;
        System.gc();
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}