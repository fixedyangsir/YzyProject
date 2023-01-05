package com.yzy.lib_common.network



/**
 * 描述　: 错误枚举类
 */
enum class Error(private val code: Int) {

    /**
     * 未知错误
     */
    UNKNOWN(1000),

    /**
     * 解析错误
     */
    PARSE_ERROR(1001),

    /**
     * 网络错误
     */
    NETWORK_ERROR(1002),

    /**
     * 证书出错
     */
    SSL_ERROR(1004),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006),

    /**
     * 特殊超时异常
     */
    SPECIAL_TIMEOUT_ERROR(1007),
    /**
     * 网络空异常
     */
    HTTP_NULL_ERROR(1008);


    fun getKey(): Int {
        return code
    }

}