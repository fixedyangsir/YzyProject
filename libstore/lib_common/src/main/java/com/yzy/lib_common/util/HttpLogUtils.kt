package com.yzy.lib_common.util

import android.text.TextUtils
import android.util.Log
import com.yzy.lib_common.BuildConfig


object HttpLogUtils {
    private const val DEFAULT_TAG = "JetpackMvvm"
    fun debugInfo(tag: String?, msg: String?) {
        if (!BuildConfig.DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        msg?.let { Log.d(tag, it) }
    }

    fun debugInfo(msg: String?) {
        debugInfo(
            DEFAULT_TAG,
            msg
        )
    }

    fun warnInfo(tag: String?, msg: String?) {
        if (!BuildConfig.DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        msg?.let { Log.w(tag, it) }
    }

    fun warnInfo(msg: String?) {
        warnInfo(
            DEFAULT_TAG,
            msg
        )
    }

    /**
     * 这里使用自己分节的方式来输出足够长度的 message
     *
     * @param tag 标签
     * @param msg 日志内容
     */
    fun debugLongInfo(tag: String?, msg: String) {
        var msg = msg
        if (!BuildConfig.DEBUG || TextUtils.isEmpty(msg)) {
            return
        }
        msg = msg.trim { it <= ' ' }
        var index = 0
        val maxLength = 3500
        var sub: String
        while (index < msg.length) {
            sub = if (msg.length <= index + maxLength) {
                msg.substring(index)
            } else {
                msg.substring(index, index + maxLength)
            }
            index += maxLength
            Log.d(tag, sub.trim { it <= ' ' })
        }
    }

    fun debugLongInfo(msg: String) {
        debugLongInfo(
            DEFAULT_TAG,
            msg
        )
    }

}