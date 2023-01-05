package com.yzy.module_home.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_home.R
import com.yzy.module_home.bean.Travel
import com.yzy.module_home.databinding.ActivityDynamicBinding
import com.yzy.module_home.ui.vm.DynamicVM


class DynamicActivity : BaseActivity<DynamicVM, ActivityDynamicBinding>() {


    companion object{
         const val EXTRA_TRAVEL = "EXTRA_TRAVEL"
        fun newInstance(context: Context, travel: Travel?): Intent {
            val intent = Intent(context, DynamicActivity::class.java)
            intent.putExtra(EXTRA_TRAVEL, travel)
            return intent
        }
    }
    override fun layoutId() = R.layout.activity_dynamic

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {

    }

}