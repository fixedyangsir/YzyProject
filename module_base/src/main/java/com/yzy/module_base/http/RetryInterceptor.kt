package com.yzy.module_base.http

import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.util.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by yzy on 2020/12/29.
 */
/**
 * 重试拦截器
 */
class RetryInterceptor(
//最大重试次数
    var maxRetry: Int,
) : Interceptor {
    private var retryNum = 0

    init {
        retryNum = 0
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response = chain.proceed(request)

        while (NetworkUtil.isNetworkAvailable(Ktx.app)&&!response.isSuccessful && retryNum < maxRetry - 1) {
            retryNum++
            response?.close()
            response = chain.proceed(request)
        }
        retryNum = 0
        return response
    }

}
