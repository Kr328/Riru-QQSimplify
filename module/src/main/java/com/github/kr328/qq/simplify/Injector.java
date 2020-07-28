package com.github.kr328.qq.simplify;

import android.util.Log;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String argument) {
        try {
            LayoutInflaterProxy.install();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Inject failure", e);
            return;
        }

        Log.i(Constants.TAG, "Inject successfully");
    }
}
