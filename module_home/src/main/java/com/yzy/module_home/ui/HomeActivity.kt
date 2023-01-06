package com.yzy.module_home.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.service.IDataService
import com.yzy.module_base.utils.ARouterUtils
import com.yzy.module_home.R
import com.yzy.module_home.adpter.TravelAdapter
import com.yzy.module_home.databinding.ActivityHomeBinding
import com.yzy.module_home.ui.vm.HomeVM
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper.getCurrentFragment
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper.setupViewPager
import com.yzy.module_home.widget.expandPage.fragments.ExpandFragment

/**
 * 主界面 组件化 数据共享
 */
@Route(path = ARouterUtils.PATH_HOME)
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>(),
    ExpandFragment.OnExpandingClickListener {


    @Autowired
    lateinit var service: IDataService

    val adapter by lazy {
        TravelAdapter(supportFragmentManager)
    }


    override fun layoutId() = R.layout.activity_home

    override fun initView(savedInstanceState: Bundle?) {
        initViewPage()
    }

    private fun initViewPage() {
        adapter.addAll(service.getHomeData())
        mDatabind.viewPager.adapter = adapter
        setupViewPager(mDatabind.viewPager)
        mDatabind.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {
                val expandingFragment = getCurrentFragment(mDatabind.viewPager)
                if (expandingFragment != null && expandingFragment.isOpenend) {
                    expandingFragment.close()
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }


    override fun createObserver() {

    }

    override fun onExpandingClick(v: View) {
        val view: View = v.findViewById(R.id.image)


        var intent: Intent? = null
        when (val index = mDatabind.viewPager.currentItem) {
            0 -> intent = DynamicActivity.newInstance(this, adapter.travels[index])
            1 -> intent = HiltActivity.newInstance(this, adapter.travels[index])
        }
        startInfoActivity(view, intent!!)
    }

    private fun startInfoActivity(view: View, intent: Intent) {
        val activity: Activity = this
        ActivityCompat.startActivity(activity,
            intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                androidx.core.util.Pair(view, getString(R.string.transition_image)))
                .toBundle())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupWindowAnimations()
    }

    private fun setupWindowAnimations() {
        val slideTransition = Explode()
        window.reenterTransition = slideTransition
        window.exitTransition = slideTransition
    }

    override fun onBackPressed() {
        if (!ExpandPagerHelper.onBackPressed(mDatabind.viewPager)) {
            super.onBackPressed();
        }
    }

}