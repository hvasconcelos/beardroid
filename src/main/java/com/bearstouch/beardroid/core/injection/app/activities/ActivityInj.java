package com.bearstouch.beardroid.core.injection.app.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bearstouch.beardroid.core.injection.Injector;

/**
 * Created by heldervasc on 22/11/14.
 */
public class ActivityInj extends Activity implements InjectView {

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
