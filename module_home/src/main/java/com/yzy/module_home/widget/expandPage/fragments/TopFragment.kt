package com.yzy.module_home.widget.expandPage.fragments

import android.os.Bundle
import android.view.View
import com.yzy.module_base.base.BaseFragment
import com.yzy.module_base.bean.Travel

import com.yzy.module_home.R
import com.yzy.module_home.databinding.FragmentTopBinding
import com.yzy.module_home.widget.expandPage.fragments.vm.TopVM

class TopFragment : BaseFragment<TopVM, FragmentTopBinding>() {

    var travel: Travel? = null

    companion object {
        const val ARG_TRAVEL = "ARG_TRAVEL"

        @JvmStatic
        fun newInstance(travel: Travel?): TopFragment {
            val args = Bundle()
            val topFragment = TopFragment()
            args.putParcelable(ARG_TRAVEL, travel)
            topFragment.arguments = args
            return topFragment
        }
    }


    override fun layoutId() = R.layout.fragment_top

    override fun initView(savedInstanceState: Bundle?) {
        val args = arguments
        if (args != null) {
            travel = args.getParcelable(ARG_TRAVEL)
            travel?.let {
                mDatabind.image.setImageResource(it.image)
                mDatabind.title.text = it.name
                mDatabind.tvFrontLeft.text = it.front_left
                mDatabind.tvFrontRight.text = it.front_right
            }
        }
    }

    override fun createObserver() {

    }

    override fun immersionBarEnabled(): Boolean {
        return false
    }

    override fun lazyLoadData() {

    }


    private fun startInfoActivity(view: View, travel: Travel) {
        val activity = activity
        /* ActivityCompat.startActivity(activity!!,
             ProjectAty.newInstance(activity, travel),
             ActivityOptionsCompat.makeSceneTransitionAnimation(
                 activity,
                 Pair(view, getString(R.string.transition_image)))
                 .toBundle())*/
    }

}
