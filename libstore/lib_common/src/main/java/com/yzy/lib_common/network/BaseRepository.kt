package com.yzy.lib_common.network

/**
 * Created by yzy on 2023/1/11.
 */
open class BaseRepository {

    suspend fun <T : Any> executeRequest(
        block: suspend () -> BaseResponse<T>
    ): BaseResponse<T> {

        return block.invoke()
    }
}