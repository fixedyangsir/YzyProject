package com.yzy.lib_protocal.widget.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.hjq.shape.view.ShapeTextView
import com.viewmodel.compiler.InjectView
import com.yzy.lib_protocal.base.*

/**
 * Created by yzy on 2023/1/11.
 */

//自动注入到初始化容器

class YzyTextView @JvmOverloads @InjectView(YzyTextView::class) constructor(
    context: Context, attrs: AttributeSet? = null,
) : ShapeTextView(context, attrs), IYzyView {
    override fun getView(): View {
        return  this
    }

    override fun bindStyle(style: IYzyStyle) {


        if (style is YzyTextViewStyle) {
            with(style) {
                text?.let {
                    setText(it)
                }
                textSize?.let {
                    setTextSize(it.toFloat())
                }
                textColor?.let {
                    setTextColor(Color.parseColor(it))
                }

                paddins?.let {
                    if (it.size == 4) {
                        setPadding(it[0].toInt(), it[1].toInt(), it[2].toInt(), it[3].toInt())
                    }
                }
                gravity?.let {
                    if (it=="center"){
                        setGravity(Gravity.CENTER)
                    }else{
                        //
                    }
                }


                val build = shapeDrawableBuilder

                solidColor?.let {
                    build.setSolidColor(Color.parseColor(it))
                }
                corners?.let {
                    if (it.size == 4) {
                        build.setRadius(it[0].toFloat(), it[1].toFloat(), it[2].toFloat(), it[3].toFloat())
                    }
                }

                build.intoBackground()
            }

        }


    }

    override fun adaptStyle(attrs: HashMap<String, String>): IYzyStyle {
          return  YzyTextViewStyle(
              text = attrs["text"],
              textColor = attrs["textColor"],
              textSize = attrs["textSize"]?.toIntOrNull(),
              gravity = attrs["gravity"],
              solidColor = attrs["solidColor"],
              corners = attrs["corners"]?.split(",")
          )
    }

    override fun bindAction(action: IYzyAction) {


        if (action is YzyClickAction){
            setOnClickListener {

                if (action.actionIntent is YzyClickActionIntent.ShowToast){
                    Toast.makeText(context,action.data,Toast.LENGTH_SHORT).show()
                }else{
                    //通过arouter 跳转

                }

            }

        }


    }



}

/**
 * textview基本属性
 */
data class YzyTextViewStyle(
    val text: String? = null,
    val textSize: Int? = null,
    val gravity:  String? = null,
    val textColor: String? = null,
    val solidColor: String? = null,
    val corners: List<String>? = null,
    val paddins: List<String>? = null,
) : IYzyStyle