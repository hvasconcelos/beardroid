/**
 * Copyright (C) 2014
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

import java.util.HashMap;

import android.content.Context;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class GATracker {

    public static final String GLOBAL_TRACKER = "GLOBAL";
    public static final String APP_TRACKER = "APP_TRACKER";

    private static HashMap<String, Tracker> mTrackers = new HashMap<String, Tracker>();

    public static Tracker getTracker(Context ctx, String trackerName) {
        Tracker mTracker = null;
        if (!mTrackers.containsKey(trackerName)) {
            mTracker = GoogleAnalytics.getInstance(ctx).getTracker(trackerName);
            mTrackers.put(trackerName, mTracker);
        } else {
            mTracker = mTrackers.get(trackerName);
        }
        return mTracker;
    }

}
