package com.yzy.module_base.widget.state

import com.kingja.loadsir.callback.Callback
import com.yzy.module_base.R


class ErrorPageState : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}