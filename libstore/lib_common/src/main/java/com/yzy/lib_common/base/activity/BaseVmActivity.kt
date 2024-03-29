package com.yzy.lib_common.base.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.launcher.ARouter
import com.yzy.lib_common.R
import com.yzy.lib_common.network.manager.NetState
import com.yzy.lib_common.network.manager.NetworkStateManager
import com.yzy.lib_common.util.SoftInputUtils
import com.yzy.lib_common.widget.dialog.LoadingDialog
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CustomAdapt


abstract class BaseVmActivity : AppCompatActivity() {

    /**
     * 是否需要使用DataBinding 供子类BaseVmDbActivity修改，用户请慎动
     */
    private var isUserDb = false




    private val loadingDialog: LoadingDialog by lazy {
        createLoadingDialog()
    }


    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)


    fun showLoading(message: String = getString(R.string.request_network)) {
        loadingDialog.setTitle(message)?.show()
    }


    fun dismissLoading() {
        loadingDialog.dismiss()
    }

    abstract fun createLoadingDialog(): LoadingDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        if (!isUserDb) {
            setContentView(layoutId())
        } else {
            initDataBind()
        }
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {

        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }



    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}


    /**
     * 创建LiveData数据观察者
     */
    abstract fun createObserver()


    fun userDataBinding(isUserDb: Boolean) {
        this.isUserDb = isUserDb
    }

    /**
     * 供子类BaseVmDbActivity 初始化Databinding操作
     */
    open fun initDataBind() {}





    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val v = currentFocus
                if (SoftInputUtils.isShouldHideInput(v, ev)) {
                    SoftInputUtils.hideSoftInput(this, v?.windowToken)
                }
                return super.dispatchTouchEvent(ev)
            }
            if (window.superDispatchTouchEvent(ev)) {
                return true
            }
            return super.dispatchTouchEvent(ev)


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.dispatchTouchEvent(ev)
    }



    override fun getResources(): Resources {
        runOnUiThread {
            if (this is CustomAdapt) {
                AutoSizeCompat.autoConvertDensityOfCustomAdapt(super.getResources(), this)
            } else {
                AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()))//如果没有自定义需求用这个方法
            }

            //     AutoSizeCompat.autoConvertDensity(super.getResources(), 1110f, false)//如果有自定义需求就用这个方法
        }

        return super.getResources();
    }
}