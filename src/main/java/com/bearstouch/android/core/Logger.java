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

package com.bearstouch.android.core;

import android.content.Context;
import android.util.Log;

public class Logger
{
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int ERROR = 3;

    Context mCtx;
    boolean mIsDebuggable;
    String mLogTag;
    int mLevel;

    public Logger(Context context, String logTag, int level)
    {
        this.mIsDebuggable = AndroidUtil.isDebugVersion(context);
        this.mLogTag = context.getApplicationInfo().name + " - " + logTag;
        this.mLevel = level;
        this.mCtx = context;
    }

    public void exception(String logMsg, Exception e)
    {
        if (mIsDebuggable)
            Log.e(mLogTag, logMsg, e);
    }

    public void error(String logMsg)
    {
        if (mIsDebuggable)
            Log.e(mLogTag, logMsg);
    }

    public void info(String logMsg)
    {
        if (mIsDebuggable && mLevel >= INFO)
            Log.i(mLogTag, logMsg);
    }

    public void warning(String logMsg)
    {
        if (mIsDebuggable && mLevel >= WARN)
            Log.w(mLogTag, logMsg);
    }
}
