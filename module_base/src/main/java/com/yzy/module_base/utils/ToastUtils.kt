package com.yzy.module_base.utils

import android.view.Gravity
import com.hjq.toast.Toaster
import com.yzy.lib_common.ext.dp
import com.yzy.module_base.R

/**
 * Created by yzy on 2023/5/30.
 */
object ToastUtils {
    init {
        Toaster.setView(R.layout.layout_toast)
    }

    fun showToast(msg: String) {
        Toaster.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 50.dp())
        Toaster.showShort(msg)
    }
}