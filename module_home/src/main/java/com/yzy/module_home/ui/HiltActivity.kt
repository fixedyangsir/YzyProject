package com.yzy.module_home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.yzy.lib_common.util.RunUtils
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.bean.Travel
import com.yzy.module_base.module.ColiLoadAnnotation
import com.yzy.module_base.module.GlideLoadAnnotation
import com.yzy.module_base.module.LoadImageInterface
import com.yzy.module_home.R
import com.yzy.module_home.databinding.ActivityHiltBinding
import com.yzy.module_home.ui.vm.HiltVM
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton


/**
 * 模拟不同图片框架加载图片
 */
@AndroidEntryPoint
class HiltActivity : BaseActivity<HiltVM, ActivityHiltBinding>() {

    @GlideLoadAnnotation
    @Inject
    lateinit var glideLoad: LoadImageInterface

    @ColiLoadAnnotation
    @Inject
    lateinit var coliLoad: LoadImageInterface

    val loadImageRunnable by lazy {
        Runnable {
            mViewModel.glideResult.set(glideLoad.loadImage("test"))
            mViewModel.coliResult.set(coliLoad.loadImage("test"))
        }
    }


    companion object {
        const val EXTRA_TRAVEL = "EXTRA_TRAVEL"

        @JvmStatic
        fun newInstance(context: Context, travel: Travel?): Intent {
            val intent = Intent(context, HiltActivity::class.java)
            intent.putExtra(EXTRA_TRAVEL, travel)
            return intent
        }
    }

    override fun layoutId() = R.layout.activity_hilt

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        val travel: Travel? = intent.getParcelableExtra(EXTRA_TRAVEL)
        if (travel != null) {
            mDatabind.image.setImageResource(travel.image)
            mDatabind.ctl.setCollapsedTitleTextColor(resources.getColor(R.color.white))
            mDatabind.ctl.setExpandedTitleColor(resources.getColor(R.color.teal_700))
            mDatabind.ctl.title = "依赖注入"
        }
        mDatabind.toolbar.setNavigationOnClickListener { onBackPressed() }


    }

    override fun createObserver() {
        RunUtils.runOnUIThreadDelayed(loadImageRunnable, 1500)
    }

    override fun onDestroy() {
        super.onDestroy()
        RunUtils.removeRunnable(loadImageRunnable)
    }

}