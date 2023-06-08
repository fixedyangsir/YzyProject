package com.yzy.module_mine.ui.vm

import androidx.lifecycle.ViewModel
import com.yzy.lib_common.base.mvi.Container
import com.yzy.lib_common.base.mvi.UiEvent
import com.yzy.lib_common.base.mvi.UiState
import com.yzy.lib_common.base.mvi.extension.containers

class MineVM : ViewModel() {

    private val _container by containers<MineUIState>(MineUIState.INIT)

    val container: Container<MineUIState, UiEvent> = _container

    fun dispatch(intent: MineIntent) {

    }

}

sealed class MineUIState() : UiState {
    object INIT : MineUIState()

}

sealed class MineIntent() {

}