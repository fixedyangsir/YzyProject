package com.yzy.module_base.base

import android.os.Bundle
import android.view.Gravity
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.toast.Toaster
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.yzy.lib_common.base.activity.BaseVmDbActivity
import com.yzy.lib_common.ext.dp
import com.yzy.lib_common.ext.getAppViewModel
import com.yzy.lib_common.ext.getResColor
import com.yzy.lib_common.widget.dialog.LoadingDialog
import com.yzy.module_base.R
import com.yzy.module_base.event.AppViewModel
import com.yzy.module_base.event.EventViewModel
import com.yzy.module_base.widget.dialog.AppLoadingDialog


abstract class BaseActivity< DB : ViewDataBinding> : BaseVmDbActivity<DB>() {

    //Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    //Application全局的ViewModel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }


    override fun onCreate(savedInstanceState: Bundle?) {
        initImmersionBar()
        super.onCreate(savedInstanceState)
    }

    open fun initImmersionBar() {
        immersionBar {
            statusBarColor(R.color.color_bg)
            navigationBarColor(R.color.color_bg)
            statusBarDarkFont(true)   //状态栏字体是深色，不写默认为亮色
            navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
        }
    }

    override fun createLoadingDialog(): LoadingDialog {
        val loadingDialog = AppLoadingDialog(this, R.layout.dialog_loading)
        return XPopup.Builder(this)
            .dismissOnTouchOutside(false).hasShadowBg(false)
            .isDestroyOnDismiss(false)
            .popupAnimation(PopupAnimation.NoAnimation)
            .navigationBarColor(R.color.colorPrimary.getResColor())
            .asCustom(loadingDialog) as LoadingDialog
    }




}