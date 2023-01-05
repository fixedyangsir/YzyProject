package com.yzy.module_home.adpter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.yzy.module_base.bean.Travel
import com.yzy.module_home.widget.expandPage.ExpandAdapter
import com.yzy.module_home.widget.expandPage.fragments.TravelExpandFragment
import java.util.ArrayList

/**
 * Created by yzy on 2023/1/5.
 */
class TravelAdapter(fm: FragmentManager?) : ExpandAdapter(fm) {
    var travels: MutableList<Travel> = ArrayList()
    fun addAll(travels: List<Travel>?) {
        this.travels.addAll(travels!!)
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Fragment {
        val travel = travels[position]
        return TravelExpandFragment.newInstance(travel)
    }

    override fun getCount(): Int {
        return travels.size
    }

}