package com.yzy.module_base.permission


import android.app.Activity
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.OnPermissionPageCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.PermissionFragment
import com.hjq.permissions.XXPermissions

import com.lxj.xpopup.XPopup
import com.yzy.lib_common.ext.screenWidth
import com.yzy.module_base.R
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ToastUtils
import com.yzy.module_base.widget.dialog.TipDialog


/**
 * Created by yzy on 2023/3/3.
 */
class DefaultPermissionInterceptor : IPermissionInterceptor {

    override fun launchPermissionRequest(
        activity: Activity,
        allPermissions: List<String>,
        callback: OnPermissionCallback?,
    ) {

        PermissionFragment.launch(activity, ArrayList(allPermissions),
            this@DefaultPermissionInterceptor, callback)
    }

    override fun grantedPermissionRequest(
        activity: Activity, allPermissions: List<String>,
        grantedPermissions: List<String>, allGranted: Boolean,
        callback: OnPermissionCallback?,
    ) {
        if (callback == null) {
            return
        }
        callback.onGranted(grantedPermissions, allGranted)
    }

    override fun deniedPermissionRequest(
        activity: Activity, allPermissions: List<String>,
        deniedPermissions: List<String>, doNotAskAgain: Boolean,
        callback: OnPermissionCallback?,
    ) {
        callback?.onDenied(deniedPermissions, doNotAskAgain)
        if (doNotAskAgain) {
            if (deniedPermissions.size == 1 && Permission.ACCESS_MEDIA_LOCATION == deniedPermissions[0]) {
                showToast(
                    activity.resources.getString(R.string.common_permission_media_location_hint_fail))
                return
            }
            showPermissionSettingDialog(activity, allPermissions, deniedPermissions, callback)
            return
        }
        if (deniedPermissions.size == 1) {
            val deniedPermission = deniedPermissions[0]
            if (Permission.ACCESS_BACKGROUND_LOCATION == deniedPermission) {
                showToast(
                    activity.resources.getString(R.string.common_permission_background_location_fail_hint))
                return
            }
            if (Permission.BODY_SENSORS_BACKGROUND == deniedPermission) {
                showToast(
                    activity.resources.getString(R.string.common_permission_background_sensors_fail_hint))
                return
            }
        }
        val message: String
        val permissionNames = PermissionNameConvert.permissionsToNames(activity, deniedPermissions)
        message = if (!permissionNames.isEmpty()) {
            activity.getString(R.string.common_permission_fail_assign_hint,
                PermissionNameConvert.listToString(activity, permissionNames))
        } else {
            activity.getString(R.string.common_permission_fail_hint)
        }
        showToast(message)
    }

    override fun finishPermissionRequest(
        activity: Activity, allPermissions: List<String>,
        skipRequest: Boolean, callback: OnPermissionCallback?,
    ) {
    }

    private fun showPermissionSettingDialog(
        activity: Activity?, allPermissions: List<String>,
        deniedPermissions: List<String>, callback: OnPermissionCallback?,
    ) {
        if (activity == null || activity.isFinishing || activity.isDestroyed
        ) {
            return
        }
        val message: String
        val permissionNames = PermissionNameConvert.permissionsToNames(activity, deniedPermissions)
        message = if (!permissionNames.isEmpty()) {
            activity.getString(R.string.common_permission_manual_assign_fail_hint,
                PermissionNameConvert.listToString(activity, permissionNames))
        } else {
            activity.getString(R.string.common_permission_manual_fail_hint)
        }


        XPopup.Builder(activity)
            .isDestroyOnDismiss(true)
            .popupWidth((activity as BaseActivity<*>).screenWidth)
            .asCustom(
                TipDialog(activity).apply {
                    setTitle("温馨提示")
                        .setMsg(
                            message
                        )
                        .setLeftText(
                            "取消"
                        ) {
                            dismiss()

                        }
                        .setRightText("确定") {
                            dismiss()
                            XXPermissions.startPermissionActivity(activity,
                                deniedPermissions, object : OnPermissionPageCallback {

                                    override fun onGranted() {
                                        callback?.onGranted(allPermissions, true);
                                    }

                                    override fun onDenied() {
                                        showPermissionSettingDialog(activity,
                                            allPermissions,
                                            XXPermissions.getDenied(activity, allPermissions),
                                            callback);
                                    }
                                });
                        }
                }

            )
            .show()


    }

    private fun showToast( msg: String) {
        ToastUtils.showToast(msg)
    }


}