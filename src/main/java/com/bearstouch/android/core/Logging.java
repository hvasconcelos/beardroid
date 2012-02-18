package com.bearstouch.android.core;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

@Singleton
public class Logging
{
	Context mContext;	
	boolean mIsDebuggable;
	String mAppLogtag;

	@Inject
	Logging(@Named("AppLogTag") String applogtag,Context context)
	{

		mContext = context;
		mAppLogtag=applogtag;
		mIsDebuggable = isDebugVersion();
	}

	private boolean isDebugVersion()
	{

		int flags = mContext.getApplicationInfo().flags;
		if ((flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void error(String tag, String logMsg, Exception e)
	{
		if (mIsDebuggable)
		{
			Log.e(mAppLogtag+" - "+tag, logMsg,e);
		}
	}
	
	public void error(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.e(mAppLogtag+" - "+tag, logMsg);
		}
	}

	public void info(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.i(mAppLogtag+" - "+tag, logMsg);
		}
	}

	public boolean isDebuggable()
	{
		return mIsDebuggable;
	}


	public void warning(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.w(mAppLogtag+" - "+tag, logMsg);
		}

	}
}
