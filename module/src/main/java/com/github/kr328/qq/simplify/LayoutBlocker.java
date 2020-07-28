package com.github.kr328.qq.simplify;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import java.util.ArrayList;

class LayoutBlocker {
    private static final int MAX_DEEP = 30;

    View block(View view) {
        if ( Constants.DISABLE )
            return view;

        blockTabs(view);
        blockMiniApps(view);
        blockCamera(view);

        return view;
    }

    private void blockMiniApps(View view) {
        if (!( view instanceof ViewGroup ))
            return;

        ArrayList<? extends View> views = UiUtils.findViewByClassName(view, "com.tencent.mobileqq.widget.PullRefreshHeader", MAX_DEEP);

        for ( View v : views ) {
            v.setVisibility(View.GONE);
        }
    }

    private void blockTabs(View view) {
        if (!( view instanceof ViewGroup ))
            return;

        ArrayList<? extends View> result =
                UiUtils.findViewByClassName(view, "com.tencent.mobileqq.widget.QQTabWidget", MAX_DEEP);

        for ( View v : result ) {
            TabWidget tabWidget = (TabWidget) v;

            tabWidget.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    ArrayList<TextView> textViews = UiUtils.findViewByType(child, TextView.class, MAX_DEEP);

                    for ( TextView textView : textViews ) {
                        if ( Constants.CONTACTS_TAB_NAME.contentEquals(textView.getText()) )
                            return;
                        else if ( Constants.MESSAGE_TAB_NAME.contentEquals(textView.getText()) )
                            return;
                    }

                    child.setVisibility(View.GONE);
                }

                @Override
                public void onChildViewRemoved(View view, View view1) {

                }
            });
        }
    }

    private void blockCamera(View view) {
        ArrayList<ImageView> result = UiUtils.findViewByType(view, ImageView.class, MAX_DEEP);

        for ( ImageView imageView : result ) {
            if ( imageView.getContentDescription() == null )
                continue;

            if (Constants.CAMERA_DESCRIPTION.equals(imageView.getContentDescription().toString())) {
                imageView.setVisibility(View.GONE);
            }
        }
    }
}
