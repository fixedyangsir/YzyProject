package com.yzy.lib_common.widget

import androidx.annotation.DrawableRes


data class MultiStateConfig(
    val errorMsg: String = "Error",
    @DrawableRes
    val errorIcon: Int = android.R.drawable.stat_notify_error,
    val emptyMsg: String = "no data",
    @DrawableRes
    val emptyIcon: Int =android.R.drawable.stat_notify_error,
    val loadingMsg: String = "Loading"
) {

   open class Builder {
        private var errorMsg: String = "Error"

        @DrawableRes
        private var errorIcon: Int =android.R.drawable.stat_notify_error
        private var emptyMsg: String = "no data"

        @DrawableRes
        private var emptyIcon: Int =android.R.drawable.stat_notify_error
        private var loadingMsg: String = "Loading"


        fun errorMsg(msg: String): Builder {
            this.errorMsg = msg
            return this
        }

        fun errorIcon(@DrawableRes icon: Int): Builder {
            this.errorIcon = icon
            return this
        }

        fun emptyMsg(msg: String): Builder {
            this.emptyMsg = msg
            return this
        }

        fun emptyIcon(@DrawableRes icon: Int): Builder {
            this.emptyIcon = icon
            return this
        }

        fun loadingMsg(msg: String): Builder {
            this.loadingMsg = msg
            return this
        }

        fun build() = MultiStateConfig(
            errorMsg = errorMsg,
            errorIcon = errorIcon,
            emptyMsg = emptyMsg,
            emptyIcon = emptyIcon,
            loadingMsg = loadingMsg,
        )
    }
}