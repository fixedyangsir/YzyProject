package com.yzy.lib_common.ext

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import java.math.BigDecimal
import java.util.regex.Pattern


/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (compareTime(lastClickTime, currentTime, interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}

fun compareTime(lastClickTime: Long, currentTime: Long, interval: Long): Boolean {
    return lastClickTime != 0L && (currentTime - lastClickTime < interval) && currentTime > lastClickTime
}

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}


/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrGone(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

/**
 * 根据条件设置view显示隐藏 为true 显示，为false 隐藏
 */
fun View.visibleOrInvisible(flag: Boolean) {
    visibility = if (flag) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}


fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true,
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    offscreenPageLimit = fragments.size
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}

fun ViewPager2.init(
    fragmentActivity: FragmentActivity,
    fragments: MutableList<Fragment>,
    isUserInputEnabled: Boolean = true,
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    offscreenPageLimit = fragments.size
    //设置适配器
    adapter = object :
        FragmentStateAdapter(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size

    }
    return this
}


fun EditText.isTextEmpty(): Boolean {
    return TextUtils.isEmpty(input())
}


fun EditText.isMoreThan(min: BigDecimal, isEqual: Boolean = false): Boolean {
    if (isTextEmpty() || !input().isNumber()) {
        return false
    }
    if (isEqual) {
        return input().toBigDecimal() >= min
    } else {
        return input().toBigDecimal() > min
    }

}


fun EditText.isLessThan(max: BigDecimal): Boolean {
    if (isTextEmpty() || !input().isNumber()) {
        return false
    }
    return input().toBigDecimal() < max
}

fun EditText.input(): String {
    return this.text.toString().trim()
}

/**
 * 判断是否是数字
 */
fun String.isNumber(): Boolean {
    val rex = "-?[0-9]+(.[0-9]+)?"
    val p = Pattern.compile(rex)
    val m = p.matcher(this)
    return m.find()
}

fun EditText.toEndSelection() {
    setSelection(input().length)
}


/**实例化 Fragment*/
inline fun <reified T : Fragment> Context.newInstanceFragment(args: Bundle? = null): T {
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(
        classLoader, className
    )
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
}


/**实例化 Fragment*/
inline fun <reified T : Fragment> Context.newInstanceFragment(vararg pair: Pair<String, String>): T {
    val args = Bundle()
    pair?.let {
        for (arg in pair) {
            args.putString(arg.first, arg.second)
        }
    }
    val className = T::class.java.name;
    val clazz = FragmentFactory.loadFragmentClass(
        classLoader, className
    )
    val f = clazz.getConstructor().newInstance()
    if (args != null) {
        args.classLoader = f.javaClass.classLoader
        f.arguments = args
    }
    return f as T
}


