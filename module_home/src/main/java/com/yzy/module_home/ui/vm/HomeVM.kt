package com.yzy.module_home.ui.vm

import androidx.lifecycle.ViewModel
import com.yzy.lib_common.base.mvi.Container
import com.yzy.lib_common.base.mvi.UiEvent
import com.yzy.lib_common.base.mvi.UiState
import com.yzy.lib_common.base.mvi.extension.containers

class HomeVM : ViewModel() {

    private val _container by containers<HomeUIState>(HomeUIState.INIT)

    val container: Container<HomeUIState, UiEvent> = _container

    fun dispatch(intent: HomeIntent) {



    }

}

sealed class HomeUIState() : UiState {
    object INIT : HomeUIState()

}

sealed class HomeIntent() {

}