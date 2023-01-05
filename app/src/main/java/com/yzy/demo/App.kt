package com.yzy.demo

import com.alibaba.android.arouter.launcher.ARouter
import com.yzy.lib_common.base.BaseApp

/**
 * Created by yzy on 2023/1/4.
 */
class App: BaseApp() {
    override fun initByAppProcess() {

    }

    override fun getAppId(): String {
        return BuildConfig.APPLICATION_ID
    }


}