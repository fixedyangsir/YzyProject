package com.yzy.lib_common.base.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.yzy.lib_common.base.activity.BaseVmActivity
import java.lang.Exception


/**
 * 界面监听
 */
class KtxLifeCycleCallBack : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        try {
            if (activity is BaseVmActivity<*>){
                KtxActivityManger.pushActivity(activity)
            }else{
                KtxActivityManger.pushOtherActivity(activity)
            }

        }catch (e:Exception){

        }

        //  "onActivityCreated : ${activity.localClassName}".logd()
    }

    override fun onActivityStarted(activity: Activity) {
        //    "onActivityStarted : ${activity.localClassName}".logd()
    }

    override fun onActivityResumed(activity: Activity) {
        //    "onActivityResumed : ${activity.localClassName}".logd()
    }

    override fun onActivityPaused(activity: Activity) {
        //    "onActivityPaused : ${activity.localClassName}".logd()
    }


    override fun onActivityDestroyed(activity: Activity) {
        //   "onActivityDestroyed : ${activity.localClassName}".logd()
        try {
            if (activity is BaseVmActivity<*> ){
                KtxActivityManger.popActivity(activity as BaseVmActivity<*>)
            }else{
                KtxActivityManger.popOtherActivity(activity)
            }

        }catch (e:Exception){

        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        //    "onActivitySaveInstanceState : ${activity.localClassName}".logd()
    }


    override fun onActivityStopped(activity: Activity) {
        //     "onActivityStopped : ${activity.localClassName}".logd()
    }


}