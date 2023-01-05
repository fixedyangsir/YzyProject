package com.yzy.lib_common.widget.dialog;

import android.content.Context;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;

import androidx.annotation.NonNull;

/**
 * Created by yzy on 2020/12/15.
 */

public abstract class LoadingDialog extends CenterPopupView {
    private TextView tv_title;

    /**
     * @param context
     * @param bindLayoutId layoutId 如果要显示标题，则要求必须有id为tv_title的TextView，否则无任何要求
     */
    public LoadingDialog(@NonNull Context context, int bindLayoutId) {
        super(context);
        this.bindLayoutId = bindLayoutId;
        addInnerContent();
    }

    @Override
    protected int getImplLayoutId() {
        return bindLayoutId != 0 ? bindLayoutId : -1;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = getTitle();
        setup();
    }

    public abstract TextView getTitle();

    protected void setup() {
        if (title != null && title.length() != 0 && tv_title != null) {
            post(new Runnable() {
                @Override
                public void run() {
                    if (tv_title.getText().length() != 0) {
                        TransitionManager.beginDelayedTransition((ViewGroup) tv_title.getParent(), new TransitionSet()
                                .setDuration(XPopup.getAnimationDuration())
                                .addTransition(new ChangeBounds()));
                    }
                    tv_title.setVisibility(VISIBLE);
                    tv_title.setText(title);
                }
            });
        }
    }

    private CharSequence title;

    public LoadingDialog setTitle(CharSequence title) {
        this.title = title;
        setup();
        return this;
    }
}