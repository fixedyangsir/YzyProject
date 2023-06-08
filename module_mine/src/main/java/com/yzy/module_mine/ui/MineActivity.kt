package com.yzy.module_mine.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.yzy.lib_common.base.mvi.LoadingEvent
import com.yzy.lib_common.base.mvi.ToastEvent
import com.yzy.lib_common.base.mvi.extension.collectSingleEvent
import com.yzy.lib_common.base.mvi.extension.collectState
import com.yzy.module_base.base.BaseActivity
import com.yzy.module_base.utils.ToastUtils.showToast

import com.yzy.module_mine.R
import com.yzy.module_mine.databinding.ActivityMineBinding
import com.yzy.module_mine.ui.vm.MineVM

class MineActivity : BaseActivity<ActivityMineBinding>() {
    private val viewModel: MineVM by viewModels()

    override fun layoutId() = R.layout.activity_mine

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