package com.yzy.lib_protocal.base

import android.view.View

/**
 * Created by yzy on 2023/1/11.
 */
interface IYzyView {



    fun getView(): View

    fun bindStyle(style: IYzyStyle)

    fun adaptStyle(attrs:HashMap<String,String>):IYzyStyle

    fun bindAction(action: IYzyAction)




}