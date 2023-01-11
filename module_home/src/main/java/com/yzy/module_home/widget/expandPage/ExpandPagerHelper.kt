package com.yzy.module_home.widget.expandPage

import android.app.Activity
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.yzy.lib_common.ext.screenWidth
import com.yzy.module_home.widget.expandPage.fragments.ExpandFragment

/**
 * Created by yzy on 2023/1/5.
 */
object ExpandPagerHelper {
    fun getCurrentFragment(viewPager: ViewPager): ExpandFragment<*>? {
        if (viewPager.adapter is ExpandAdapter) {
            val adapter = viewPager.adapter as ExpandAdapter?
            val fragment = adapter!!.currentFragment
            if (fragment is ExpandFragment<*>) {
                return fragment
            }
        }
        return null
    }

    fun setupViewPager(viewPager: ViewPager) {
        val layoutParams = viewPager.layoutParams
        layoutParams.width =viewPager.context.screenWidth /7 * 5
        layoutParams.height = (layoutParams.width / 0.75).toInt()
        viewPager.offscreenPageLimit =3
        if (viewPager.parent is ViewGroup) {
            val viewParent = viewPager.parent as ViewGroup
            viewParent.clipChildren = false
            viewPager.clipChildren = false
        }
        viewPager.setPageTransformer(true,
            ExpandTransformer())
    }

    fun onBackPressed(viewPager: ViewPager): Boolean {
        val expandingFragment = getCurrentFragment(viewPager)
        if (expandingFragment != null && expandingFragment.isOpenend) {
            expandingFragment.close()
            return true
        }
        return false
    }
}