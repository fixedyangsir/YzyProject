package com.yzy.lib_common.widget


object MultiStatePage {

    var config: MultiStateConfig = MultiStateConfig()


    fun config(config: MultiStateConfig): MultiStatePage {
        MultiStatePage.config = config
        return this
    }

}