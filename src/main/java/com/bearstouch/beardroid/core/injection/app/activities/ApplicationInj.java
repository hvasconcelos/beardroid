package com.bearstouch.beardroid.core.injection.app.activities;

import android.app.Application;

import com.bearstouch.beardroid.core.injection.Injector;

public class ApplicationInj extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        Injector.getInstance().injectApplication(this);
    }
}
