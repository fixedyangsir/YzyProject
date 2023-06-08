package com.yzy.module_base.utils

import com.alibaba.android.arouter.launcher.ARouter


object ARouterUtils {
    const val PATH_HOME="/app/HomeActivity"


    fun goHome() {
        ARouter.getInstance().build(PATH_HOME)
            .navigation()
    }
}