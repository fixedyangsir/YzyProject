package com.yzy.lib_common.base.viewmodel

/**
 * Created by yzy on 2023/1/11.
 */
interface IUiState //重复性事件 可以多次消费
interface ISingleUiState //一次性事件，不支持多次消费

object EmptySingleState : ISingleUiState

//一次性事件，不支持多次消费
sealed class LoadUiState {
    data class Loading(var isShow: Boolean) : LoadUiState()
    object ShowMainView : LoadUiState()
    data class Error(val msg: String) : LoadUiState()
}