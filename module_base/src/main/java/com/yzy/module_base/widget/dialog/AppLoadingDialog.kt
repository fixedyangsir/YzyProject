package com.yzy.module_base.widget.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.yzy.lib_common.widget.dialog.LoadingDialog
import com.yzy.module_base.R


class AppLoadingDialog(context: Context, bindLayoutId: Int) : LoadingDialog(context, bindLayoutId) {


    @SuppressLint("WrongViewCast")
    override fun getTitle(): TextView {
       return findViewById(R.id.tv_title)
    }

}