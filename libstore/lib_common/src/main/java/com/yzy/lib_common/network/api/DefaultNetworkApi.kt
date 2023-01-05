package com.yzy.lib_common.network.api


import com.google.gson.GsonBuilder
import com.yzy.lib_common.BuildConfig
import com.yzy.lib_common.base.appContext
import com.yzy.lib_common.network.CoroutineCallAdapterFactory
import com.yzy.lib_common.network.interceptor.CacheInterceptor
import com.yzy.lib_common.network.interceptor.RewriteCacheControlInterceptor
import com.yzy.lib_common.network.interceptor.logging.LogInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


/**
 *默认的BasenetworkApi实现
 * 描述　: 网络请求构建器，继承BasenetworkApi 并实现setHttpClientBuilder/setRetrofitBuilder方法，
 * 在这里可以添加拦截器，设置构造器可以对Builder做任意操作
 */
abstract class DefaultNetworkApi : BaseNetworkApi() {

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //设置缓存配置 缓存最大10M
            cache(Cache(File(appContext.cacheDir, "cxk_cache"), (10 * 1024 * 1024).toLong()))


            //添加Cookies自动持久化
            //  cookieJar(cookieJar)


            if (getInterceptor() != null) {
                getInterceptor()?.forEach {
                    addInterceptor(it)
                }
            }
            setSLL(this)
            //添加缓存拦截器 可传入缓存天数，不传默认7天
            addInterceptor(CacheInterceptor(7))
            addNetworkInterceptor(RewriteCacheControlInterceptor())

            // 日志拦截器
            if (BuildConfig.DEBUG) {
                addInterceptor(LogInterceptor())
            }
            //超时时间 连接、读、写
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(15, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)



        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

    /*  val cookieJar: PersistentCookieJar by lazy {
          PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(appContext))
      }*/

    /**
     * 用于添加一些默认的Interceptor 比如 header参数
     */
    abstract fun getInterceptor(): List<Interceptor>?

}



