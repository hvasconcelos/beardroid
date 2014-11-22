package com.bearstouch.beardroid.core.injection;

import com.bearstouch.beardroid.core.injection.annotation.Singleton;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 *
 * Created by heldervasc on 22/11/14.
 */
public class BaseInjProvider {

    Map<Integer, Class> mImplemMap = new HashMap<Integer, Class>();
    Map<Integer, Object> mSingletonMap = new HashMap<Integer, Object>();

    public BaseInjProvider() {}

    protected void registerProvider(Class base, Class impl, Class... genericParams) {
        int key=genHashCode(base, genericParams);
        mImplemMap.put(key, impl);
    }

    private int genHashCode(Class base, Class... genericParams) {
        final int prime = 31;
        int result = 1;
        for (int i = 0; i < genericParams.length; i++) {
            result = prime * result + genericParams[i].hashCode();
        }
        return result;
    }

    Object getInstance(Class base, Class... genericParams) {
        Object res = null;
        int key = genHashCode(base, genericParams);
        // Unknown Class
        if (!mImplemMap.containsKey(key))
            return res;

        // Singleotn return always the same instance
        if ( mSingletonMap.containsKey(key) )
            return mSingletonMap.get(key);

        try {
            Class impl = mImplemMap.get(key);
            res = impl.newInstance();
            if ( res!=null && impl.isAnnotationPresent(Singleton.class) )
                mSingletonMap.put(key, res);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return res;
    }



}
