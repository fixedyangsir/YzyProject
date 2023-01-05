package com.yzy.lib_common.base.fragment;

import android.content.res.Configuration;

import com.gyf.immersionbar.components.SimpleImmersionOwner;

/**
 * Fragment快速实现沉浸式的代理类
 */
public class QuickImmersionProxy {
    /**
     * 要操作的Fragment对象
     */
    private BaseVmFragment mFragment;
    /**
     * 沉浸式实现接口
     */
    private SimpleImmersionOwner mSimpleImmersionOwner;



    public QuickImmersionProxy(BaseVmFragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof SimpleImmersionOwner) {
            this.mSimpleImmersionOwner = (SimpleImmersionOwner) fragment;
        } else {
            throw new IllegalArgumentException("Fragment SimpleImmersionOwner");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser){
            setImmersionBar();
        }

    }


    public void onDestroy() {
        mFragment = null;
        mSimpleImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        setImmersionBar();
    }

    public void onHiddenChanged(boolean hidden) {
        if (mFragment != null) {
            mFragment.setUserVisibleHint(!hidden);
        }
    }

    /**
     * 是否已经对用户可见
     * Is user visible hint boolean.
     *
     * @return the boolean
     */
    public boolean isUserVisibleHint() {
        if (mFragment != null) {
            return mFragment.getUserVisibleHint();
        } else {
            return false;
        }
    }

    private void setImmersionBar() {
        if (mFragment != null && mFragment.getUserVisibleHint()
                && mSimpleImmersionOwner.immersionBarEnabled()) {
            mSimpleImmersionOwner.initImmersionBar();
        }
    }
}
