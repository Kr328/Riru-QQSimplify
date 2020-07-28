package com.github.kr328.qq.simplify;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class UiUtils {
    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> findViewByType(View view, Class<T> clazz, int deep) {
        ArrayList<T> result = new ArrayList<>(0);

        if ( deep <= 0 )
            return result;

        if ( clazz.isInstance(view) )
            result.add((T) view);

        if (!( view instanceof ViewGroup) )
            return result;

        ViewGroup vg = (ViewGroup) view;

        for ( int i = 0 ; i < vg.getChildCount() ; i++ )
            result.addAll(findViewByType(vg.getChildAt(i), clazz, deep - 1));

        return result;
    }

    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> findViewByClassName(View view, String clazz, int deep) {
        ArrayList<T> result = new ArrayList<>(0);

        if ( deep <= 0 )
            return result;

        if ( clazz.equals(view.getClass().getName()) )
            result.add((T) view);

        if (!( view instanceof ViewGroup) )
            return result;

        ViewGroup vg = (ViewGroup) view;

        for ( int i = 0 ; i < vg.getChildCount() ; i++ )
            result.addAll(findViewByClassName(vg.getChildAt(i), clazz, deep - 1));

        return result;
    }

    @SuppressWarnings("unchecked")
    static ArrayList<ViewGroup> findParentViewByClassName(View root, String clazz ,int deep) {
        ArrayList<ViewGroup> result = new ArrayList<>(0);

        if ( deep <= 0 )
            return result;

        if (!( root instanceof ViewGroup) )
            return result;

        ViewGroup vg = (ViewGroup) root;

        for ( int i = 0 ; i < vg.getChildCount() ; i++ ) {
            View v = vg.getChildAt(i);

            if ( clazz.equals(v.getClass().getName()) )
                result.add(vg);

            result.addAll(findParentViewByClassName(v, clazz, deep - 1));
        }

        return result;
    }

    static void attachAllClickListener(View root, View.OnClickListener listener, int deep) {
        if (!( root instanceof ViewGroup) )
            return;

        ViewGroup vg = (ViewGroup) root;

        for ( int i = 0 ; i < vg.getChildCount() ; i++ ) {
            View v = vg.getChildAt(i);

            attachAllClickListener(v, listener, deep - 1);

            try {
                v.setOnClickListener(listener);
            } catch (Exception e) {
                Log.d(Constants.TAG, v.toString());
            }
        }
    }
}
