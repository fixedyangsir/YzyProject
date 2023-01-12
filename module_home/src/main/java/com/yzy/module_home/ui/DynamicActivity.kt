package com.yzy.module_home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.tmall.wireless.vaf.framework.VafContext
import com.tmall.wireless.vaf.virtualview.core.IContainer
import com.tmall.wireless.vaf.virtualview.event.EventManager
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.util.Utils
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.bean.Travel
import com.yzy.module_home.R
import com.yzy.module_home.databinding.ActivityDynamicBinding
import org.json.JSONObject


class DynamicActivity : BaseActivity<ActivityDynamicBinding>() {


    companion object {
        const val EXTRA_TRAVEL = "EXTRA_TRAVEL"

        @JvmStatic
        fun newInstance(context: Context, travel: Travel?): Intent {
            val intent = Intent(context, DynamicActivity::class.java)
            intent.putExtra(EXTRA_TRAVEL, travel)
            return intent
        }
    }

    override fun layoutId() = R.layout.activity_dynamic

    override fun initImmersionBar() {
        // super.initImmersionBar()
        immersionBar {
            //  statusBarColor(R.color.colorPrimary)
            navigationBarColor(R.color.white)
            statusBarDarkFont(false)
            navigationBarDarkIcon(true)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initVirtualView()

        val travel: Travel? = intent.getParcelableExtra(HiltActivity.EXTRA_TRAVEL)
        if (travel != null) {
            mDatabind.image.setImageResource(travel.image)
            mDatabind.ctl.setCollapsedTitleTextColor(resources.getColor(R.color.white))
            mDatabind.ctl.setExpandedTitleColor(resources.getColor(R.color.teal_700))
            mDatabind.ctl.title = getString(R.string.str_virtualview)
        }
        mDatabind.toolbar.setNavigationOnClickListener { onBackPressed() }

    }

    override fun onBackPressed() {
        mDatabind.llContent.removeAllViews()
        super.onBackPressed()

    }

    private fun initVirtualView() {
        val vafContext = VafContext(Ktx.app)

        val viewManager = vafContext.viewManager
        viewManager.init(Ktx.app)

        //加载布局模板编译成的二进制文件
        viewManager.loadBinBufferSync(Utils.getAssetsFile(this, "HelloWorld.out"))

        //注册点击事件
        vafContext.eventManager.register(EventManager.TYPE_Click
        ) { data ->
            showToast(data.mVB.action)
            true
        }
        val container: View = vafContext.containerService.getContainer("HelloWorld", true)
        mDatabind.llContent.addView(container)

        //模拟填充数据
        val jsonObject= JSONObject(Utils.getJSONDataFromAsset(this, "HelloWorld.json"))
        if (jsonObject != null) {
            (container as IContainer).virtualView.setVData(jsonObject)
        }


    }


    override fun createObserver() {

    }

}