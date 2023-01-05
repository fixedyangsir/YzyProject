package com.yzy.lib_common.network.interceptor

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response


class RewriteCacheControlInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val response = chain.proceed(request)
        val serverCache = response.header("Cache-Control")

        // 如果服务端设置相应的缓存策略那么遵从服务端的不做修改
        if (TextUtils.isEmpty(serverCache)) {
            val cacheControl = request.cacheControl.toString();
            if (TextUtils.isEmpty(cacheControl)) {
              /*  // 如果请求接口中未设置cacheControl，则统一设置为一分钟
                val maxAge = 1 * 60 // 在线缓存在1分钟内可读取 单位:秒
                return response.newBuilder()
                    .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .build();*/
            } else {
                return response.newBuilder()
                    .addHeader("Cache-Control", cacheControl)
                    .removeHeader("Pragma")
                    .build();
            }

        }
        return response
    }
}