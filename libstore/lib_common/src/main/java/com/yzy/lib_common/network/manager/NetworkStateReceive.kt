package com.yzy.mvvmlib.network.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.yzy.lib_common.R
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.ext.getResString

import com.yzy.lib_common.network.Error
import com.yzy.lib_common.network.manager.NetState
import com.yzy.lib_common.network.manager.NetworkStateManager
import com.yzy.lib_common.util.NetworkUtil
import com.yzy.lib_common.util.logd


/**
 * 网络状态接收
 */
class NetworkStateReceive : BroadcastReceiver() {
    var isInit = true


    override fun onReceive(context: Context, intent: Intent) {

        "NetworkStateReceive action==${intent.action}".logd()
        if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            if (!isInit) {
                if (!NetworkUtil.isNetworkAvailable(context)) {
                    "NetworkStateReceive 无网络".logd()
                    //收到没有网络时判断之前的值是不是有网络，如果有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkStateCallback.getValue()?.let {
                        if (it.isSuccess) {
                            //没网
                            NetworkStateManager.instance.mNetworkStateCallback.submitValue(
                                NetState(
                                    isSuccess = false,tips = R.string.str_network_error.getResString()
                                )
                            )
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.submitValue(
                        NetState(
                            isSuccess = false,tips = R.string.str_network_error.getResString()
                        )
                    )
                } else {
                    "NetworkStateReceive 有网络".logd()
                    //收到有网络时判断之前的值是不是没有网络，如果没有网络才提示通知 ，防止重复通知
                    NetworkStateManager.instance.mNetworkStateCallback.getValue()?.let {

                        "mNetworkStateCallback =====${it.isSuccess}".logd()

                        if (!it.isSuccess) {
                            //有网络了
                            NetworkStateManager.instance.mNetworkStateCallback.submitValue(
                                NetState(
                                    isSuccess = true
                                )








                            )
                        }
                        return
                    }
                    NetworkStateManager.instance.mNetworkStateCallback.submitValue(
                        NetState(
                            isSuccess = true
                        )
                    )
                }
            }
            isInit = false
        } else if (intent.action == Ktx.NETWORKACTION) {

            val code = intent.getIntExtra("code",1006)
            var msg=R.string.str_timeout_error.getResString()
            when (code){
               Error.UNKNOWN.getKey()->{
                    msg=R.string.str_unknown_error.getResString()
                }
                Error.PARSE_ERROR.getKey() ->{
                    msg=R.string.str_parse_error.getResString()
                }
               Error.NETWORK_ERROR.getKey()->{
                    msg=R.string.str_network_error.getResString()
                }
                Error.SSL_ERROR.getKey() ->{
                    msg=R.string.str_ssl_error.getResString()
                }
               Error.TIMEOUT_ERROR.getKey()  ->{
                    msg=R.string.str_timeout_error.getResString()
                }
                Error.SPECIAL_TIMEOUT_ERROR.getKey()  ->{
                    msg=R.string.str_timeout_error.getResString()
                }
                Error.HTTP_NULL_ERROR.getKey()  ->{
                    msg=R.string.str_network_error.getResString()
                }
            }
            //网络超时
            NetworkStateManager.instance.mNetworkStateCallback.submitValue(
                NetState(
                    isSuccess = false,
                    tips = msg,
                    type =  code
                )
            )
        }
    }
}