package com.github.kr328.qq.blocker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.policy.PhoneLayoutInflater;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

class LayoutInflaterProxy extends PhoneLayoutInflater {
    private static Method methodGetOuterContext;

    static {
        try {
            methodGetOuterContext = Context.class.getClassLoader().loadClass("android.app.ContextImpl").getMethod("getOuterContext");
            methodGetOuterContext.setAccessible(true);
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            methodGetOuterContext = null;
            Log.w(Constants.TAG, "getOuterContext failure");
        }
    }

    private static LayoutInflater sCache;
    private LayoutBlocker blocker = new LayoutBlocker();

    private LayoutInflaterProxy(Context context) {
        super(context);
    }

    @Override
    public LayoutInflater cloneInContext(Context context) {
        return new LayoutInflaterProxy(context);
    }

    @Override
    public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
        View result = super.inflate(parser, root, attachToRoot);

        return blocker.block(result);
    }

    private static Context getOuterContext(Context context) {
        if ( methodGetOuterContext == null )
            return context;

        try {
            return (Context) methodGetOuterContext.invoke(context);
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "getOuterContext failure");
        }

        return context;
    }

    private static LayoutInflater getOrCreate(Context context) {
        if ( sCache == null )
            sCache = new LayoutInflaterProxy(getOuterContext(context));
        return sCache;
    }

    @SuppressWarnings("unchecked")
    static void install() throws ReflectiveOperationException {
        Class<?> registryClass = Class.forName("android.app.SystemServiceRegistry");
        Class<?> fetcherClass = Class.forName("android.app.SystemServiceRegistry$ServiceFetcher");

        Field fetchersField = registryClass.getDeclaredField("SYSTEM_SERVICE_FETCHERS");
        fetchersField.setAccessible(true);

        Map services = (Map) fetchersField.get(null);
        if ( services == null )
            throw new NoSuchFieldException("services not found");

        Object fetcher = services.get(Context.LAYOUT_INFLATER_SERVICE);
        if ( fetcher == null )
            throw new NoSuchFieldException("fetcher not found");

        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{fetcherClass}, (o, method, args) -> {
            if ( "getService".equals(method.getName()) ) {
                method.invoke(fetcher, args);
                return getOrCreate((Context) args[0]);
            }
            return method.invoke(fetcher, args);
        });

        services.put(Context.LAYOUT_INFLATER_SERVICE, proxy);
    }
}
