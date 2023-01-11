package com.yzy.module_base.event

import androidx.lifecycle.ViewModel
import com.yzy.lib_common.base.viewmodel.BaseViewModel
import github.leavesczy.eventlivedata.EventLiveData

/**
 * 描述　:APP全局的Viewmodel，可以在这里发送全局通知替代Eventbus，LiveDataBus等
 */
class EventViewModel : ViewModel() {


    //日志导出
    val logcatEvent = EventLiveData<Boolean>()



}
