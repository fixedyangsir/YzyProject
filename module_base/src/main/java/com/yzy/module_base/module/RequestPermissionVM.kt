package com.yzy.module_base.module

import android.content.Context
import androidx.lifecycle.ViewModel
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.yzy.lib_common.base.mvi.Container
import com.yzy.lib_common.base.mvi.UiEvent
import com.yzy.lib_common.base.mvi.UiState
import com.yzy.lib_common.base.mvi.extension.containers
import com.yzy.module_base.permission.DefaultPermissionInterceptor

/**
 * 用于权限请求
 */
class RequestPermissionVM : ViewModel() {


    private val _container by containers<RequestPermissionUiState>(RequestPermissionUiState.INIT)

    val container: Container<RequestPermissionUiState, UiEvent> = _container


    fun dispatch(intent: RequestPermissionIntent) {


        when (intent) {
            is RequestPermissionIntent.RequestCarmaPermissionIntent -> {

                XXPermissions.with(intent.context).permission(
                    Permission.CAMERA
                ).interceptor(DefaultPermissionInterceptor()).request { permissions, allGranted ->
                    if (allGranted) {
                        _container.updateState {
                            RequestPermissionUiState.RequestCarmaPermissionSuccessState()
                        }
                    } else {
                        _container.updateState {
                            RequestPermissionUiState.RequestCarmaPermissionFailState()
                        }

                    }
                }
            }

            is RequestPermissionIntent.RequestStoragePermissionIntent -> {

                XXPermissions.with(intent.context).permission(
                    Permission.WRITE_EXTERNAL_STORAGE, Permission.READ_EXTERNAL_STORAGE
                ).interceptor(DefaultPermissionInterceptor()).request { permissions, allGranted ->
                    if (allGranted) {
                        _container.updateState {
                            RequestPermissionUiState.RequestStoragePermissionSuccessState()
                        }
                    } else {
                        _container.updateState {
                            RequestPermissionUiState.RequestStoragePermissionFailState()
                        }

                    }
                }


            }


            else -> {


            }
        }


    }


}


sealed class RequestPermissionUiState() : UiState {
    object INIT : RequestPermissionUiState()

    class RequestCarmaPermissionSuccessState() : RequestPermissionUiState()

    class RequestCarmaPermissionFailState() : RequestPermissionUiState()




    class RequestStoragePermissionSuccessState() : RequestPermissionUiState()

    class RequestStoragePermissionFailState() : RequestPermissionUiState()

    

}

sealed class RequestPermissionIntent(val context: Context) {
    class RequestCarmaPermissionIntent(context: Context) : RequestPermissionIntent(context)
    class RequestStoragePermissionIntent(context: Context) : RequestPermissionIntent(context)
}