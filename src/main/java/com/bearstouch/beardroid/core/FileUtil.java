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

package com.bearstouch.beardroid.core;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class FileUtil
{
    /**
     * @param fis
     * @return
     */
    public static String readFileContentToString(InputStream fis)
    {
        StringBuffer stringBuffer = new StringBuffer();

        if (fis == null)
        {
            return "";
        }

        try
        {
            char[] buffer = new char[1024];
            Reader reader = new BufferedReader(new InputStreamReader(fis,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1)
            {
                stringBuffer.append(buffer, 0, n);
            }
            fis.close();

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return stringBuffer.toString();

    }

    /**
     * @param context
     * @return
     */
    public static String getSDCardCachePath(Context context)
    {
        String cacheDir = Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/Android/data/"
                + context.getPackageName() + "/cache";
        return cacheDir;
    }

    /**
     * @param context
     * @return
     */
    public static String getInternalCachePath(Context context)
    {
        return context.getCacheDir().getAbsolutePath();
    }

}
