package com.yzy.module_base.http

import com.yzy.lib_common.network.BaseResponse

/**
 * 描述　:服务器返回数据的基类
 * 1.继承 BaseResponse
 * 2.重写isSucces 方法，编写你的业务需求，根据自己的条件判断数据是否请求成功
 * 3.重写 getResponseCode、getResponseData、getResponseMsg方法，传入你的 code data msg
 */
data class ApiResponse<T>(var errorCode: Int, var errorMsg: String, var data: T?,var traceId:String?) : BaseResponse<T>() {


    override fun isSucces() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg

    override fun getResponseTraceId()=traceId

}