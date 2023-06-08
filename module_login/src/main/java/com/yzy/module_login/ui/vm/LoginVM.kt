package com.yzy.module_login.ui.vm

import androidx.lifecycle.ViewModel
import com.yzy.lib_common.base.mvi.Container
import com.yzy.lib_common.base.mvi.LoadingEvent
import com.yzy.lib_common.base.mvi.ToastEvent
import com.yzy.lib_common.base.mvi.UiEvent
import com.yzy.lib_common.base.mvi.UiState
import com.yzy.lib_common.base.mvi.extension.containers
import com.yzy.lib_common.base.mvi.extension.requestWithFlow
import com.yzy.lib_common.network.AppException
import com.yzy.module_base.bean.UserInfo
import com.yzy.module_login.http.LoginServiceApi

/**
 * Created by yzy on 2023/6/2.
 */
class LoginVM: ViewModel() {


    private val _container by containers<LoginUIState>(LoginUIState.INIT)

    val container: Container<LoginUIState, UiEvent> = _container


    fun dispatch(intent: LoginIntent) {


        when (intent) {
            is LoginIntent.Login -> {
                requestWithFlow(request = LoginServiceApi.login(intent.name, intent.pwd), onStart = {
                    _container.sendEvent(LoadingEvent(true))
                }, successCallback = {
                    _container.sendEvent(ToastEvent("登录成功"))
                    _container.updateState {
                        LoginUIState.LoginSuccessState(it)
                    }

                }, failCallback = {

                    _container.sendEvent(ToastEvent(it.errorLog))


                }, completionCallback = {
                    _container.sendEvent(LoadingEvent(false))
                }


                )
            }

        }


    }


}

sealed class LoginUIState() : UiState {
    object INIT : LoginUIState()


    data class LoginSuccessState(val userInfo: UserInfo) : LoginUIState()

    data class ErrorState(val exception: AppException) : LoginUIState()
}


sealed class LoginIntent() {


    data class Login(val name: String, val pwd: String) : LoginIntent()

}