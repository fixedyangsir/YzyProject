package com.yzy.module_login.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import com.yzy.lib_common.ext.clickNoRepeat
import com.yzy.lib_common.ext.goActivity
import com.yzy.lib_common.ext.input
import com.yzy.lib_common.ext.parseState
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ARouterUtils
import com.yzy.module_base.utils.AppCache

import com.yzy.module_login.R
import com.yzy.module_login.databinding.ActivityLoginBinding
import com.yzy.module_login.ui.vm.LoginVM
import java.lang.StringBuilder


/**
 * 登录:动态生成请求代码 需要make project
 * 账号：yzy888 密码 123456
 *
 */
class LoginActivity : BaseActivity<LoginVM, ActivityLoginBinding>() {
    override fun layoutId() = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.tvLogin.clickNoRepeat {
            val account = mDatabind.etAccount.input()
            val pwd = mDatabind.etPwd.input()
            if (!checkInput(account)) {
                showToast(getString(R.string.str_account_input_tip))
                return@clickNoRepeat
            }
            if (!checkInput(pwd)) {
                showToast(getString(R.string.str_pwd_input_tip))
                return@clickNoRepeat
            }
            mViewModel.login(account, pwd, true)

        }


    }

    private fun checkInput(data: String): Boolean {
        return data.length > 5
    }

    override fun createObserver() {

        mViewModel.login_LiveData.observe(this, Observer {
            parseState(it, { result, traceId ->
                showToast(getString(R.string.str_login_success))
                result?.let {
                    AppCache.saveUserInfo(result)
                    ARouterUtils.goHome()
                    finish()
                }
            }, {
                showToast(it.errorLog)
            })

        })
    }

}