package com.yzy.module_home.widget.expandPage

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import java.lang.ref.WeakReference

abstract class ExpandAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(
    fm!!) {
    var currentFragmentReference: WeakReference<Fragment>? = null
    val currentFragment: Fragment?
        get() = if (currentFragmentReference != null) {
            currentFragmentReference!!.get()
        } else null

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        if (currentFragment !== `object` && `object` is Fragment) {
            currentFragmentReference = WeakReference(`object`)
        }
        super.setPrimaryItem(container, position, `object`)
    }
}