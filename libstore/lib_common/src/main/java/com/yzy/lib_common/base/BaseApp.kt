package com.yzy.lib_common.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.hjq.toast.ToastUtils
import com.tencent.mmkv.MMKV
import com.yzy.lib_common.BuildConfig

import com.yzy.lib_common.ext.getAppProcessName
import com.yzy.lib_common.util.LogUtil
import com.yzy.lib_common.util.MyToastStrategy
import me.jessyan.autosize.AutoSizeConfig


abstract class BaseApp : Application(), ViewModelStoreOwner {
    companion object {
        lateinit var baseApp: BaseApp
    }

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        baseApp =this
        //防止多进程重复初始化
        if (getAppProcessName() == getAppId()) {
            initARouter()
            initAutoSize()
            mAppViewModelStore = ViewModelStore()
            LogUtil.init()
            MMKV.initialize(this)
            initToast()
            initByAppProcess()

        }


    }

    private fun initToast() {
        val myToastStrategy = MyToastStrategy()
        ToastUtils.init(this)
        ToastUtils.setStrategy(myToastStrategy)
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }


    /**
     * 主进程
     */
    abstract fun initByAppProcess()

    private fun initAutoSize() {
        AutoSizeConfig.getInstance().setBaseOnWidth(false).setExcludeFontScale(true)
            .setUseDeviceSize(true)//设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏高度
            .setLog(BuildConfig.DEBUG)
    }


    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }

    /**
     * 获取应用包名 用于判断 多进程初始化问题
     */
    abstract fun getAppId(): String
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}