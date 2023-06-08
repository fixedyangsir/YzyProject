package com.yzy.lib_common.glide

import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.request.RequestOptions

import com.yzy.lib_common.widget.MultiStatePage

import java.io.File


/**
 * Created by yzy on 2020/8/3.
 */
object ImageExt {

    fun ImageView.loadImage(
        url: Any?,
        errorIcon: Int = MultiStatePage.config.errorIcon,
        radius: Int = 0,
        tranform: BitmapTransformation = FitCenter(),
        strategy: DiskCacheStrategy =DiskCacheStrategy.AUTOMATIC,
        skipMemoryCache:Boolean=false
    ) {
        val myOptions = RequestOptions.bitmapTransform(
            if (radius == 0) {
                tranform
            } else {
                MultiTransformation(
                    tranform,
                    RoundedCorners(radius)
                )
            }

        )
        Glide.with(this.context)
            .load(if (url is Int) ResourcesCompat.getDrawable(this.context.resources,
                url,
                null) else url)
            .apply(myOptions)
            .placeholder(errorIcon)
            .error(errorIcon)
            .skipMemoryCache(skipMemoryCache)
            .diskCacheStrategy(strategy)
            .into(this)
    }



    //加载本地图片
    fun ImageView.loadLocationImage(resId: Int) {
        Glide.with(this.context)
            .load(ResourcesCompat.getDrawable(this.context.resources, resId, null))
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(this)
    }

    //加载手机内部图片 圆
    fun ImageView.loadPhoneCircleImage(filePath: String) {
        Glide.with(this.context).load(File(filePath))
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(this)
    }

    //加载手机内部图片
    fun ImageView.loadPhoneImage(filePath: String) {
        Glide.with(this.context).load(File(filePath))
            .into(this)
    }




}
