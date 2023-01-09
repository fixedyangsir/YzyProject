package com.yzy.demo.ui

import android.content.Intent
import android.os.Bundle

import com.yzy.demo.R
import com.yzy.demo.databinding.ActivitySplashBinding
import com.yzy.demo.ui.vm.SplashVM
import com.yzy.lib_common.ext.goActivity
import com.yzy.lib_common.util.RunUtils
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.AppCache
import com.yzy.module_home.ui.HomeActivity
import com.yzy.module_login.ui.LoginActivity

class SplashActivity : BaseActivity<SplashVM, ActivitySplashBinding>() {

    val runnable by lazy {
        Runnable {
            if (AppCache.isLogin()){
                goActivity<HomeActivity>()
            }else{
                goActivity<LoginActivity>()
            }
            finish()
        }
    }


    override fun layoutId() = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {
        //防止热启动显示启动页
        if (!this.isTaskRoot) {
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                finish()
                return
            }
        }




    }

    override fun createObserver() {

        RunUtils.runOnUIThreadDelayed(runnable, 1500)
    }

    override fun onBackPressed() {
        //super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        RunUtils.removeRunnable(runnable)
    }

}