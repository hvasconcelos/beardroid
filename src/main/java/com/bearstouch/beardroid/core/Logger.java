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
import android.util.Log;
import com.google.analytics.tracking.android.Tracker;

public class Logger
{
    // Global Logger
    private static Logger gLogger = null;
    // Logger Name
    String      mTagName;                //application name
    // Logger Context
    Context     mCtx;                    //application context
    // Is the instance Debuggablke
    boolean     mIsDebuggable;           //if not a debu Version will debug
    // Active Level
    LogLevel    mLevel;


    public enum LogLevel{
        INFO,
        WARN,
        ERROR,
        FATAL
    }

    public Logger(Context ctx) {
        init(ctx, ctx.getApplicationInfo().name, LogLevel.INFO);
    }

    public Logger(Context ctx, String name, LogLevel level){
        init(ctx, name, level);
    }

    private void init(Context ctx, String name, LogLevel level) {
        this.mCtx = ctx;
        if (AndroidUtil.isDebugVersion(ctx)) {
            mIsDebuggable = true;
        } else {
            mIsDebuggable = false;
        }
        mTagName = name;
        mLevel=level;
    }

    public static void error(Context ctx, String logMsg) {
        initializeIfNeeded(ctx);
        gLogger.error(logMsg);
    }

    public void error(String logMsg){
        Log.d(mTagName, logMsg);
    }


    public static void error(Context ctx, String logMsg, Exception e) {
        initializeIfNeeded(ctx);
        gLogger.error(logMsg,e);
    }

    public void error(String logMsg, Exception e){
        Log.d(mTagName, logMsg);
    }

    public static void info(Context ctx, String logMsg) {

        initializeIfNeeded(ctx);
        gLogger.info(logMsg);

    }

    public void info(String logMsg){
        if (mIsDebuggable &&
            mLevel.ordinal()>=LogLevel.INFO.ordinal()) {
            Log.i(mTagName, logMsg);
        }
    }

    protected static void initializeIfNeeded(Context ctx) {
        if (gLogger == null) {
            gLogger = new Logger(ctx);
        }
    }

    public static boolean isDebuggable(Context ctx) {
        initializeIfNeeded(ctx);
        return gLogger.mIsDebuggable;

    }

    public static void logAndTrackException(Context ctx, String type,
                                            String logMsg, Exception e)
    {
        initializeIfNeeded(ctx);
        gLogger.error(type,logMsg,e);
    }


    public void error(String type,
          String logMsg, Exception e)
    {
        Tracker tracker= GATracker.getTracker(mCtx, GATracker.APP_TRACKER);
        if (tracker != null & !mIsDebuggable) {
            tracker.sendException("SMS Scheduler",e,false);
            tracker.sendEvent("SQLException", type, logMsg, 0L);
        }
        if (mIsDebuggable) {
            Log.e(mTagName, logMsg, e);
        }

    }

    public static void warning(Context ctx, String logMsg) {

        initializeIfNeeded(ctx);
        gLogger.warning(logMsg);

    }

    public void warning(String logMsg) {
        if (    mIsDebuggable &&
                mLevel.ordinal() >= LogLevel.WARN.ordinal() ) {
            Log.w(mTagName, logMsg);
        }

    }
}
