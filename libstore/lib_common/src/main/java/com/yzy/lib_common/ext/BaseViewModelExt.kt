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
fun <T> BaseVmActivity<*>.parseState(
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
fun <T> BaseVmFragment<*>.parseState(
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
 * request  请求结果需要使用parseState方法获取
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */

fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: EventLiveData<ResultState<T>>,
    isShowDialog: Boolean? = false,
    loadingMessage: String? = Ktx.app.getString(R.string.request_network),
): Job {
    return viewModelScope.launch {
        runCatching {

            if (isShowDialog != null && isShowDialog) {
                loadingMessage?.let {
                    resultState.submitValue(ResultState.onAppLoading(loadingMessage))
                } ?: let {
                    resultState.submitValue(ResultState.onAppLoading(Ktx.app.getString(R.string.request_network)))
                }

            }
            //请求体
            block()
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it?.let {
                it.printStackTrace()
                LogUtil.e(it.message)
                val e = resultState.paresException(it)
            }

        }
    }
}


/**
 *  调用协程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> BaseViewModel.launch(
    block: suspend () -> T,
    resultState: EventLiveData<ResultState<T>>,
    isShowDialog: Boolean? = false,
    loadingMessage: String? = Ktx.app.getString(R.string.request_network),
): Job {
    return viewModelScope.launch {
        kotlin.runCatching {
            isShowDialog?.let {
                if (isShowDialog) {
                    loadingMessage?.let {
                        resultState.submitValue(ResultState.onAppLoading(it))
                    }
                }
            }
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.printStackTrace()
            LogUtil.e(it.message)
            resultState.paresException(it)
        }
    }

}

/**
 *  调用协程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调
 */
fun <T> BaseViewModel.launch(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {},
): Job {
    return viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            it.printStackTrace()
            error(it)
        }
    }
}


/**
 * @param job
 */
fun BaseViewModel.cancelJob(job: Job?) {
    if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
        job.cancel()
    }
}







