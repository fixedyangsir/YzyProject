package com.yzy.module_home.widget.expandPage

import androidx.viewpager.widget.ViewPager
import android.view.View

class ExpandTransformer : ViewPager.PageTransformer {
    override fun transformPage(page: View, p: Float) {
        var position = p
        position = if (position < -1) -1f else position
        position = if (position > 1) 1f else position
        val tempScale = if (position < 0) 1 + position else 1 - position
        val slope = (MAX_SCALE - MIN_SCALE) / 1
        val scaleValue = MIN_SCALE + tempScale * slope
        page.scaleX = scaleValue
        page.scaleY = scaleValue
    }

    companion object {
        const val MAX_SCALE = 0.9f
        const val MIN_SCALE = 0.8f
    }
}