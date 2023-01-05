package com.yzy.module_base.http


import com.yzy.module_base.utils.AppCache
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * 自定义头部参数拦截器，传入heads
 */
class DefaultHeadInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder: Request.Builder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("User-Agent", "Android-XT-Water-Phone")
        val token: String = AppCache.getToken()
        if (token.isNotEmpty()) {
            builder.addHeader("token", token)
        }
        builder.addHeader("timestamp", System.currentTimeMillis().toString())
        builder.addHeader("platform", "android")



        return chain.proceed(builder.build())
    }

}