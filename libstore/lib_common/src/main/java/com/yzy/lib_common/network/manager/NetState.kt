package com.yzy.lib_common.network.manager


class NetState(
    var isSuccess: Boolean = true,
    var tips:String="",
    var type:Int= NET_TYPE_DEFAULT
)
const val NET_TYPE_DEFAULT=0
const val NET_TYPE=1