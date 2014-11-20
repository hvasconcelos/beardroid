package com.bearstouch.beardroid.core.injection.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bearstouch.beardroid.core.injection.Injector;

/**
 * Created by heldervasc on 18/11/14.
 */
public class InjectedFragment  extends Fragment implements InjectView {


    @Override
    public View getViewById(int resId) {
        return getView().findViewById(resId);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        Injector injector=Injector.getInstance();
        View ret =  injector.injectLayout(this, inflater, container);
        injector.inject(this);
        return ret;
    }


    @Override
    public Context getViewContext() {
        return getViewContext();
    }
}
