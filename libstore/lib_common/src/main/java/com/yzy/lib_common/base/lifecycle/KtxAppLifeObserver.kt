package com.yzy.lib_common.base.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.yzy.lib_common.util.LogUtil

/**
 * 用于监听前后台切换
 */
object KtxAppLifeObserver : LifecycleObserver {

    var isForeground = MutableLiveData<Boolean>(false)

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private  fun onForeground() {
        isForeground.postValue(true)
        LogUtil.d("onForeground")
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground.postValue(false)
        LogUtil.d( "onBackground")
    }

}