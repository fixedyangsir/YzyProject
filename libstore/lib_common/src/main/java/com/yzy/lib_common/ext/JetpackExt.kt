package com.yzy.lib_common.ext

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

/**
 * Created by yzy on 2023/1/11.
 */
object JetpackExt {

    /**
     * LiveData扩展函数封装
     */
    fun <T> FragmentActivity.observe(liveData: MutableLiveData<T>, observer: (t: T) -> Unit) {
        liveData.observe(this) { observer(it) }
    }


    inline fun <T> Flow<T>.flowWithLifecycle2(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.(T) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch {
        //前后台切换时可以重复订阅数据。如：Lifecycle.State是STARTED，那么在生命周期进入 STARTED 状态时开始任务，在 STOPED 状态时停止订阅
        flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { block(it) }

    }

    /**
     * MVI模式中使用
     */
    inline fun <T, A> Flow<T>.flowWithLifecycle2(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        prop: KProperty1<T, A>,
        crossinline block: suspend CoroutineScope.(A) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch {
        //前后台切换时可以重复订阅数据。如：Lifecycle.State是STARTED，那么在生命周期进入 STARTED 状态时开始任务，在 STOPED 状态时停止订阅
        flowOnSingleLifecycle(lifecycleOwner.lifecycle, minActiveState)
            .map { prop.get(it) }
            .collect { block(it) }
    }

    inline fun <T> Flow<T>.flowSingleWithLifecycle2(
        lifecycleOwner: LifecycleOwner,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        crossinline block: suspend CoroutineScope.(T) -> Unit,
    ) = lifecycleOwner.lifecycleScope.launch {
        flowOnSingleLifecycle(lifecycleOwner.lifecycle, minActiveState).collect { block(it) }
    }

    /**
     *  如果不想对UI层的Lifecycle.repeatOnLifecycle/Flow.flowWithLifecycle在前后台切换时重复订阅，可以使用此方法；
     * 效果类似于Channel，不过Channel是一对一的，而这里是一对多的
     */
    fun <T> Flow<T>.flowOnSingleLifecycle(
        lifecycle: Lifecycle,
        minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
        isFirstCollect: Boolean = true,
    ): Flow<T> = callbackFlow {
        var lastValue: T? = null
        lifecycle.repeatOnLifecycle(minActiveState) {
            this@flowOnSingleLifecycle.collect {
                if ((lastValue != null || isFirstCollect) && (lastValue != it)) {
                    send(it)
                }
                lastValue = it
            }
        }
        lastValue = null
        close()
    }

}