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

import com.google.analytics.tracking.android.GAServiceManager;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

public class GoogleAnaliticsTracker
{
    Context mContext;
    Tracker mTracker;
    private static HashMap<String, Tracker> mTrackers=new HashMap<String, Tracker>();

    public GoogleAnaliticsTracker(Context context,String trackerName)
    {
        mContext = context;
        if( mTrackers.containsKey(trackerName) ){
            mTracker=GoogleAnalytics.getInstance(context).getTracker(trackerName);
            mTrackers.put(trackerName,mTracker);
        }else{
            mTrackers.get(trackerName);
        }
        // Get singleton.      
        GAServiceManager.getInstance().setDispatchPeriod(30);
    }

    public void trackView(String name)
    {
        mTracker.sendView(name);
    }

    public void trackEvent(String category, String action, String label,
            long value)
    {
        mTracker.sendEvent(category, action, label, value);
    }

    public void trackExeception(String description, Exception e)
    {
        mTracker.sendException(description, e, true);
    }

    public void trackSocial(String network, String action, String target)
    {
        mTracker.sendSocial(network, action, target);
    }

    public void trackTiming(String category, long interval, String name,
            String label)
    {
        mTracker.sendTiming(category, interval, name, label);
    }

    public void disptach()
    {
        GAServiceManager.getInstance().dispatch();
    }

}
