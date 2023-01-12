package com.yzy.lib_protocal.bean

/**
 * Created by yzy on 2023/1/11.
 */
data class ViewNode(
    val name: String,
    //view  viewGroup
    val type: String,
    //style样式
    val style: HashMap<String,String>?,
    val id: Int,
    //父布局id
    val parentId: Int?,
    //父布局名字
    val parentName: String?,

    //父布局约束
    val layoutParams:HashMap<String,String>?,

    //子布局
    val children: List<ViewNode>?,

    val action: HashMap<String,String>?


){



}

