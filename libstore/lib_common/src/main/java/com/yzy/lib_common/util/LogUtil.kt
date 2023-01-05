package com.yzy.lib_common.util



import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.yzy.lib_common.BuildConfig


object LogUtil {

    internal fun init() {
        if (BuildConfig.DEBUG) {
            val builder = PrettyFormatStrategy.newBuilder()
            builder.methodCount(2)
            builder.methodOffset(1)
            builder.tag("LogUtil")
            val adapter = AndroidLogAdapter(builder.build())
            Logger.addLogAdapter(adapter)

        }
    }

    fun v(message: String?) {
        message?.let {
            Logger.v(it)
        } ?: let {
            Logger.v("message is empty")
        }
    }

    fun i(message: String?) {
        message?.let {
            Logger.i(it)
        } ?: let {
            Logger.i("message is empty")
        }
    }

    fun d(message: String?) {
        //   Timber.tag(tag).d(message)
        message?.let {
            Logger.d(it)
        } ?: let {
            Logger.d("message is empty")
        }

    }

    fun w(message: String?) {
        message?.let {
            Logger.w(it)
        } ?: let {
            Logger.w("message is empty")
        }
    }

    fun e(message: String?) {
        message?.let {
            Logger.e(it)
        } ?: let {
            Logger.e("message is empty")
        }
    }

    fun json(message: String?) {
        message?.let {
            Logger.json(it)
        } ?: let {
            Logger.e("message is empty")
        }
    }

    fun xml(message: String?) {
        message?.let {
            Logger.xml(it)
        } ?: let {
            Logger.xml("message is empty")
        }
    }

}

fun String.logd(vararg parameters: String = arrayOf()) {
    this.let {
        if (!parameters.isNullOrEmpty()) {
            Logger.d("$it ---->${parameters.joinToString()}")
        } else {
            Logger.d(it)
        }
    }
}

fun String.logd() {
    this.let {
        Logger.d(it)
    }
}


fun String.logv() {
    this.let {
        Logger.v(it)
    }
}

fun String.logv(vararg parameters: String = arrayOf()) {
    this.let {
        if (!parameters.isNullOrEmpty()) {
            Logger.v("$it ---->${parameters.joinToString()}")
        } else {
            Logger.v(it)
        }
    }
}



fun String.loge() {
    this.let {
        Logger.e(it)
    }
}


fun String.loge(vararg parameters: String = arrayOf()) {
    this.let {
        if (!parameters.isNullOrEmpty()) {
            Logger.e("$it ---->${parameters.joinToString()}")
        } else {
            Logger.e(it)
        }
    }
}


fun String.logw() {
    this.let {
        Logger.w(it)
    }
}

fun String.logw(vararg parameters: String = arrayOf()) {
    this.let {
        if (!parameters.isNullOrEmpty()) {
            Logger.w("$it ---->${parameters.joinToString()}")
        } else {
            Logger.w(it)
        }
    }
}


fun String.logi() {
    this.let {
        Logger.i(it)
    }
}

fun String.logi(vararg parameters: String = arrayOf()) {
    this.let {
        if (!parameters.isNullOrEmpty()) {
            Logger.i("$it ---->${parameters.joinToString()}")
        } else {
            Logger.i(it)
        }
    }
}

fun String.logJson() {
    this.let {
        Logger.json(it)
    }
}

fun String.logXml() {
    this.let {
        Logger.xml(it)
    }
}