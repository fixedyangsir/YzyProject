package com.yzy.module_login.http

import com.yzy.module_base.bean.UserInfo
import com.yzy.module_base.http.ApiResponse
import com.yzy.module_base.http.ApiManager
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 登录模块
 */
interface LoginService {
    /**
     * 登录
     */
    @POST("user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): ApiResponse<UserInfo>


}


val LOGIN_SERVICE: LoginService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    ApiManager.INSTANCE.getApi(LoginService::class.java, ApiManager.SERVER_URL)
}