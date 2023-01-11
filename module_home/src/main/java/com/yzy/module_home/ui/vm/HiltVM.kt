package com.yzy.module_home.ui.vm

import androidx.lifecycle.ViewModel
import com.yzy.lib_common.base.databind.StringObservableField
import com.yzy.lib_common.base.viewmodel.BaseViewModel
import com.yzy.module_home.R

class HiltVM : ViewModel() {

    val glideResult = StringObservableField("Loading...")
    val coliResult = StringObservableField("Loading...")


}