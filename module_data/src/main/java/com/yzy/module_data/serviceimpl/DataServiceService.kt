package com.yzy.module_data.serviceimpl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.yzy.module_base.bean.Travel
import com.yzy.module_base.service.IDataService
import com.yzy.module_base.utils.ARouterUtils
import com.yzy.module_data.R

/**
 * 组件化数据通信
 */
@Route(path = ARouterUtils.SERVICE_HOME_DATA)
class DataServiceService:IDataService {
    override fun getHomeData(): MutableList<Travel> {
       return mutableListOf<Travel>().apply {
           add(Travel("VirtualView 控件演示",
               "by 杨镇瑜",
               "2023-1-06",
               "简介：阿里巴巴开源动态化界面库",
               "MY  PROJECT",
               R.mipmap.icon_like,
               R.mipmap.icon_page_03))

           add(Travel("Hilt 依赖注入展示",
               "by 杨镇瑜",
               "2023-1-06",
               "简介：替代Dagger的依赖注入框架",
               "MY  PROJECT",
               R.mipmap.icon_star,
               R.mipmap.icon_page_02))


       }
    }

    override fun init(context: Context?) {

    }
}