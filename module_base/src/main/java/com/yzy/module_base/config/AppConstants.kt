package com.yzy.module_base.config

import com.yzy.module_base.BuildConfig

/**
 * 常量配置
 */
object AppConstants {
    // 基础url
    val url=if(BuildConfig.DEBUG) "https://www.wanandroid.com" else "https://www.wanandroid.com"

}