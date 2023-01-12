package com.yzy.lib_protocal.base

import android.view.ViewGroup

/**
 * Created by yzy on 2023/1/11.
 */
interface IYzyLayoutParams {

    fun bindParams(attrs:HashMap<String,String>):ViewGroup.LayoutParams

}


