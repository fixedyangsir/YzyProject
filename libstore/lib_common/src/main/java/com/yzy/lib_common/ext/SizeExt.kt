package com.yzy.lib_common.ext

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import com.yzy.lib_common.base.Ktx
import com.yzy.lib_common.base.lifecycle.KtxActivityManger

/**
 * 尺寸扩展类
 */

/**
 * 获取屏幕宽度
 */
val Context.screenWidth
    get() = getDisplayMetrics().widthPixels

/**
 * 获取屏幕高度
 */
val Context.screenHeight
    get() = getDisplayMetrics().heightPixels


/**
 * float 转dp
 */
inline fun <reified T> Float.dp(): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this,
        getDisplayMetrics()
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}
inline fun <reified T> Int.dip2px(): T  {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX, this.toFloat(),
        getDisplayMetrics()
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}
inline fun <reified T> Float.dip2px(): T  {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_PX, this.toFloat(),
        getDisplayMetrics()
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}
/**
 * int 转dp  dp2px
 */
inline fun <reified T> Int.dp(): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(),
        getDisplayMetrics()
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}


/**
 * int 转sp
 */
inline fun <reified T> Int.sp(): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, this.toFloat(),
        getDisplayMetrics()
    )

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}
/**
 * int px转dp
 */
inline fun <reified T> Int.px2dp(): T {
    val result = (this / getDisplayMetrics().density + 0.5f).toInt()

    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

fun getDisplayMetrics(): DisplayMetrics {
    if (KtxActivityManger.currentActivity != null) {
        return KtxActivityManger.currentActivity!!.resources.displayMetrics
    }
    return Ktx.app.resources.displayMetrics

}