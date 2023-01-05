package com.yzy.lib_common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import kotlin.jvm.JvmStatic;

public class StatusView extends View {

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusView(Context context) {
        this(context, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int h = 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                if (getRootWindowInsets() != null && getRootWindowInsets().getDisplayCutout() != null && getRootWindowInsets().getDisplayCutout().getSafeInsetTop() > 0) {
                    h = getRootWindowInsets().getDisplayCutout().getSafeInsetTop();
                } else {
                    h = getStatusBarHeight();
                }
            } catch (Exception e) {
                h = getStatusBarHeight();
            }

        } else {
            h = getStatusBarHeight();
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @JvmStatic
    int getStatusBarHeight() {
        Resources resources = Resources.getSystem();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}

