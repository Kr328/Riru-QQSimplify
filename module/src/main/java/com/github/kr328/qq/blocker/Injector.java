package com.github.kr328.qq.blocker;

import android.util.Log;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String argument) {
        try {
            LayoutInflaterProxy.install();
        } catch (ReflectiveOperationException e) {
            Log.e(Constants.TAG, "Inject failure", e);
        }

        Log.i(Constants.TAG, "Inject successfully");
    }
}
