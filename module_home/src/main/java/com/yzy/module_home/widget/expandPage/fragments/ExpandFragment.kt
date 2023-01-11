package com.yzy.module_home.widget.expandPage.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.yzy.lib_common.base.viewmodel.BaseViewModel
import com.yzy.module_base.base.BaseFragment
import com.yzy.module_home.R
import com.yzy.module_home.databinding.FragmentExpandingBinding
import com.yzy.module_home.widget.expandPage.fragments.vm.ExpandVM

abstract class ExpandFragment< DB : ViewDataBinding>  : BaseFragment< FragmentExpandingBinding>() {
    var fgtTop: Fragment? = null
    var fgtBottom: Fragment? = null
    private var startY = 0f
    private var defaultCardElevation = 0f
    private var mListener: OnExpandingClickListener? = null
    private var frontAnimator: ObjectAnimator? = null
    private var backAnimator: ObjectAnimator? = null

    override fun layoutId(): Int {
        return R.layout.fragment_expanding
    }

    override fun initView(savedInstanceState: Bundle?) {
        fgtTop = getFragmentTop()
        fgtBottom = getFragmentBottom()
        if (fgtTop != null && fgtBottom != null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.card_front, fgtTop!!)
                .replace(R.id.bottomLayout, fgtBottom!!)
                .commit()
        }
        view?.setOnClickListener(OnClick())
        setupDownGesture(view)
        defaultCardElevation = mDatabind.cardFront.cardElevation
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

    override fun immersionBarEnabled(): Boolean {
        return false
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setupDownGesture(view: View?) {
        view?.setOnTouchListener(OnTouchListener { v, event ->
            var my = 0f
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startY = event.y
                MotionEvent.ACTION_MOVE -> my = event.y
                MotionEvent.ACTION_UP -> if (isOpenend && event.y - startY > 0) {
                    close()
                    return@OnTouchListener true
                }
            }
            false
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnExpandingClickListener) {
            context
        } else {
            throw RuntimeException(context.toString()
                    + "ExpandingFragment must implement OnExpandingClickListener")
        }
    }

    abstract fun getFragmentTop(): Fragment
    abstract fun getFragmentBottom(): Fragment

    val isClosed: Boolean
        get() = ViewCompat.getScaleX(mDatabind.cardBack) == SCALE_CLOSED.toFloat()
    val isOpenend: Boolean
        get() = ViewCompat.getScaleX(mDatabind.cardBack) == SCALE_OPENED

    fun toggle() {
        if (isClosed) {
            open()
        } else {
            close()
        }
    }

    fun open() {
        val layoutParams = mDatabind.bottomLayout.layoutParams
        layoutParams.height = (mDatabind.cardFront.height * SCALE_OPENED / 4 * SCALE_OPENED).toInt()
        mDatabind.bottomLayout.layoutParams = layoutParams
        ViewCompat.setPivotY(mDatabind.cardBack, 0f)
        val front1 =
            PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,
                0f,
                (-mDatabind.cardFront.height / 4).toFloat())
        val front2 = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1f)
        frontAnimator = ObjectAnimator.ofPropertyValuesHolder(mDatabind.cardFront, front1, front2)
        val backX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.2f)
        val backY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.2f)
        backAnimator = ObjectAnimator.ofPropertyValuesHolder(mDatabind.cardBack, backX, backY)
        mDatabind.cardBack.pivotY = 0f
        frontAnimator!!.start()
        backAnimator!!.start()
        mDatabind.cardFront.cardElevation = ELEVATION_OPENED.toFloat()
    }

    fun close() {
        if (frontAnimator != null) {
            frontAnimator!!.reverse()
            backAnimator!!.reverse()
            backAnimator = null
            frontAnimator = null
        }
        mDatabind.cardFront.cardElevation = defaultCardElevation
    }

    internal inner class OnClick : View.OnClickListener {
        override fun onClick(v: View) {
            if (isOpenend) {
                if (mListener != null) {
                    mListener!!.onExpandingClick(v)
                }
            } else {
                open()
            }
        }
    }

    interface OnExpandingClickListener {
        fun onExpandingClick(v: View)
    }
    companion object {
        const val SCALE_OPENED = 1.2f
        const val SCALE_CLOSED = 1
        const val ELEVATION_OPENED = 40
    }


}