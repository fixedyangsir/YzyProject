package com.yzy.module_base


import com.kingja.loadsir.core.LoadSir
import com.yzy.lib_common.base.BaseApp
import com.yzy.lib_common.ext.getResString
import com.yzy.lib_common.widget.MultiStateConfig
import com.yzy.lib_common.widget.MultiStatePage
import com.yzy.module_base.widget.state.EmptyPageState
import com.yzy.module_base.widget.state.ErrorPageState
import com.yzy.module_base.widget.state.LoadingPageState


/**
 * Created by yzy on 2023/5/30.
 */
abstract class CommonApp : BaseApp() {
    override fun initByAppProcess() {
        initPageState()
    }


    /**
     * 初始化状态页配置
     */
    private fun initPageState() {
        val config =
            MultiStateConfig.Builder().errorIcon(R.mipmap.ic_empty).emptyIcon(R.mipmap.ic_empty)
                .emptyMsg(R.string.str_default_empty_text.getResString())
                .loadingMsg(R.string.str_default_loading_text.getResString())
                .errorMsg(R.string.str_default_error_text.getResString()).build()
        MultiStatePage.config(config)

        LoadSir.beginBuilder().addCallback(LoadingPageState())//加载
            .addCallback(ErrorPageState())//错误
            .addCallback(EmptyPageState())//空
            .commit()
    }

}