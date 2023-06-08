package com.yzy.lib_common.base.mvi.extension


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yzy.lib_common.base.mvi.ContainerLazy
import com.yzy.lib_common.base.mvi.MutableContainer
import com.yzy.lib_common.base.mvi.UiEvent
import com.yzy.lib_common.base.mvi.UiState
import com.yzy.lib_common.network.AppException
import com.yzy.lib_common.network.BaseResponse
import com.yzy.lib_common.network.ExceptionHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * 构建viewModel的Ui容器，存储Ui状态和一次性事件
 */
fun <STATE : UiState> ViewModel.containers(
    initialState: STATE,
): Lazy<MutableContainer<STATE, UiEvent>> {
    return ContainerLazy(initialState, viewModelScope)
}

fun <T : Any> ViewModel.requestWithFlow(

    request: Flow<BaseResponse<T>>,
    onStart: (() -> Unit)? = null,
    successCallback: (T) -> Unit,
    successCallbackCanNull: ((T?) -> Unit)? = null,
    failCallback: ((AppException) -> Unit)? = null,
    completionCallback: (() -> Unit)? = null,
) {
    viewModelScope.launch {
        request.onStart {
            onStart?.invoke()
        }.flowOn(Dispatchers.IO).catch {
            val exception = ExceptionHandle.handleException(it)
            failCallback?.invoke(exception)
        }.onCompletion {
            completionCallback?.invoke()
        }.collect { response ->
            if (response.isSuccess()) {
                // sendLoadUiState(LoadUiState.ShowMainView)
                successCallbackCanNull?.let {
                    successCallbackCanNull(response.getResponseData())
                }
                response.getResponseData()?.let { successCallback(it) }
            } else {
                failCallback?.invoke(
                    AppException(
                        response.getResponseCode(), response.getResponseMsg()
                    )
                )
            }
        }
    }
}