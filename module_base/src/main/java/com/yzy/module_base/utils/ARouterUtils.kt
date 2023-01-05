package com.yzy.module_base.utils

import com.alibaba.android.arouter.launcher.ARouter

/**
 * Created by yzy on 2023/1/5.
 */
object ARouterUtils {
    const val PATH_HOME="/app/HomeActivity"

    fun goHome() {
        ARouter.getInstance().build(PATH_HOME)
            .navigation()
    }
}