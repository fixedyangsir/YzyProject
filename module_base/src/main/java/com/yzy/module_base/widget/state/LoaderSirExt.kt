package com.yzy.module_base.widget.state

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yzy.lib_common.widget.MultiStatePage
import com.yzy.module_base.R

/**
 * 状态页扩展
 */
fun View.loadServiceInit(callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(this) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    return loadsir
}

fun LoadService<*>.showEmpty(
    emptyMsg: String = MultiStatePage.config.emptyMsg,
    emptyIcon: Int = MultiStatePage.config.emptyIcon,
    bgColor: Int = Color.WHITE
) {
    setCallBack(
        EmptyPageState::class.java
    ) { context, view ->
        view.findViewById<TextView>(R.id.empty_text).text = emptyMsg
        view.findViewById<ImageView>(R.id.empty_img).setImageResource(emptyIcon)
        view.findViewById<ViewGroup>(R.id.empty_bg).setBackgroundColor(bgColor)
    }
    showCallback(EmptyPageState::class.java)

}

fun LoadService<*>.showError(
    errorMsg: String = MultiStatePage.config.errorMsg,
    errorIcon: Int = MultiStatePage.config.errorIcon,
    bgColor: Int = Color.WHITE
) {
    setCallBack(
        ErrorPageState::class.java
    ) { context, view ->
        view.findViewById<TextView>(R.id.error_text).text = errorMsg
        view.findViewById<ImageView>(R.id.error_img).setImageResource(errorIcon)
        view.findViewById<ViewGroup>(R.id.error_bg).setBackgroundColor(bgColor)
    }
    showCallback(ErrorPageState::class.java)
}

fun LoadService<*>.showLoading(loadingMsg: String = MultiStatePage.config.loadingMsg) {
    setCallBack(
        LoadingPageState::class.java
    ) { context, view ->
        view.findViewById<TextView>(R.id.tv_loading).text = loadingMsg
    }
    showCallback(LoadingPageState::class.java)
}