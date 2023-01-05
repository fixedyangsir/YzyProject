package com.yzy.lib_common.base.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.yzy.lib_common.util.LogUtil
import github.leavesczy.eventlivedata.EventLiveData

/**
 * 用于监听前后台切换
 */
object KtxAppLifeObserver : LifecycleObserver {

    var isForeground = EventLiveData<Boolean>(false)

    //在前台
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private  fun onForeground() {
        isForeground.submitValue(true)
        LogUtil.d("onForeground")
    }

    //在后台
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground.submitValue(false)
        LogUtil.d( "onBackground")
    }

}