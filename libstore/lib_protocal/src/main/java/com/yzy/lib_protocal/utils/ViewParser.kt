package com.yzy.lib_protocal.utils

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import com.alibaba.android.arouter.utils.ClassUtils
import com.google.android.flexbox.FlexboxLayout
import com.yzy.lib_protocal.base.*
import com.yzy.lib_protocal.bean.ViewNode

/**
 * 解析ViewNode
 * Created by yzy on 2023/1/11.
 */
object ViewParser {
    private const val VIEW_PROVIDER_PATH = "com.yzy.lib_protocal.widget.view"

    //所有支持的动态view
    val viewProvider = mutableMapOf<String, String>()

    //所有支持的LayoutParams
    val layoutParamsSupport = mutableMapOf<String, String>()


    fun init(context: Context) {

        //获取apt生成的文件
        val set = ClassUtils.getFileNameByPackageName(context, VIEW_PROVIDER_PATH)

        set.filter {
            it.endsWith("Provider")
        }.forEach {

            //通过反射创建IYzyViewProvider
            val cls = Class.forName(it)

            val provider: IYzyViewProvider = cls.newInstance() as IYzyViewProvider
            //添加到map 保存包名
            provider.loadInto(viewProvider)

        }
        set.filter {
            it.endsWith("Support")
        }.forEach {

            //通过反射创建IYzyLayoutParamsSupport
            val cls = Class.forName(it)
            val support: IYzyLayoutParamsSupport = cls.newInstance() as IYzyLayoutParamsSupport
            //添加到map 保存包名
            support.loadInto(layoutParamsSupport)

        }


    }


    fun adapt(viewNode: ViewNode, context: Context): IYzyView? {

        return adapt(viewNode, context, null)

    }


    private fun adapt(viewNode: ViewNode, context: Context, parent: IYzyView?): IYzyView? {

        if (!viewProvider.containsKey(viewNode.name)) {
            return null
        }
        val viewPackageName = viewProvider.get(viewNode.name)
        //通过反射创建View
        val cls = Class.forName(viewPackageName)
        val con = cls.getConstructor(Context::class.java)
        val v: IYzyView = con.newInstance(context) as IYzyView
        //绑定属性
        viewNode.style?.let {
            v.bindStyle(v.adaptStyle(it))
        }
        //绑定事件
        adaptAction(viewNode)?.let {
            v.bindAction(it)
        }

        parent?.let {
            if (it.getView() is ViewGroup) {

                (it.getView() as ViewGroup).addView(v.getView(),
                    adaptLayoutParams(viewNode))
            }
        }

        //递归 继续解析子View
        if (viewNode.type == "ViewGroup" && viewNode.children?.size != null && viewNode.children.isNotEmpty()) {
            viewNode.children.forEach {
                adapt(it, context, v)
            }
        }
        return parent ?: v
    }


    fun adaptLayoutParams(viewNode: ViewNode): ViewGroup.LayoutParams? {

        if (viewNode.layoutParams == null || TextUtils.isEmpty(viewNode.parentName) || !layoutParamsSupport.containsKey(
                viewNode.parentName)
        ) {
            return ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        }

        //通过反射创建layoutParams
        val layoutParamsPackageName = layoutParamsSupport.get(viewNode.parentName)
        val cls = Class.forName(layoutParamsPackageName)


        val layoutParams: IYzyLayoutParams = cls.newInstance() as IYzyLayoutParams

        return layoutParams.bindParams(viewNode.layoutParams)

    }

    private fun adaptAction(viewNode: ViewNode): IYzyAction? {
        val action = viewNode.action ?: return null

        when (action["type"]) {
            "Click" -> {
                val data = action["data"]

                val actionIntent: YzyClickActionIntent? = when (action["intent"]) {
                    "ShowToast" -> {
                        YzyClickActionIntent.ShowToast(data!!)
                    }
                    "GoPage" -> {
                        YzyClickActionIntent.GoPage(data!!)
                    }
                    else -> {
                        null
                    }
                }

                return YzyClickAction(action["data"], actionIntent = actionIntent)
            }

        }

        return null

    }

}