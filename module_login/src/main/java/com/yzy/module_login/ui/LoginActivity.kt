package com.yzy.module_login.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.yzy.lib_common.base.mvi.LoadingEvent
import com.yzy.lib_common.base.mvi.ToastEvent
import com.yzy.lib_common.base.mvi.extension.collectSingleEvent
import com.yzy.lib_common.base.mvi.extension.collectState
import com.yzy.lib_common.ext.clickNoRepeat
import com.yzy.lib_common.ext.input
import com.yzy.lib_common.util.logd
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.module.RequestPermissionIntent
import com.yzy.module_base.module.RequestPermissionUiState
import com.yzy.module_base.module.RequestPermissionVM
import com.yzy.module_base.utils.ToastUtils.showToast
import com.yzy.module_login.R
import com.yzy.module_login.databinding.ActivityLoginBinding
import com.yzy.module_login.ui.vm.LoginIntent
import com.yzy.module_login.ui.vm.LoginUIState
import com.yzy.module_login.ui.vm.LoginVM


/**
 * 登录
 * 账号：yzy888 密码 123456
 *
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private val viewModel: LoginVM by viewModels()
    private val requestPermissionModel: RequestPermissionVM by viewModels()

    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {


        mDatabind.tvLogin.clickNoRepeat {
            val account = mDatabind.etAccount.input()
            val pwd = mDatabind.etPwd.input()

           // viewModel.dispatch(LoginIntent.Login(account, pwd))


            requestPermissionModel.dispatch(RequestPermissionIntent.RequestCarmaPermissionIntent(this))
        }

    }


    override fun createObserver() {


        viewModel.container.uiStateFlow.collectState(this) {
             when (it) {
                 is LoginUIState.LoginSuccessState -> {

                      "LoginSuccessState".logd()
                 }

                 else -> {}
             }
        }



        requestPermissionModel.container.uiStateFlow.collectState(this){

            when(it){
                is RequestPermissionUiState.RequestCarmaPermissionSuccessState->{
                    showToast("授权成功")
                }


                else -> {}
            }


        }




        viewModel.container.singleEventFlow.collectSingleEvent(this) {
            when (it) {
                is ToastEvent -> {
                    showToast(it.message)
                }

                is LoadingEvent -> {
                    if (it.isShow) showLoading() else dismissLoading()
                }
            }
        }


    }

}