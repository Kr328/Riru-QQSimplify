package com.github.kr328.qq.simplify;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

class LayoutBlocker {
    private static final UiUtils.Matcher[] MINI_APP_CHAIN = new UiUtils.Matcher[]{
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.mini.entry.desktop.MiniAppDesktopLayout"),
            new UiUtils.ClassMatcher(FrameLayout.class),
            new UiUtils.ClassMatcher(LinearLayout.class),
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.mini.entry.desktop.widget.DragRecyclerView")
    };

    private static final UiUtils.Matcher[] TAB_CHAIN = new UiUtils.Matcher[] {
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.widget.QQTabHost"),
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.activity.recent.cur.DragFrameLayout"),
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.widget.QQTabWidget")
    };

    private static final UiUtils.Matcher[] CAMERA_CHAIN = new UiUtils.Matcher[] {
            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
            new UiUtils.ClassMatcher(android.widget.ImageView.class),
    };

    private static final UiUtils.Matcher[] PLAY_CHAIN = new UiUtils.Matcher[] {
            new UiUtils.ClassNameMatcher("com.tencent.widget.FitSystemWindowsRelativeLayout"),
            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.widget.navbar.NavBarAIO"),
            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
            new UiUtils.ClassMatcher(android.widget.ImageView.class),
//            new UiUtils.ClassMatcher(android.widget.RelativeLayout.class),
//            new UiUtils.ClassMatcher(android.widget.LinearLayout.class),
//            new UiUtils.ClassNameMatcher("com.tencent.mobileqq.widget.QVipMedalView")
    };

    private static final UiUtils.Matcher[] TAB_ELEMENT_CHAIN = new UiUtils.Matcher[] {
            new UiUtils.ClassMatcher(ViewGroup.class),
            new UiUtils.ClassMatcher(RelativeLayout.class),
            new UiUtils.ClassMatcher(TextView.class)
    };

    private static final String[] TAB_BLOCK_CONTENTS = new String[] {
            "\u52a8\u6001", // 动态
            "\u770b\u70b9", // 看点
    };

    static final String CAMERA_DESCRIPTION = "\u62cd\u6444"; // 拍摄

    View block(View view) {
        if ( Constants.DISABLE )
            return view;

        blockTabs(view);
        blockMiniApps(view);
        blockCamera(view);
        blockPlay(view);

        return view;
    }

    private void blockMiniApps(View view) {
        UiUtils.walk(MINI_APP_CHAIN, view, (chains) -> {
            chains[0].setVisibility(View.INVISIBLE);
        });
    }

    private void blockTabs(View view) {
        UiUtils.walk(TAB_CHAIN, view, (chains) -> {
            View v = chains[chains.length-1];

            if (v instanceof ViewGroup) {
                ViewGroup tabWidget = (ViewGroup) v;

                tabWidget.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                    @Override
                    public void onChildViewAdded(View parent, View child) {
                        UiUtils.walk(TAB_ELEMENT_CHAIN, child, (chains) -> {
                            CharSequence text = ((TextView)chains[chains.length-1]).getText();

                            for ( String blocked : TAB_BLOCK_CONTENTS ) {
                                if ( blocked.contentEquals(text) )
                                    chains[0].setVisibility(View.GONE);
                            }
                        });
                    }

                    @Override
                    public void onChildViewRemoved(View view, View view1) {

                    }
                });
            }
        });
    }

    private void blockCamera(View view) {
        UiUtils.walk(CAMERA_CHAIN, view, (chains) -> {
            ImageView imageView = (ImageView) chains[chains.length-1];

            if ( imageView.getContentDescription() != null && CAMERA_DESCRIPTION.contentEquals(imageView.getContentDescription()) ) {
                chains[chains.length-1].setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
            }
        });
    }

    private void blockPlay(View view) {
        final AtomicBoolean finished = new AtomicBoolean(false);

        UiUtils.walk(PLAY_CHAIN, view, (chains) -> {
            if ( finished.getAndSet(true) ) return;

            ImageView imageView = (ImageView) chains[chains.length-1];

            imageView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
        });
    }
}
