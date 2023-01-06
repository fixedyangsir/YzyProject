package com.yzy.module_base.service

import com.alibaba.android.arouter.facade.template.IProvider
import com.yzy.module_base.bean.Travel

/**
 * Created by yzy on 2023/1/5.
 */

interface IDataService:IProvider {


    fun getHomeData():MutableList<Travel>




}