package com.yzy.module_home.widget.expandPage.fragments

import android.os.Bundle
import android.view.View
import com.yzy.module_base.base.BaseFragment
import com.yzy.module_base.bean.Travel
import com.yzy.module_home.R
import com.yzy.module_home.databinding.FragmentBottomBinding
import com.yzy.module_home.widget.expandPage.fragments.vm.BottomVM

class BottomFragment : BaseFragment<BottomVM, FragmentBottomBinding>() {


    var travel: Travel? = null

    companion object {
        const val ARG_TRAVEL = "ARG_TRAVEL"
        @JvmStatic
        fun newInstance(travel: Travel?): BottomFragment {
            val args = Bundle()
            val fragmentBottom = BottomFragment()
            args.putParcelable(ARG_TRAVEL, travel)
            fragmentBottom.arguments = args
            return fragmentBottom
        }
    }


    override fun layoutId() = R.layout.fragment_bottom

    override fun initView(savedInstanceState: Bundle?) {
        val args = arguments
        if (args != null) {
            travel = args.getParcelable(ARG_TRAVEL)
            travel?.let {
                mDatabind.ivType.setImageResource(it.bottom_img)
                mDatabind.tvBottomContent.text = it.bottom_content
                mDatabind.tvType.text = it.bottom_type
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