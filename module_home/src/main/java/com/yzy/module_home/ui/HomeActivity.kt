package com.yzy.module_home.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ARouterUtils
import com.yzy.module_home.R
import com.yzy.module_home.adpter.TravelViewPagerAdapter
import com.yzy.module_home.bean.Travel
import com.yzy.module_home.databinding.ActivityHomeBinding
import com.yzy.module_home.ui.vm.HomeVM
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper.getCurrentFragment
import com.yzy.module_home.widget.expandPage.ExpandPagerHelper.setupViewPager
import com.yzy.module_home.widget.expandPage.fragments.ExpandFragment

@Route(path = ARouterUtils.PATH_HOME)
class HomeActivity : BaseActivity<HomeVM, ActivityHomeBinding>(),
    ExpandFragment.OnExpandingClickListener {


    val MODULE_1 = "项目经验"
    val MODULE_2 = "开发库"
    val MODULE_3 = "工具库"


    override fun layoutId() = R.layout.activity_home

    override fun initView(savedInstanceState: Bundle?) {
        initViewPage()
    }

    private fun initViewPage() {
        val adapter = TravelViewPagerAdapter(supportFragmentManager)
        adapter.addAll(generateTravelList())
        mDatabind.viewPager.setAdapter(adapter)
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

    private fun generateTravelList(): List<Travel> {
        val travels: MutableList<Travel> = ArrayList()
        for (i in 0..4) {
            //  String name, String front_left, String front_right, String bottom_content, String bottom_type, int bottom_img, int image
            travels.add(Travel(MODULE_1,
                "by 杨镇瑜",
                "2017-3-16",
                "项目经验：包含开发的项目的展示，以及一些经验的概述",
                "MY  PROJECT",
                R.mipmap.icon_like,
                R.mipmap.icon_page_03))
            travels.add(Travel(MODULE_2,
                "by 杨镇瑜",
                "update...",
                "开发库：展示开发过程中所用到的三方类库、三方sdk等",
                "DEVELOPMENT LIBRARY",
                R.mipmap.icon_star,
                R.mipmap.icon_page_02))
            travels.add(Travel(MODULE_3,
                "by 杨镇瑜",
                "Loading...",
                "工具库：一些工具类的集合，包含常用工具的封装，硬件交互和各种自定义View的展示",
                "TOOLS LIBRARY",
                R.mipmap.icon_gongju,
                R.mipmap.icon_page_01))
        }
        return travels
    }

    override fun createObserver() {

    }

    override fun onExpandingClick(v: View) {
        val view: View = v.findViewById(R.id.image)
        val travel = generateTravelList()[mDatabind.viewPager.currentItem]

        var intent: Intent? = null
        when (travel.name) {
            MODULE_1 -> intent = DynamicActivity.newInstance(this, travel)
            MODULE_2 -> intent = DynamicActivity.newInstance(this, travel)
            MODULE_3 -> intent = DynamicActivity.newInstance(this, travel)
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