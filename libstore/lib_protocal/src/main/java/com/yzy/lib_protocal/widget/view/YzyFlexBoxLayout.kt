package com.yzy.lib_protocal.widget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import com.viewmodel.compiler.InjectLayoutParams
import com.viewmodel.compiler.InjectView
import com.yzy.lib_protocal.base.*

/**
 * Created by yzy on 2023/1/11.
 */
//自动注入到初始化容器
class YzyFlexBoxLayout @JvmOverloads @InjectView(YzyFlexBoxLayout::class) constructor(
    context: Context, attrs: AttributeSet? = null,
) : FlexboxLayout(context, attrs), IYzyView {

    override fun getView(): View {
        return this
    }

    override fun bindStyle(style: IYzyStyle) {


        if (style is YzyFlexBoxStyle) {


            with(style) {
                when (flexDirection) {
                    "row" -> {
                        setFlexDirection(FlexDirection.ROW)
                    }
                    "column" -> {
                        setFlexDirection(FlexDirection.COLUMN)
                    }

                }
                when (justifyContent) {
                    "flex_start" -> {
                        setJustifyContent(JustifyContent.FLEX_START)
                    }
                    "center" -> {
                        setJustifyContent(JustifyContent.CENTER)
                    }

                }


            }


        }


    }

    override fun adaptStyle(attrs: HashMap<String, String>): IYzyStyle {
        return YzyFlexBoxStyle(
            flexDirection = attrs["flexDirection"],
            justifyContent = attrs["justifyContent"],
        )
    }

    override fun bindAction(action: IYzyAction) {

    }


}

/**
 * FlexBox基本属性
 */
data class YzyFlexBoxStyle(
    val flexDirection: String? = null,
    val justifyContent: String? = null,

    ) : IYzyStyle


/**
 * 为子view提供LayoutParams
 */
//自动注入到初始化容器
class YzyFlexBoxLayoutParams @InjectLayoutParams(YzyFlexBoxLayoutParams::class) constructor() :
    IYzyLayoutParams {
    override fun bindParams(attrs: HashMap<String, String>): ViewGroup.LayoutParams {

        return FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            setWidth(attrs["width"]!!.toInt())
            setHeight(attrs["height"]!!.toInt())
        }
    }


}
