package com.yzy.module_login.http

import com.yzy.lib_common.network.BaseRepository
import com.yzy.lib_common.network.BaseResponse
import com.yzy.module_base.bean.UserInfo

/**
 * Created by yzy on 2023/1/11.
 */
class LoginRepository : BaseRepository() {

    suspend fun requestLogin(name: String, pwd: String): BaseResponse<UserInfo> {
        return executeRequest { LOGIN_SERVICE.login(name, pwd) }
    }


}