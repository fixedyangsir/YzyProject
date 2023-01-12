package com.yzy.module_home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gyf.immersionbar.ktx.immersionBar
import com.yzy.lib_common.util.Utils
import com.yzy.lib_protocal.bean.ViewNode
import com.yzy.lib_protocal.utils.ViewParser
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.bean.Travel

import com.yzy.module_home.R
import com.yzy.module_home.databinding.ActivityYzyViewBinding
import com.yzy.mvvmlib.util.jsonToBean

class YzyViewActivity : BaseActivity<ActivityYzyViewBinding>() {

    companion object {
        const val EXTRA_TRAVEL = "EXTRA_TRAVEL"

        @JvmStatic
        fun newInstance(context: Context, travel: Travel?): Intent {
            val intent = Intent(context, YzyViewActivity::class.java)
            intent.putExtra(EXTRA_TRAVEL, travel)
            return intent
        }
    }

    override fun initImmersionBar() {
        // super.initImmersionBar()
        immersionBar {
            //  statusBarColor(R.color.colorPrimary)
            navigationBarColor(R.color.white)
            statusBarDarkFont(false)
            navigationBarDarkIcon(true)
        }
    }


    override fun layoutId() = R.layout.activity_yzy_view

    override fun initView(savedInstanceState: Bundle?) {

        val travel: Travel? = intent.getParcelableExtra(HiltActivity.EXTRA_TRAVEL)
        if (travel != null) {
            mDatabind.image.setImageResource(travel.image)
            mDatabind.ctl.setCollapsedTitleTextColor(resources.getColor(R.color.white))
            mDatabind.ctl.setExpandedTitleColor(resources.getColor(R.color.teal_700))
            mDatabind.ctl.title = "动态布局"
        }
        mDatabind.toolbar.setNavigationOnClickListener { onBackPressed() }


    }

    override fun createObserver() {

        val pageData = Utils.getJSONDataFromAsset(this, "YzyViewPage.json")?.jsonToBean<ViewNode>()

        pageData?.let {
            //解析view节点
            val view = ViewParser.adapt(it, this)
            view?.let {
                mDatabind.llContent.addView(view.getView(),
                    ViewParser.adaptLayoutParams(pageData))
            }


        }


    }

}