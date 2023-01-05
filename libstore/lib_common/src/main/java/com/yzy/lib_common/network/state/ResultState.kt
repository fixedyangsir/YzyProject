package com.yzy.lib_common.network.state

import com.yzy.lib_common.ext.getDefault
import com.yzy.lib_common.network.AppException
import com.yzy.lib_common.network.BaseResponse
import com.yzy.lib_common.network.ExceptionHandle
import github.leavesczy.eventlivedata.EventLiveData


/**
 * 描述　: 自定义结果集封装类
 */
sealed class ResultState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T?,traceId:String): ResultState<T> = Success(data,traceId)
        fun <T> onAppLoading(loadingMessage: String): ResultState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ResultState<T> = Error(error)

    }

    data class Loading(val loadingMessage: String) : ResultState<Nothing>()
    data class Success<out T>(val data: T?,val traceId:String ) : ResultState<T>()
    data class Error(val error: AppException) : ResultState<Nothing>()

}


/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> EventLiveData<ResultState<T>>.paresResult(result: BaseResponse<T>) {

    submitValue(
        if (result.isSucces()) ResultState.onAppSuccess(result.getResponseData(),result.getResponseTraceId().getDefault("")) else
            ResultState.onAppError(
                AppException(
                    result.getResponseCode(),
                    result.getResponseMsg()
                )
            )
    )


}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> EventLiveData<ResultState<T>>.paresResult(result: T) {
    submitValue(ResultState.onAppSuccess(result,""))
}

/**
 * 异常转换异常处理
 */
fun <T> EventLiveData<ResultState<T>>.paresException(e: Throwable) : AppException {
    val exception= ExceptionHandle.handleException(e)
    submitValue(ResultState.onAppError(exception))
    return exception
}

