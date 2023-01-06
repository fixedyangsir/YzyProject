package com.yzy.demo

import com.yzy.lib_common.base.BaseApp


class App: BaseApp() {
    override fun initByAppProcess() {

    }

    override fun getAppId(): String {
        return BuildConfig.APPLICATION_ID
    }


}