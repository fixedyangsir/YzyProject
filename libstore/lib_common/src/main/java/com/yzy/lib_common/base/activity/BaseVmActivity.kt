package com.yzy.lib_common.base.activity

import android.content.res.Resources
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yzy.lib_common.R
import com.yzy.lib_common.base.viewmodel.BaseViewModel
import com.yzy.lib_common.ext.getVmClazz
import com.yzy.lib_common.network.manager.NetState
import com.yzy.lib_common.network.manager.NetworkStateManager
import com.yzy.lib_common.util.SoftInputUtils
import com.yzy.lib_common.widget.dialog.LoadingDialog
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CustomAdapt


abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    /**
     * 是否需要使用DataBinding 供子类BaseVmDbActivity修改，用户请慎动
     */
    private var isUserDb = false

    lateinit var mViewModel: VM


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
        if (checkNext()) {
            if (!isUserDb) {
                setContentView(layoutId())
            } else {
                initDataBind()
            }
            init(savedInstanceState)
        }
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    open fun checkNext(): Boolean {
        return true
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}


    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

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

    /**
     *
     *
     */

    abstract fun showToast(msg: String)


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

    fun getThis(): BaseVmActivity<*> {
        return this
    }

    override fun getResources(): Resources {
        runOnUiThread {
            if (this is CustomAdapt){
                AutoSizeCompat.autoConvertDensityOfCustomAdapt(super.getResources(),this)
            }else{
                AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()))//如果没有自定义需求用这个方法
            }

            //     AutoSizeCompat.autoConvertDensity(super.getResources(), 1110f, false)//如果有自定义需求就用这个方法
        }

        return super.getResources();
    }
}