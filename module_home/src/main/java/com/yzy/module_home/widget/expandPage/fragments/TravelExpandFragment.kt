package com.yzy.module_home.widget.expandPage.fragments

import android.os.Bundle
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.yzy.module_base.bean.Travel
import com.yzy.module_home.databinding.FragmentExpandingBinding
import com.yzy.module_home.widget.expandPage.fragments.vm.ExpandVM


class TravelExpandFragment : ExpandFragment< FragmentExpandingBinding>() {
    var travel: Travel? = null
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        if (args != null) {
            travel = args.getParcelable(ARG_TRAVEL)
        }
    }

    /**
     * include TopFragment
     * @return
     */
    override fun getFragmentTop(): Fragment {
        return TopFragment.newInstance(travel)
    }

    /**
     * include BottomFragment
     * @return
     */
    override fun getFragmentBottom(): Fragment {
        return BottomFragment.newInstance(travel)
    }

    companion object {
        const val ARG_TRAVEL = "ARG_TRAVEL"
        @JvmStatic
        fun newInstance(travel: Travel?): TravelExpandFragment {
            val fragment = TravelExpandFragment()
            val args = Bundle()
            args.putParcelable(ARG_TRAVEL, travel)
            fragment.arguments = args
            return fragment
        }


    }
}