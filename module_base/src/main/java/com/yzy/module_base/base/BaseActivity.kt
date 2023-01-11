package com.yzy.module_base.base

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.toast.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupAnimation
import com.yzy.lib_common.base.activity.BaseVmDbActivity
import com.yzy.lib_common.ext.dp
import com.yzy.lib_common.ext.getResColor
import com.yzy.lib_common.util.MyToastStrategy
import com.yzy.lib_common.widget.dialog.LoadingDialog
import com.yzy.module_base.R
import com.yzy.module_base.widget.dialog.AppLoadingDialog


abstract class BaseActivity< DB : ViewDataBinding> : BaseVmDbActivity<DB>() {


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

    override fun showToast(msg: String) {
        ToastUtils.setView(R.layout.layout_toast)
        ToastUtils.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 50.dp())
        (ToastUtils.getStrategy() as MyToastStrategy).duration = Toast.LENGTH_SHORT
        ToastUtils.show(msg)
    }


}