package com.github.kr328.qq.simplify;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class UiUtils {
    interface Matcher {
        boolean match(View view);
    }

    static class ClassMatcher implements Matcher {
        final Class<?> clazz;

        ClassMatcher(Class<?> clazz) {
            this.clazz = clazz;
        }

        @Override
        public boolean match(View view) {
            return clazz.isAssignableFrom(view.getClass());
        }
    }

    static class ClassNameMatcher implements Matcher {
        final String name;

        ClassNameMatcher(String name) {
            this.name = name;
        }

        @Override
        public boolean match(View view) {
            return name.equals(view.getClass().getName());
        }
    }

    static class AndMatcher implements Matcher {
        final Matcher[] matchers;

        AndMatcher(Matcher ...matchers) {
            this.matchers = matchers;
        }

        @Override
        public boolean match(View view) {
            return Stream.of(matchers).allMatch(m -> m.match(view));
        }
    }

    static void walk(Matcher[] chains, View root, Consumer<View[]> consumer) {
        if ( !chains[0].match(root) )
            return;

        View[] cache = new View[chains.length];

        cache[0] = root;

        walk(chains, root, consumer, cache, 1);
    }

    private static void walk(Matcher[] chains, View view, Consumer<View[]> consumer, View[] cache, int offset) {
        if ( offset == chains.length ) {
            consumer.accept(cache);
            return;
        }

        if (!(view instanceof ViewGroup))
            return;

        ViewGroup group = (ViewGroup) view;

        for ( int i = 0 ; i < group.getChildCount() ; i++ ) {
            View v = group.getChildAt(i);

            if ( chains[offset].match(v) ) {
                cache[offset] = v;
                walk(chains, group.getChildAt(i), consumer, cache, offset + 1);
            }
        }
    }

    static void attachLongClickDump(View view, String text) {
        final String currentText = text + "/" + view.getClass().getName();

        view.setOnLongClickListener((v) -> {
            Log.d(Constants.TAG, currentText);

            return false;
        });

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for ( int i = 0 ; i < group.getChildCount() ; i++ ) {
                attachLongClickDump(group.getChildAt(i), currentText);
            }
        }
    }

    static void attachAllLayoutChanged(View view, ViewGroup.OnHierarchyChangeListener listener) {
        if (!( view instanceof ViewGroup))
            return;

        ViewGroup group = (ViewGroup) view;

        group.setOnHierarchyChangeListener(listener);

        for ( int i = 0 ; i < group.getChildCount(); i++) {
            attachAllLayoutChanged(group.getChildAt(i), listener);
        }
    }

    static void dumpView(View root, String padding) {
        if ( root == null )
            return;

        Log.d(Constants.TAG, padding + root.getClass().getName());

        if ( root instanceof ViewGroup ) {
            ViewGroup group = (ViewGroup) root;

            for ( int i = 0 ; i < group.getChildCount() ; i++ ) {
                dumpView(group.getChildAt(i), padding + " ");
            }
        }
    }
}
