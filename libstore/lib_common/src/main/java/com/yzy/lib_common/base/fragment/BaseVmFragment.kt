package com.yzy.lib_common.base.fragment

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.gyf.immersionbar.components.SimpleImmersionOwner
import com.yzy.lib_common.R
import com.yzy.lib_common.network.manager.NetState
import com.yzy.lib_common.network.manager.NetworkStateManager
import com.yzy.lib_common.widget.dialog.LoadingDialog


/**
 * 描述　: ViewModelFragment基类，自动把ViewModel注入Fragment
 */
abstract class BaseVmFragment : Fragment(),
    SimpleImmersionOwner {

    //Whether to load for the first time
    var isFirst: Boolean = true



    lateinit var mActivity: AppCompatActivity

    private val mSimpleImmersionProxy: QuickImmersionProxy by lazy {
        QuickImmersionProxy(this)
    }


    val loadingDialog: LoadingDialog? by lazy {
        createLoadingDialog()
    }


    abstract fun createLoadingDialog(): LoadingDialog

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFirst = true


        initView(savedInstanceState)
        createObserver()
        onVisible()
        initData()
    }



    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}



    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载
     */
    abstract fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        mSimpleImmersionProxy.isUserVisibleHint = true
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()

            //在Fragment中，只有懒加载过了才能开启网络变化监听
            NetworkStateManager.instance.mNetworkStateCallback.observe(
                viewLifecycleOwner,
                Observer {
                    //不是首次订阅时调用方法，防止数据第一次监听错误
                    if (!isFirst) {
                        onNetworkStateChanged(it)
                    }
                })
            isFirst = false
        }
    }

    /**
     * Fragment执行onCreate后触发的方法
     */
    open fun initData() {}


    fun showLoading(message: String = getString(R.string.request_network)) {
        if (isResumed){
            loadingDialog?.setTitle(message)?.show()
        }

    }

    fun dismissLoading() {
        loadingDialog?.dismiss()
    }


    abstract fun showToast(msg: String)




    override fun onDestroy() {
        super.onDestroy()
        mSimpleImmersionProxy.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mSimpleImmersionProxy.onConfigurationChanged(newConfig)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSimpleImmersionProxy.onHiddenChanged(hidden)
    }


}

