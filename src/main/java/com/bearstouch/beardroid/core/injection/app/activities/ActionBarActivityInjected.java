package com.bearstouch.beardroid.core.injection.app.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.bearstouch.beardroid.core.injection.Injector;


public class ActionBarActivityInjected extends ActionBarActivity implements InjectView {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.getInstance().injectLayout(this);
        Injector.getInstance().inject(this);
    }

    @Override
    public Context getViewContext(){ return this.getApplicationContext();}


    @Override
    public View getViewById(int resId){
        return findViewById(resId);
    }
}
