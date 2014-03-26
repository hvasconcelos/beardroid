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

public class Logging
{

    boolean mIsDebuggable;
    String logTag;
    private static Logging logging = null;

    private Logging(Context context)
    {
        this.mIsDebuggable = AndroidUtil.isDebugVersion(context);
        this.logTag = context.getApplicationInfo().name;
    }

    private static void initInstance(Context ctx)
    {
        if (logging == null)
        {
            logging = new Logging(ctx);
        }
    }

    public static void exception(Context ctx, String logMsg, Exception e)
    {
        exception(ctx, "Geral", logMsg, e);
    }

    public static void exception(Context ctx, String tag, String logMsg,
            Exception e)
    {
        initInstance(ctx);
        if (logging.mIsDebuggable)
        {
            Log.e(logging.logTag + " | " + tag, logMsg, e);
        }
    }

    public static void error(Context ctx, String logMsg)
    {
        error(ctx, "Geral", logMsg);
    }

    public static void error(Context ctx, String tag, String logMsg)
    {
        initInstance(ctx);
        if (logging.mIsDebuggable)
        {
            Log.e(logging.logTag + " | " + tag, logMsg);
        }
    }

    public static void info(Context ctx, String logMsg)
    {
        info(ctx, "Geral", logMsg);
    }

    public static void info(Context ctx, String tag, String logMsg)
    {
        initInstance(ctx);
        if (logging.mIsDebuggable)
        {
            Log.i(logging.logTag + " | " + tag, logMsg);
        }
    }

    public static void warning(Context ctx, String logMsg)
    {
        warning(ctx, "Geral", logMsg);
    }

    public static void warning(Context ctx, String tag, String logMsg)
    {
        initInstance(ctx);
        if (logging.mIsDebuggable)
        {
            Log.w(logging.logTag + " | " + tag, logMsg);
        }
    }
}
