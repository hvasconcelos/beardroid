package com.bearstouch.beardroid.core.injection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.bearstouch.beardroid.core.Logger;
import com.bearstouch.beardroid.core.injection.annotation.AfterInjection;
import com.bearstouch.beardroid.core.injection.annotation.ClickOn;
import com.bearstouch.beardroid.core.injection.annotation.Inject;
import com.bearstouch.beardroid.core.injection.annotation.Layout;
import com.bearstouch.beardroid.core.injection.annotation.Log;
import com.bearstouch.beardroid.core.injection.annotation.Provider;
import com.bearstouch.beardroid.core.injection.annotation.Singleton;
import com.bearstouch.beardroid.core.injection.annotation.StringRes;
import com.bearstouch.beardroid.core.injection.annotation.ViewId;
import com.bearstouch.beardroid.core.injection.app.activities.InjectView;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class Injector {
    // Singleton Injector
    static Injector sInjector = new Injector();
    static List<Class> classAnnotations = new ArrayList<Class>();
    static List<Class> memberAnnotations = new ArrayList<Class>();
    static List<Class> methodAnnotations = new ArrayList<Class>();

    static BaseInjProvider sAppProvider;

    static {
        classAnnotations.add(Layout.class);
        memberAnnotations.add(Log.class);
        memberAnnotations.add(StringRes.class);
        memberAnnotations.add(ViewId.class);
        methodAnnotations.add(ClickOn.class);
        methodAnnotations.add(AfterInjection.class);
    }

    ;

    private Injector() {
    }

    public static Injector getInstance() {
        return sInjector;
    }

    public <T extends InjectView> void inject(T obj) {
        try {
            processClassAnns(obj);
            processMemberAnns(obj);
            processMethodAnns(obj);
            processAfterInjection(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void injectLayout(Activity activity) {
        if (((Object) activity).getClass().isAnnotationPresent(Layout.class)) {
            Layout annotation = ((Object) activity).getClass().getAnnotation(Layout.class);
            int resId = annotation.value();
            activity.setContentView(resId);
        }
    }

    public View injectLayout(
            Fragment frag,
            LayoutInflater inflater,
            ViewGroup container) {
        View res = null;
        if (((Object) frag).getClass().isAnnotationPresent(Layout.class)) {
            Layout annotation = ((Object) frag).getClass().getAnnotation(Layout.class);
            res = inflater.inflate(annotation.value(), container, false);
        }
        return res;
    }


    <T extends InjectView>
    void processAfterInjection(T obj) throws Exception {

        for (final Method method : obj.getClass().getMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof AfterInjection) {
                    processAfterInjectionAnn(obj, method, annotation);
                }
            }
        }
    }

    <T extends InjectView>
    void processAfterInjectionAnn(T obj,
                                  final Method method,
                                  Annotation annotation) throws InvocationTargetException,
            IllegalAccessException {
        method.invoke(obj);
    }


    <T extends InjectView>
    void processMethodAnns(final T obj) throws Exception {
        for (final Method method : obj.getClass().getMethods()) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof ClickOn) {
                    processClickOnAnn(obj, method, annotation);
                }
            }
        }
    }

    <T extends InjectView>
    void processClickOnAnn(final T obj,
                           final Method method,
                           Annotation annotation) {
        ClickOn clickOn = (ClickOn) annotation;
        View view = obj.getViewById(clickOn.value());
        view.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                try {
                    method.invoke((Object) obj);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    <T extends InjectView>
    void processClassAnns(T obj) {
        // Nothing to do this time
    }

    <T extends InjectView>
    void processMemberAnns(T act)
            throws IllegalArgumentException, IllegalAccessException {
        for (Field field : act.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                // Google Analitics Injection
                if (annotation instanceof StringRes) {
                    processStringResAnn(act, field, annotation);
                } else if (annotation instanceof ViewId) {
                    processViewIdAnn(act, field, annotation);
                } else if (annotation instanceof Log) {
                    processLoggerAnn(act, field, annotation);
                } else if (annotation instanceof Inject) {
                    processInject(act, field, (Inject) annotation);
                }
            }
        }
    }

    private <T extends InjectView>
    void processInject(T obj, Field field, Inject annotation) {

        ParameterizedType pt= (ParameterizedType)field.getGenericType();
        Type[] genTypes = pt.getActualTypeArguments();
        Object genObj = null;
        if (genTypes != null && genTypes.length > 0) {
            Class<?>  arrayClass[] = new Class[genTypes.length];
            for (int i = 0; i < genTypes.length; i++) {
                arrayClass[i] = (Class<?>)genTypes[i];
            }
            genObj = sAppProvider.getInstance(field.getType(), arrayClass);
        } else {
            genObj = sAppProvider.getInstance(field.getType());
        }
        try {
            field.set(obj, genObj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    <T extends InjectView>
    void processLoggerAnn(T act, Field field,
                          Annotation annotation) throws IllegalAccessException {
        Log vAn = (Log) annotation;
        field.set(act, new Logger(act.getViewContext(), vAn
                .logTag(), Logger.LogLevel.INFO));
    }

    <T extends InjectView>
    void processViewIdAnn(T act, Field field,
                          Annotation annotation) throws IllegalAccessException {
        ViewId vAn = (ViewId) annotation;
        field.set(act, act.getViewById(vAn.value()));
    }


    <T extends InjectView> void processStringResAnn(T act, Field field,
                                                    Annotation annotation) throws IllegalAccessException {
        // String Injection
        StringRes srAn = (StringRes) annotation;
        field.set(act, new String(act.getViewContext().getString(srAn.value())));
    }


    public void injectApplication(Application application) {

        if ( hasClassAnnotation(application,Provider.class) ) {
            Provider vAn = getClassAnnotation(application,Provider.class);
            try {
                sAppProvider = (BaseInjProvider) vAn.value().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    <T,A extends Annotation>
    boolean hasClassAnnotation(T type,Class<A> clazz){
        return ((Object) type).getClass().
                isAnnotationPresent(clazz);
    }


    <T,A extends Annotation>
    A getClassAnnotation(T type,Class<A> clazz){
        return (A)((T) type).getClass().getAnnotation(clazz);
    }
}
