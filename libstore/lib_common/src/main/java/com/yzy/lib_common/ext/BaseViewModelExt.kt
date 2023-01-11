package com.yzy.lib_common.ext


import androidx.lifecycle.viewModelScope
import com.yzy.lib_common.R
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.base.activity.BaseVmActivity
import com.yzy.lib_common.network.AppException
import com.yzy.lib_common.network.BaseResponse
import com.yzy.lib_common.network.state.ResultState
import com.yzy.lib_common.network.state.paresException
import com.yzy.lib_common.network.state.paresResult
import com.yzy.lib_common.util.LogUtil

import com.yzy.lib_common.base.fragment.BaseVmFragment
import com.yzy.lib_common.base.viewmodel.BaseViewModel
import github.leavesczy.eventlivedata.EventLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 描述　:BaseViewModel请求协程封装
 */

/**
 * 显示页面状态
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmActivity.parseState(
    resultState: ResultState<T>,
    onSuccess: (T?, String) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    needDismissDialog: Boolean = true,
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            if (needDismissDialog) {
                dismissLoading()
            }
            onSuccess(resultState.data, resultState.traceId)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }

        }
    }
}

/**
 * 解析EventLiveData中的ResultState数据源
 */
inline fun <reified T> EventLiveData<*>.parseResultState(
): T? {
    return when (val resultState = this.getValue() as ResultState<*>) {
        is ResultState.Success -> {
            resultState.data as T
        }
        else -> {
            null
        }
    }
}


/**
 * 显示页面状态
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmFragment.parseState(
    resultState: ResultState<T>,
    onSuccess: (T?, String) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null,
    needDismissDialog: Boolean = true,
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            if (needDismissDialog) {
                dismissLoading()
            }
            onSuccess(resultState.data, resultState.traceId)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}










