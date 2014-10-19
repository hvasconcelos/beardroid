package com.bearstouch.android.core.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.bearstouch.android.core.GATracker;
import com.bearstouch.android.core.Logger;
import com.bearstouch.android.core.injection.annotation.ClickOn;
import com.bearstouch.android.core.injection.annotation.GoogleAnalytics;
import com.bearstouch.android.core.injection.annotation.Layout;
import com.bearstouch.android.core.injection.annotation.Log;
import com.bearstouch.android.core.injection.annotation.StringRes;
import com.bearstouch.android.core.injection.annotation.ViewId;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class Injector
{
    public static void injectActiviy(Activity act) 
    {
        try
        { 
            processClassAnns(act);
            processMemberAnns(act);
            processMethodAnns(act);
                
        } catch (Exception e)
        {
            e.printStackTrace();
        }
       
    }

    private static void processMethodAnns(final Activity act) throws Exception
    {
        for (final Method method : act.getClass().getMethods())
        {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
            {
                if (annotation instanceof ClickOn)
                {
                    processClickOnAnn(act, method, annotation);
                }
            }
        }        
    }

    private static void processClickOnAnn(final Activity act,
            final Method method, Annotation annotation)
    {
        ClickOn clickOn = (ClickOn) annotation;
        View view=act.findViewById(clickOn.value());
        view.setOnClickListener(new OnClickListener()
        {
            
            public void onClick(View v)
            {
                try
                {
                    method.invoke((Object)act);
                } catch (IllegalArgumentException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        });
    }

    private static void processClassAnns(Activity act)
    {
        Annotation[] annotations = act.getClass().getAnnotations();
        for (Annotation annotation : annotations)
        {
            if (annotation instanceof Layout)
            {
                Layout gaAn = (Layout) annotation;
                act.setContentView(gaAn.value());
            }
        }
    }

    private static void processMemberAnns(Activity act)
            throws IllegalArgumentException, IllegalAccessException
    {
        for (Field field : act.getClass().getDeclaredFields())
        {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
            {
                // Google Analitics Injection
                if (annotation instanceof GoogleAnalytics)
                {
                    processGaAnn(act, field, annotation);
                } else if (annotation instanceof StringRes)
                {
                    processStringResAnn(act, field, annotation);
                } else if (annotation instanceof ViewId)
                {
                    processViewIdAnn(act, field, annotation);
                } else if (annotation instanceof Log)
                {
                    processLoggerAnn(act, field, annotation);
                }
            }
        }
    }

    private static void processLoggerAnn(Activity act, Field field,
            Annotation annotation) throws IllegalAccessException
    {
        Log vAn = (Log) annotation;
        field.set(act,new Logger(act.getApplicationContext(), vAn
                        .logTag(), Logger.LogLevel.INFO));
    }

    private static void processViewIdAnn(Activity act, Field field,
            Annotation annotation) throws IllegalAccessException
    {
        ViewId vAn = (ViewId) annotation;
        field.set(act, act.findViewById(vAn.value()));
    }

    private static void processStringResAnn(Activity act, Field field,
            Annotation annotation) throws IllegalAccessException
    {
        // String Injection
        StringRes srAn = (StringRes) annotation;
        field.set(act, new String(act.getString(srAn.value())));
    }

    private static void processGaAnn(Activity act, Field field,
            Annotation annotation) throws IllegalAccessException
    {
        GoogleAnalytics gaAn = (GoogleAnalytics) annotation;
        //field.set(act,new GATracker(act, gaAn.value()));
    }
}
