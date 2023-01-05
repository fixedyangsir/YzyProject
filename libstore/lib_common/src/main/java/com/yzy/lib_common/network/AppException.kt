package com.yzy.lib_common.network

import com.yzy.lib_common.R
import com.yzy.lib_common.ext.getResString

/**
 * 描述　:自定义错误信息异常
 */
class AppException : Exception {

   private var errorMsg: String //错误消息
    var errCode: Int = 0 //错误码
    var errorLog: String //错误日志

    constructor(errCode: Int, error: String?) : super(error) {
        this.errorMsg = getErrorValue(errCode)
        this.errCode = errCode
        this.errorLog = error?:this.errorMsg
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = getErrorValue(error.getKey())
        errorLog =errorMsg
    }

    private fun getErrorValue(code:Int):String{
        when (code){
            Error.UNKNOWN.getKey()->{
             return  R.string.str_unknown_error.getResString()
            }

            Error.PARSE_ERROR.getKey()->{
                return   R.string.str_parse_error.getResString()
            }

            Error.NETWORK_ERROR.getKey()->{
                return   R.string.str_network_error.getResString()
            }

            Error.SSL_ERROR.getKey()->{
                return  R.string.str_ssl_error.getResString()
            }

            Error.TIMEOUT_ERROR.getKey()->{
                return    R.string.str_timeout_error.getResString()
            }
            Error.SPECIAL_TIMEOUT_ERROR.getKey()->{
                return   R.string.str_network_error.getResString()
            }
            Error.HTTP_NULL_ERROR.getKey()->{
                return  R.string.str_network_error.getResString()
            }
            else->{
                return  R.string.str_unknown_error.getResString()
            }
        }
    }

}