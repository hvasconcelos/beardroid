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

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class Logging {
    Context mContext;
    boolean mIsDebuggable;
    String logTag;

    @Inject
    Logging(Context context) {
        this.mContext = context;
        this.mIsDebuggable = AndroidUtil.isDebugVersion(context);
        this.logTag = context.getApplicationInfo().name;
    }

    public void exception(String tag, String logMsg, Exception e) {
        if (mIsDebuggable) {
            Log.e(logTag + " - " + tag, logMsg, e);
        }
    }

    public void error(String tag, String logMsg) {
        if (mIsDebuggable) {
            Log.e(logTag + " - " + tag, logMsg);
        }
    }

    public void info(String tag, String logMsg) {
        if (mIsDebuggable) {
            Log.i(logTag + " - " + tag, logMsg);
        }
    }

    public boolean isDebuggable() {
        return mIsDebuggable;
    }

    public void warning(String tag, String logMsg) {
        if (mIsDebuggable) {
            Log.w(logTag + " - " + tag, logMsg);
        }
    }
}
