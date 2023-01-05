package com.yzy.lib_common.network.exception

import java.io.InterruptedIOException

class HttpNullThrowable(message: String?) : InterruptedIOException(message) {
}