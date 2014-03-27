
package com.bearstouch.android.core.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.bearstouch.android.core.GoogleAnaliticsTracker;
import com.bearstouch.android.core.Logger;
import com.bearstouch.android.core.injection.annotation.GoogleAnalytics;
import com.bearstouch.android.core.injection.annotation.Layout;
import com.bearstouch.android.core.injection.annotation.Log;
import com.bearstouch.android.core.injection.annotation.StringRes;
import com.bearstouch.android.core.injection.annotation.ViewId;

import android.app.Activity;

public class Injector
{
    public static void injectActiviy(Activity act)
    {
        processClassAnns(act);
        processMemberAnns(act);        
    }

    private static void processClassAnns(Activity act)
    {
        Annotation[] annotations = act.getClass().getAnnotations();
        for (Annotation annotation : annotations)
        {
            if (annotation instanceof Layout)
            {
                try
                {
                    Layout gaAn = (Layout) annotation;
                    act.setContentView(gaAn.value());                  
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }            
        }
    }

    private static void processMemberAnns(Activity act)
    {                
        for (Field field : act.getClass().getDeclaredFields())
        {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
            {
                // Google Analitics Injection
                if (annotation instanceof GoogleAnalytics)
                {
                    try
                    {
                        GoogleAnalytics gaAn = (GoogleAnalytics) annotation;
                        field.set(act,
                                new GoogleAnaliticsTracker(act, gaAn.value()));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else if (annotation instanceof StringRes)
                {
                    // String Injection
                    try
                    {
                        StringRes srAn = (StringRes) annotation;
                        field.set(act, new String(act.getString(srAn.value())));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } else if (annotation instanceof ViewId)
                {
                    try
                    {
                        // String Injection
                        ViewId vAn = (ViewId) annotation;
                        field.set(act, act.findViewById(vAn.value()));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (annotation instanceof Log)
                {
                    try
                    {
                        // String Injection
                        Log vAn = (Log) annotation;
                        field.set(act, new Logger(act.getApplicationContext(),vAn.logTag(),vAn.level()));
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
