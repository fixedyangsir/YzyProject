package com.yzy.lib_common.base.lifecycle

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import com.yzy.lib_common.base.activity.BaseVmActivity
import java.util.*

/**
 * activity管理
 */
object KtxActivityManger {
    private val mActivityList = LinkedList<BaseVmActivity<*>>()

    //三方activity
    private val mOtherActivityList by lazy {
        LinkedList<Activity>()
    }

    fun getAllSize(): Int {
        return mActivityList.size
    }

    fun getAllList(): LinkedList<BaseVmActivity<*>> = mActivityList


    /**
     * 当前activity
     */
    val currentActivity: BaseVmActivity<*>?
        get() =
            if (mActivityList.isEmpty()) null
            else mActivityList.last

    /**
     * activity入栈
     */
    fun pushActivity(activity: BaseVmActivity<*>) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        } else {
            mActivityList.add(activity)
        }
    } /**
     * 三方activity入栈
     */
    fun pushOtherActivity(activity: Activity) {
        if (mOtherActivityList.contains(activity)) {
            if (mOtherActivityList.last != activity) {
                mOtherActivityList.remove(activity)
                mOtherActivityList.add(activity)
            }
        } else {
            mOtherActivityList.add(activity)
        }
    }

    /**
     * activity出栈
     */
    fun popActivity(activity: BaseVmActivity<*>) {
        mActivityList.remove(activity)
    }

    /**
     * 三方activity出栈
     */
    fun popOtherActivity(activity: Activity) {
        mOtherActivityList.remove(activity)
    }

    /**
     * 关闭当前activity
     */
    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    /**
     * 关闭传入的activity
     */
    fun finishActivity(activity: BaseVmActivity<*>) {
        mActivityList.remove(activity)
        activity.finish()
    }

    /**
     * 关闭传入的activity类名
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }
    /**
     * 关闭三方传入的activity类名
     */
    fun finishOtherActivity(clazz: Class<*>) {
        for (activity in mOtherActivityList)
            if (activity.javaClass == clazz)
                activity.finish()
    }

    /**
     * 关闭所有的activity
     */
    fun finishAllActivity() {
        for (activity in mActivityList)
            activity.finish()
    }

    /**
     * 结束栈顶activity
     */
    fun finishTopActivity() {
        if (!mActivityList.isEmpty()) {
            finishActivity(mActivityList.last)
        }
    }


    fun finishTaskToBackActivity(clazz: Class<*>) {
        for (activity in mActivityList)
            if (activity::class.java == clazz) {
                activity.moveTaskToBack(false)
            } else {
                activity.finish()
            }

    }

    /**
     * 关闭除clazz之外的所有activity
     */
    fun finishContainActivity(clazz: Class<*>): Boolean {
        var boolean = false
        val it = mActivityList.iterator()
        while (it.hasNext()) {
            val activity = it.next()
            if (activity::class.java != clazz) {
                activity.finish()
                it.remove()
            } else {
                boolean = true
            }
        }

        return boolean
    }


    /**
     * 关闭除clazz集合之外的所有activity
     */
    fun finishContainActivity(clazzArray: Array<Class<*>>) {
        //需要删除的activity
        val list = mActivityList.filter {
            it::class.java !in clazzArray
        }
        for(clazz in list){
            val it = mActivityList.iterator()
            while (it.hasNext()) {
                val activity = it.next()
                if (activity::class.java == clazz::class.java) {
                    activity.finish()
                    it.remove()
                }
            }
        }
    }


    /**
     * 包含clazz activity
     */
    fun isContainActivity(clazz: Class<*>): Boolean {
        for (activity in mActivityList) {
            if (activity::class.java == clazz) {
                return true
            }
        }
        return false
    }

    /**
     * clazz activity 是否在栈顶
     */
    fun isTopActivity(clazz: Class<*>): Boolean {
        if (!mActivityList.isEmpty() && mActivityList.last.javaClass == clazz) {
            return true
        }
        return false
    }

    /**
     * 查找 activity
     */
    fun findActivity(clazz: Class<*>): BaseVmActivity<*>? {
        for (activity in mActivityList) {
            if (activity::class.java == clazz) {
                return activity
            }
        }
        return null
    }

    /**
     * app是否运行在前台
     */
    fun isRunningForeground(context: Context): Boolean {
        val activityManager =
            context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.applicationContext.applicationInfo.packageName
        for (runningAppProcessInfo in runningAppProcesses) {
            if (packageName.equals(
                    runningAppProcessInfo.processName,
                    ignoreCase = true
                ) && runningAppProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
            ) {
                return true
            }
        }
        return false
    }

}