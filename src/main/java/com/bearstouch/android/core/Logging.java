package com.bearstouch.android.core;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

/**
 * @author HŽlder Vasconcelos heldervasc@bearstouch.com
 *
 */
@Singleton
public class Logging
{
	Context mContext;	
	boolean mIsDebuggable;
	String mAppLogtag;

	/**
	 * @param applogtag
	 * @param context
	 */
	@Inject
	Logging(@Named("AppLogTag") String applogtag,Context context)
	{

		mContext = context;
		mAppLogtag=applogtag;
		mIsDebuggable = isDebugVersion();
	}

	/**
	 * @return
	 */
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

	/**
	 * @param tag
	 * @param logMsg
	 * @param e
	 */
	public void error(String tag, String logMsg, Exception e)
	{
		if (mIsDebuggable)
		{
			Log.e(mAppLogtag+" - "+tag, logMsg,e);
		}
	}
	
	/**
	 * @param tag
	 * @param logMsg
	 */
	public void error(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.e(mAppLogtag+" - "+tag, logMsg);
		}
	}

	/**
	 * @param tag
	 * @param logMsg
	 */
	public void info(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.i(mAppLogtag+" - "+tag, logMsg);
		}
	}

	/**
	 * @return
	 */
	public boolean isDebuggable()
	{
		return mIsDebuggable;
	}


	/**
	 * @param tag
	 * @param logMsg
	 */
	public void warning(String tag, String logMsg)
	{
		if (mIsDebuggable)
		{
			Log.w(mAppLogtag+" - "+tag, logMsg);
		}

	}
}
