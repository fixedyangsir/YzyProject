package com.yzy.lib_common.network

import android.app.Application
import android.net.ParseException
import com.yzy.lib_common.network.exception.SpecialTimeoutThrowable
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.network.exception.HttpNullThrowable
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


/**
 * 描述　: 根据异常返回相关的错误信息工具类
 */
object ExceptionHandle {
    val appContext: Application by lazy { Ktx.app }
    var time=0L

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)

                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(Error.PARSE_ERROR,e)

                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)

                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR,e)


          //           netSendBroadcast(Error.SSL_ERROR.getKey(),retryCount)

                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)

                    return ex
                }
                is SpecialTimeoutThrowable ->{
                    ex = AppException(Error.SPECIAL_TIMEOUT_ERROR,e)

                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)

                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)

                    return ex
                }
                is HttpNullThrowable ->{
                    ex = AppException(Error.NETWORK_ERROR,e)

                    return ex
                }

                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN,e)
        return ex
    }


}