package com.yzy.module_login.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.yzy.lib_common.base.viewmodel.LoadUiState
import com.yzy.lib_common.ext.JetpackExt.flowWithLifecycle2
import com.yzy.lib_common.ext.clickNoRepeat
import com.yzy.lib_common.ext.input
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ARouterUtils
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

    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {


        mDatabind.tvLogin.clickNoRepeat {
            val account = mDatabind.etAccount.input()
            val pwd = mDatabind.etPwd.input()

            viewModel.dispatch(LoginIntent.Login(account, pwd))
        }

    }


    override fun createObserver() {
        /**
         * Load加载事件 Loading、Error
         */
        viewModel.loadUiStateFlow.flowWithLifecycle2(this) { state ->
            when (state) {
                is LoadUiState.Loading -> if (state.isShow) showLoading() else dismissLoading()
            }
        }

        /**
         * 一次性消费事件
         */
        viewModel.sUiStateFlow.flowWithLifecycle2(this) { data ->
            showToast(data.message)
        }

        /**
         * 登录事件
         */
        viewModel.uiStateFlow.flowWithLifecycle2(this) { state ->

            when (state) {
                is LoginUIState.LoadSuccess -> {
                    ARouterUtils.goHome()
                    finish()
                }

            }

        }


    }

}