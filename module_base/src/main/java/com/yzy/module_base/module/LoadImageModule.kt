package com.yzy.module_base.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject
import javax.inject.Qualifier

/**
 * 模拟测试不同框架加载图片
 */
@Module
@InstallIn(ActivityComponent::class)
abstract class LoadImageModule {


    @GlideLoadAnnotation
    @Binds
    abstract fun provideGlide(glideLoad: GlideLoadImage): LoadImageInterface


    @ColiLoadAnnotation
    @Binds
    abstract fun provideColi(coliLoad: ColiLoadImage): LoadImageInterface


}

interface LoadImageInterface {

    fun loadImage(url: String): String

}



// 定义Glide注解
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlideLoadAnnotation

// 定义Coli注解
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ColiLoadAnnotation






class GlideLoadImage @Inject constructor() : LoadImageInterface {

    override fun loadImage(url: String): String {
        return "使用Glide加载图片：$url"
    }

}

class ColiLoadImage @Inject constructor() : LoadImageInterface {

    override fun loadImage(url: String): String {
        return "使用Coli加载图片：$url"
    }

}


