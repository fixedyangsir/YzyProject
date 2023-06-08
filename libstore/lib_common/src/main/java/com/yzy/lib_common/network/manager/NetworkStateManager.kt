package com.yzy.lib_common.network.manager

import androidx.lifecycle.MutableLiveData


class NetworkStateManager private constructor() {

    val mNetworkStateCallback = MutableLiveData<NetState>()

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }

}