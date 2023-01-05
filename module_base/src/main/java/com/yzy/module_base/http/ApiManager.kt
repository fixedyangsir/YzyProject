package com.yzy.module_base.http

import com.yzy.lib_common.network.api.DefaultNetworkApi
import com.yzy.lib_common.util.HttpsUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient


class ApiManager : DefaultNetworkApi() {

    companion object {
        val INSTANCE: ApiManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiManager()
        }

        //服务器地址
        const val SERVER_URL = "https://www.wanandroid.com"

    }

    override fun getInterceptor(): List<Interceptor> {
        return listOf(RetryInterceptor(3), DefaultHeadInterceptor())
    }

    override fun setSLL(builder: OkHttpClient.Builder) {

        //忽略https验证
        builder.hostnameVerifier(
            HttpsUtils.getTestHostnameVerifier()
        )
        builder.sslSocketFactory(
            HttpsUtils.getTestSSLSocketFactory(),
            HttpsUtils.chooseTrustManager(HttpsUtils.getTestTrustManager())
        )

    }

}




