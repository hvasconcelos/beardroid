package com.bearstouch.beardroid.core.injection.app.activities;

import android.content.Context;
import android.view.View;

/**
 * Created by heldervasc on 18/11/14.
 */
public interface InjectView {

    public View getViewById(int resId);

    public Context getViewContext();

}
