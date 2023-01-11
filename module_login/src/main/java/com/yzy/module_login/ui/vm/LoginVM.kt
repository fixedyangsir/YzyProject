package com.yzy.module_login.ui.vm

import com.yzy.lib_common.base.viewmodel.BaseViewModel
import com.yzy.lib_common.base.viewmodel.ISingleUiState
import com.yzy.lib_common.base.viewmodel.IUiState
import com.yzy.lib_common.ext.getResString
import com.yzy.module_base.bean.UserInfo
import com.yzy.module_base.utils.AppCache
import com.yzy.module_login.R
import com.yzy.module_login.http.LoginRepository

class LoginVM : BaseViewModel<LoginUIState, LoginSingleUiState>() {


    private val repo = LoginRepository()


    override fun initUiState(): LoginUIState {
        return LoginUIState.INIT
    }

    /**
     * 分发用户事件
     * @param LoginIntent
     */
    fun dispatch(intent: LoginIntent) {

        when (intent) {
            is LoginIntent.Login -> {


                if (!checkInput(intent.name)) {
                    showToast(R.string.str_account_input_tip.getResString())
                    return
                }
                if (!checkInput(intent.pwd)) {
                    showToast(R.string.str_pwd_input_tip.getResString())
                    return
                }
                requestDataWithFlow(showLoading = true,
                    request = { repo.requestLogin(intent.name, intent.pwd) },
                    successCallback = { data ->
                        showToast(R.string.str_login_success.getResString())
                        AppCache.saveUserInfo(data)
                        sendUiState {
                            LoginUIState.LoadSuccess(data)
                        }
                    },
                    failCallback = {
                        showToast(it)
                    })

            }


        }

    }

    private fun checkInput(data: String): Boolean {
        return data.length > 5
    }


    private fun showToast(message: String) {
        sendSingleUiState(LoginSingleUiState(message))
    }


}


sealed class LoginUIState() : IUiState {
    object INIT : LoginUIState()

    /**
     * 请求成功
     */
    data class LoadSuccess(val userInfo: UserInfo) : LoginUIState()
}

data class LoginSingleUiState(val message: String) : ISingleUiState


sealed class LoginIntent() {

    /**
     * 登录
     */
    data class Login(val name: String, val pwd: String) : LoginIntent()

}