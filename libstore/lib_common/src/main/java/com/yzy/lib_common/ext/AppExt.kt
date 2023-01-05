package com.yzy.lib_common.ext

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.util.ProcessUtil
import com.yzy.lib_common.base.lifecycle.KtxActivityManger

/**
 * 获取进程号对应的进程名
 *
 * @return 进程名
 */
fun Application.getAppProcessName(): String {
    val process = ProcessUtil.getCurrentProcessName(this)
    return if (TextUtils.isEmpty(process)) "" else process!!
}


inline fun <reified T> AppCompatActivity.goActivity(bundle: Bundle? = null) {
    bundle?.let {
        this.startActivity(Intent(this, T::class.java).apply {
            putExtras(bundle)
        })
    } ?: let {
        this.startActivity(Intent(this, T::class.java))
    }

}

inline fun <reified T> Context.goActivity(bundle: Bundle? = null) {
    bundle?.let {
        this.startActivity(Intent(this, T::class.java).apply {
            putExtras(bundle)
        })
    } ?: let {
        this.startActivity(Intent(this, T::class.java))
    }

}

inline fun <reified T> Context.goActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int = 0
) {
    (this as AppCompatActivity).goActivityForResult<T>(bundle, requestCode)

}

inline fun <reified T> Fragment.goActivity(bundle: Bundle? = null) {
    bundle?.let {
        this.startActivity(Intent(context, T::class.java).apply {
            putExtras(bundle)
        })
    } ?: let {
        this.startActivity(Intent(context, T::class.java))
    }

}

inline fun <reified T> AppCompatActivity.goActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int = 0
) {
    bundle?.let {
        this.startActivityForResult(Intent(this, T::class.java).apply {
            putExtras(bundle)
        }, requestCode)
    } ?: let {
        this.startActivityForResult(Intent(this, T::class.java), requestCode)
    }

}


inline fun <reified T> Fragment.goActivityForResult(
    bundle: Bundle? = null,
    requestCode: Int = 0
) {
    bundle?.let {
        this.startActivityForResult(Intent(context, T::class.java).apply {
            putExtras(bundle)
        }, requestCode)
    } ?: let {
        this.startActivityForResult(Intent(context, T::class.java), requestCode)
    }
}

fun Int.getResString(): String {
    return try {
        KtxActivityManger.currentActivity?.getString(this) ?: let {
            Ktx.app.getString(this)
        }
    } catch (e: Exception) {
        try {
            Ktx.app.getString(this)
        } catch (e: Exception) {
            return ""
        }
    }
}

fun Int.getResColor(): Int {
    return try {
        Ktx.app.getColor(this)
    } catch (e: Exception) {
        0
    }
}
fun <T> T?.getDefault(default: T): T = this?.let { this } ?: default











