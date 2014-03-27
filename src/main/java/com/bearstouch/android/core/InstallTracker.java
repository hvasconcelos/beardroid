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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.DisplayMetrics;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class InstallTracker
{

    private static final String UNIQUE_KEYID = "PID";
    private static final String INSTALL_TIMESTAMP_KEY = "ITSK";
    private static final String TIMESTAMP_HASH_KEY = "UIDHK";
    public static final String INSTALLFILE = "install.info";
    private static final String LOGTAG = "InstallTracker";
    public static final String APP_NAME_VAR = "Application Name";
    public static final String APP_VERSION_VAR = "Application Version";
    public static final String PHONE_VAR = "Device";
    public static final String SDK_VERSION_VAR = "SDK Version";
    public static final String CPU_VAR = "CPU";
    public static final String INSTALLER_VAR = "Installer";
    public static final String INSTALL_TRACK_EVENT_TAG = "Install Event";

    private Context mContext;
    private Logger mLg;
    private GoogleAnaliticsTracker mGTracker;
    boolean mIsFirstTimeRunnig;
    private String mUniqueID = null;
    private String mTimeStampHash = null;
    private long mInstallTimeStamp = 0;
    private boolean mIsAValidInstall = false;

    public InstallTracker(  Context context,   
                            Logger log,
                            GoogleAnaliticsTracker gTracker )
    {
        mContext = context;
        mLg = log;
        mGTracker = gTracker;
        mUniqueID = getIDFromFile(context);
    }

    private String getIDFromFile(Context ctx)
    {
        SharedPreferences mSettings = null;
        mSettings = getPreferenceFile();
        mLg.info("Verifying Install Info");
        mUniqueID = mSettings.getString(UNIQUE_KEYID, "");
        if (mUniqueID.length() == 0)
        {
            // Application First run
            mLg.info("First Time Running - Generating Unique Install ID");
            mIsFirstTimeRunnig = true;
            mIsAValidInstall = true;
            mUniqueID = UUID.randomUUID().toString();
            mInstallTimeStamp = System.currentTimeMillis();
            mTimeStampHash = DigestUtils.shaHex(mUniqueID
                    + Long.toString(mInstallTimeStamp));
            saveToPreferencesFile(ctx, mSettings, mUniqueID, mInstallTimeStamp,
                    mTimeStampHash);
            mLg.info("Install Info Saved with Success");
            return mUniqueID;
        } else
        {
            mLg.info("Not First Time Running - Validating Install");
            mTimeStampHash = mSettings.getString(TIMESTAMP_HASH_KEY, "");
            mInstallTimeStamp = mSettings.getLong(INSTALL_TIMESTAMP_KEY, 0);
            mIsAValidInstall = verifyInstallID();
            if (!mIsAValidInstall)
            {
                mLg.error("Invalid Install = " + mUniqueID);
            } else
            {
                mLg.info("Unique ID Loaded = " + mUniqueID);
            }
            return mUniqueID;
        }

    }

    private void saveToPreferencesFile(Context ctx, SharedPreferences Settings,
            String deviceId, Long install_ts, String install_hash)
    {

        SharedPreferences.Editor editor = Settings.edit();
        mLg.info("Saving Unique Install ID = " + deviceId);
        editor.putString(UNIQUE_KEYID, deviceId);
        mLg.info("Saving Timestamp = " + install_ts);
        editor.putLong(INSTALL_TIMESTAMP_KEY, install_ts);
        mLg.info("Saving UUID hash = " + install_hash);
        editor.putString(TIMESTAMP_HASH_KEY, install_hash);
        editor.commit();
    }

    protected SharedPreferences getPreferenceFile()
    {
        return mContext.getSharedPreferences(INSTALLFILE, 0);
    }

    public boolean isFirstTimeRunning()
    {
        return mIsFirstTimeRunnig;
    }

    public String getUniqueID()
    {
        return mUniqueID;

    }

    public long getInstallTimeStamp()
    {
        return mInstallTimeStamp;
    }

    private boolean verifyInstallID()
    {

        String digestTemp = DigestUtils.shaHex(mUniqueID
                + Long.toString(mInstallTimeStamp));
        if (mTimeStampHash.compareTo(digestTemp) != 0)
        {
            return false;
        } else
        {
            return true;
        }
    }

    public boolean isInstallValid()
    {
        return mIsAValidInstall;
    }

    public void trackInstall(Activity activity, String app_name,
            String app_version_name)
    {

        mGTracker
                .trackEvent(INSTALL_TRACK_EVENT_TAG, APP_NAME_VAR, app_name, 1);
        mGTracker.trackEvent(INSTALL_TRACK_EVENT_TAG, APP_VERSION_VAR,
                app_version_name, 1);
        mGTracker.trackEvent(INSTALL_TRACK_EVENT_TAG, SDK_VERSION_VAR,
                Integer.toString(Build.VERSION.SDK_INT), 1);
        mGTracker
                .trackEvent(INSTALL_TRACK_EVENT_TAG, PHONE_VAR,
                        Build.MANUFACTURER + " " + Build.PRODUCT + " "
                                + Build.MODEL, 1);
        mGTracker
                .trackEvent(INSTALL_TRACK_EVENT_TAG, CPU_VAR, Build.CPU_ABI, 1);
        String installationSource = mContext.getPackageManager()
                .getInstallerPackageName(mContext.getPackageName());
        mGTracker.trackEvent(INSTALL_TRACK_EVENT_TAG, INSTALLER_VAR,
                installationSource, 1);

        // Ecra
        DisplayMetrics displayMetrics = AndroidUtil.getDisplayMetrics(activity);
        if (displayMetrics != null)
        {
            String Resolution = displayMetrics.widthPixels + "x"
                    + displayMetrics.heightPixels;
            String dpis = displayMetrics.xdpi + "x" + displayMetrics.ydpi;
            mGTracker.trackEvent(INSTALL_TRACK_EVENT_TAG, "RESOLUTION",
                    Resolution, 1);
            mGTracker.trackEvent(INSTALL_TRACK_EVENT_TAG, "DPI", dpis, 1);
        }
    }

    protected void setmIsFirstTimeRunnig(boolean mIsFirstTimeRunnig)
    {
        this.mIsFirstTimeRunnig = mIsFirstTimeRunnig;
    }

    protected void setmUniqueID(String mUniqueID)
    {
        this.mUniqueID = mUniqueID;
    }

    protected void setmTimeStampHash(String mTimeStampHash)
    {
        this.mTimeStampHash = mTimeStampHash;
    }

    protected void setmInstallTimeStamp(long mInstallTimeStamp)
    {
        this.mInstallTimeStamp = mInstallTimeStamp;
    }

    protected void setmIsAValidInstall(boolean mIsAValidInstall)
    {
        this.mIsAValidInstall = mIsAValidInstall;
    }
}
