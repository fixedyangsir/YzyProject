package com.yzy.demo

import com.yzy.module_base.CommonApp


class App : CommonApp() {
    override fun initByAppProcess() {
        super.initByAppProcess()

    }

    override fun getAppId(): String {
        return BuildConfig.APPLICATION_ID
    }


}