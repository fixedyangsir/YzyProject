package com.yzy.lib_common.base.mvi

import androidx.annotation.Keep

@Keep
interface UiEvent


data class ToastEvent(val message: String) : UiEvent

data class LoadingEvent(val isShow: Boolean) : UiEvent
