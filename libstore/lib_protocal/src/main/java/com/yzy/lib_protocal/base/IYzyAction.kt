package com.yzy.lib_protocal.base

/**
 * Created by yzy on 2023/1/11.
 */
abstract class IYzyAction(val data: String?, val actionIntent: YzyClickActionIntent?) {
}

//定义不同的意图
sealed class YzyClickActionIntent() {

    data class ShowToast(val message: String) : YzyClickActionIntent()
    data class GoPage(val path: String) : YzyClickActionIntent()

}


class YzyClickAction(data: String?, actionIntent: YzyClickActionIntent?) : IYzyAction(data,
    actionIntent) {

}