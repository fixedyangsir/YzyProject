package com.yzy.lib_common.network.exception

import java.net.SocketTimeoutException

class SpecialTimeoutThrowable(message: String?) : SocketTimeoutException(message) {
}