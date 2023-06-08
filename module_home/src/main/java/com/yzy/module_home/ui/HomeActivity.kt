package com.yzy.module_home.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.yzy.lib_common.base.mvi.LoadingEvent
import com.yzy.lib_common.base.mvi.ToastEvent
import com.yzy.lib_common.base.mvi.extension.collectSingleEvent
import com.yzy.lib_common.base.mvi.extension.collectState
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ToastUtils.showToast

import com.yzy.module_home.R
import com.yzy.module_home.databinding.ActivityHomeBinding
import com.yzy.module_home.ui.vm.HomeVM

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private val viewModel: HomeVM by viewModels()

    override fun layoutId() = R.layout.activity_home

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {
        viewModel.container.uiStateFlow.collectState(this) {

        }

        viewModel.container.singleEventFlow.collectSingleEvent(this) {
            when (it) {
                is ToastEvent -> {
                    showToast(it.message)
                }

                is LoadingEvent -> {
                    if (it.isShow) showLoading() else dismissLoading()
                }
            }
        }
    }

}