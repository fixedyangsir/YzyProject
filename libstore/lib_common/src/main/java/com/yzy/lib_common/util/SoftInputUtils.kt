package com.yzy.lib_common.util

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.NonNull
import com.lxj.xpopup.util.XPopupUtils
import com.yzy.lib_common.base.Ktx
import java.util.*

/**
 * Created by yzy on 2021/3/2.
 */
object SoftInputUtils {


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && (v is EditText)) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom)
        }
        return false

    }

    /**
     * 隐藏软件盘方法
     *
     * @param token
     */
    fun hideSoftInput(context: Context, token: IBinder?) {
        if (token != null) {
            val im = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * Hide the soft input.
     *
     * @param activity The activity.
     */
    fun hideSoftInput(@NonNull activity: Activity) {
        hideSoftInput(activity.window)
    }

    /**
     * Show the soft input.
     */
    fun showSoftInput(activity: Activity) {
        if (!isSoftInputVisible(activity)) {
            toggleSoftInput()
        }
    }


    /**
     * Hide the soft input.
     *
     * @param window The window.
     */
    private   fun hideSoftInput(window: Window) {
        var view: View? = window.getCurrentFocus()
        if (view == null) {
            val decorView: View = window.getDecorView()
            val focusView: View? = decorView.findViewWithTag("keyboardTagView")
            if (focusView == null) {
                view = EditText(window.getContext())
                view.setTag("keyboardTagView")
                (decorView as ViewGroup).addView(view, 0, 0)
            } else {
                view = focusView
            }
            view.requestFocus()
        }
        hideSoftInput(view)
    }

    /**
     * Hide the soft input.
     *
     * @param view The view.
     */
    private fun hideSoftInput(view: View) {
        val imm: InputMethodManager =
            Ktx.app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Return whether soft input is visible.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
     fun isSoftInputVisible(activity: Activity): Boolean {
        return XPopupUtils.getDecorViewInvisibleHeight(activity.window) > 0
    }

    /**
     * Toggle the soft input display or not.
     */
    private   fun toggleSoftInput() {
        val imm = Ktx.app.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager ?: return
        imm.toggleSoftInput(0, 0)
    }

}