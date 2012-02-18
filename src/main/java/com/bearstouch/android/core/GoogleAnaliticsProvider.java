package com.bearstouch.android.core;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.inject.Inject;

import roboguice.inject.ContextScoped;
import com.google.inject.name.Named;


import android.content.Context;


public class GoogleAnaliticsProvider 
{
	public static final String LOGTAG_="GoogleAnaliticsTracker";
	Context context;
	GoogleAnalyticsTracker mtracker;	
	Logging mlogging;

	@Inject
	public GoogleAnaliticsProvider(@Named("GoogleAnalyticsId") String gAnaliticsId,Context context,Logging mlogging) {
		this.context = context;		
		this.mlogging=mlogging;
			mtracker=GoogleAnalyticsTracker.getInstance();
			mlogging.info(LOGTAG_, "Starting a Google Analitics Sessin for Id = ["+gAnaliticsId+"]");
			mtracker.startNewSession(gAnaliticsId,20,context);
			
				
	}
	
	public void trackPage(String name){	
		mlogging.info(LOGTAG_, "Tracking Page = ["+name+"]");
		mtracker.trackPageView(name);
		
	}
	
	public void trackCustomVar(int index,String name,String value,int scope){
		mlogging.info(LOGTAG_, "Tracking Custom Var = ["+name+"] with Value =["+value+"]");
		mtracker.setCustomVar(index, name, value, scope);
		
	}
	
	public void trackEvent(String category,String action,String label,int value){
		mlogging.info(LOGTAG_, "Tracking Event Category = ["+category+"], Action = ["+action+"], Label = ["+label+"]");
		mtracker.trackEvent(category, action, label, value);
		
		
	}
	
	public void closeAndDisptach(){
		mtracker.dispatch();
		mtracker.stop();
	
	}
	
}
