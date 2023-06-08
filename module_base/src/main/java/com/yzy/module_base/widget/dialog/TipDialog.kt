package com.yzy.module_base.widget.dialog

import android.content.Context
import android.text.TextUtils
import android.widget.TextView
import com.hjq.shape.view.ShapeTextView
import com.lxj.xpopup.core.CenterPopupView
import com.yzy.lib_common.ext.clickNoRepeat
import com.yzy.lib_common.ext.gone
import com.yzy.lib_common.ext.visible
import com.yzy.module_base.R



class TipDialog(context: Context) : CenterPopupView(context) {

    private var leftVisibility = true
    private var title: CharSequence = ""
    private var msg: CharSequence = ""
    private var leftText: CharSequence = ""
    private var rightText: CharSequence = ""
    private var leftListener: (()->Unit? )?= null
    private var rightListener:(()->Unit? )?= null
    private var rightTextBgColor = context.resources.getColor(R.color.colorAccent,null)


    override fun getImplLayoutId(): Int {
        return R.layout.dialog_base_tip
    }

    override fun onCreate() {
        super.onCreate()


        val tvTitle = findViewById<TextView>(R.id.tv_title)
        if (TextUtils.isEmpty(title)) {
            tvTitle.gone()
        } else {
            tvTitle.text = title
            tvTitle.visible()
        }

        val tvMsg = findViewById<TextView>(R.id.tv_message)
        tvMsg.text = msg

        val tvLeft = findViewById<ShapeTextView>(R.id.tv_cancel)
        tvLeft.text = leftText

        if (leftVisibility){
            tvLeft.visibility = VISIBLE
        }else{
            tvLeft.visibility = GONE
        }


        val tvRight = findViewById<ShapeTextView>(R.id.tv_confirm)
        tvRight.text = rightText

        tvLeft.clickNoRepeat {
            dismiss()
            leftListener?.invoke()
        }
        tvRight.clickNoRepeat {
            rightListener?.invoke()
        }
        tvRight.shapeDrawableBuilder.solidColor = rightTextBgColor
        tvRight.shapeDrawableBuilder.intoBackground()

    }

    fun setTitle(string: CharSequence): TipDialog {
        title = string
        return this
    }

    fun setMsg(string: CharSequence): TipDialog {
        msg = string
        return this
    }

    //左边按钮文案及点击
    fun setLeftText(string: CharSequence, listener:(()->Unit? )? = null): TipDialog {
        leftText = string
        leftListener = listener
        return this
    }
    fun setLeftVisibility(visibility:Boolean): TipDialog {
        leftVisibility = visibility
        return this
    }

    //右边边按钮文案及点击
    fun setRightText(string: CharSequence, listener: (()->Unit? )?= null): TipDialog {
        rightText = string
        rightListener = listener
        return this
    }

    fun setRightTextBgColor(color:Int): TipDialog{
        rightTextBgColor = color
        return this
    }
}