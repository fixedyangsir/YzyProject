package com.yzy.module_base.utils

import android.text.TextUtils
import com.yzy.lib_common.util.CacheUtils
import com.yzy.module_base.bean.UserInfo
import com.yzy.mvvmlib.util.jsonToBean
import com.yzy.mvvmlib.util.toJson


/**
 * 缓存
 */
object AppCache {
    //登录token
    private const val TOKEN_KEY = "token"
    private const val USER_INFO = "userInfo"

    fun getUserInfo(): UserInfo? {
        return CacheUtils.getString(USER_INFO, "{}").jsonToBean()
    }

    fun saveUserInfo(userInfo: UserInfo) {
        CacheUtils.saveValue(USER_INFO, userInfo.toJson())
    }

    /**
     * 获取跟用户相关的key
     * 需要登录
     */
    fun getSingleKey(key: String): String {
        getUserInfo()?.id?.let {
            return key + "_" + it
        }
        return key
    }


    fun getToken(): String {
        return CacheUtils.getString(TOKEN_KEY, "")
    }

    fun saveToken(token: String) {
        CacheUtils.saveValue(TOKEN_KEY, token)
    }

    /**
     * 判断是否登录
     */
    fun isLogin(): Boolean {
        return getUserInfo() != null && !TextUtils.isEmpty(getUserInfo()?.username)
    }

    fun removeUserInfo() {
        CacheUtils.remove(USER_INFO)
        CacheUtils.remove(TOKEN_KEY)
    }


}