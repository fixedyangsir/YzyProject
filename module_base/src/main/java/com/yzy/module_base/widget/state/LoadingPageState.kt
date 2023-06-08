package com.yzy.module_base.widget.state


import android.content.Context
import android.view.View
import com.kingja.loadsir.callback.Callback
import com.yzy.module_base.R

class LoadingPageState : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_loading
    }

    override fun onReloadEvent(context: Context?, view: View?): Boolean {
        return true
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)

    }

    override fun onAttach(context: Context?, view: View?) {
        super.onAttach(context, view)


    }



}