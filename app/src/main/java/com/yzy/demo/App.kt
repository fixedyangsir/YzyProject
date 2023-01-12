package com.yzy.demo

import com.yzy.lib_common.base.BaseApp
import com.yzy.lib_protocal.utils.ViewParser


class App : BaseApp() {
    override fun initByAppProcess() {
        ViewParser.init(this)
    }

    override fun getAppId(): String {
        return BuildConfig.APPLICATION_ID
    }


}