package com.yzy.module_base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter

import com.yzy.lib_common.ext.clickNoRepeat
import com.yzy.lib_common.ext.getResColor
import com.yzy.lib_common.ext.visible
import com.yzy.lib_common.widget.BaseXRefreshLayout
import com.yzy.lib_common.widget.MultiStatePage
import com.yzy.module_base.R
import com.yzy.module_base.widget.state.showEmpty
import com.yzy.module_base.widget.state.showError
import com.yzy.module_base.widget.state.showLoading


/**
 * Created by yzy on 2020/12/11.
 */
class XRefreshLayout<T>(context: Context?, attrs: AttributeSet?) :
    BaseXRefreshLayout(context, attrs) {

    private var emptyBg = R.color.color_bg.getResColor()
    private var errorBg =  R.color.color_bg.getResColor()

    private var isClick=true


    var statePageTopMarge: Int? = null


     val emptyView: View by lazy {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_empty, null)
        view.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
    private val errorView: View by lazy {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_error, null)
        view.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }
    private val loadingView: View by lazy {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_loading, null)
        view.apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun <T> refreshList(newsList: MutableList<T>?, hasMoreData: Boolean?) {
        hideLoading()
        super.refreshList(newsList, hasMoreData)
    }

    public override fun showLoading() {

        if (isUseLoadService()) {
            loadService?.showLoading(loadingMsg)
        } else {
            recyclerView?.let {
                val adapter = recyclerView?.adapter as BaseQuickAdapter<T, *>
                adapter.setNewInstance(null)
                loadingView.let {
                    adapter.setEmptyView(it)
                }
                loadingView.findViewById<TextView>(R.id.tv_loading).text = loadingMsg
                val progress = loadingView.findViewById<ProgressBar>(R.id.animation_view)
                progress.visible()
                statePageTopMarge?.let {
                    loadingView.findViewById<LinearLayout>(R.id.ll_loading).gravity =
                        Gravity.CENTER_HORIZONTAL
                    val p = progress.layoutParams as LinearLayout.LayoutParams
                    p.topMargin = it
                    progress.layoutParams = p
                }
            }
        }
    }

    override fun showEmpty() {

        if (isUseLoadService()) {
            loadService?.showEmpty(emptyMsg, emptyIcon, emptyBg)
        } else {
            recyclerView?.let {
                val adapter = recyclerView?.adapter as BaseQuickAdapter<T, *>
                adapter.setNewInstance(null)
                emptyView.let {
                    adapter.setEmptyView(it)
                }

                val imageView = emptyView.findViewById<ImageView>(R.id.empty_img)
                emptyView.findViewById<TextView>(R.id.empty_text).text = emptyMsg
                imageView.setImageResource(emptyIcon)
                emptyView.findViewById<ViewGroup>(R.id.empty_bg).setBackgroundColor(emptyBg)


                statePageTopMarge?.let {
                    emptyView.findViewById<LinearLayout>(R.id.empty_bg).gravity =
                        Gravity.CENTER_HORIZONTAL
                    val p =
                        imageView.layoutParams as LinearLayout.LayoutParams
                    p.topMargin = it
                    imageView.layoutParams = p
                }
                hideLoading()
                emptyView.clickNoRepeat {
                    if (isClick) {
                        showLoading()
                        onRefreshData()
                    }
                }
            }
        }


    }
    fun hideLoading(){
        if (!isUseLoadService()){
           /* loadingView.findViewById<ProgressBar>(R.id.animation_view)?.let {
                if (it.isAnimating){
                    it.visibility=View.GONE
                }
            }*/
        }

    }

    override fun showError() {

        if (isUseLoadService()) {
            loadService?.showError(errorMsg, errorIcon, errorBg)
        } else {
            recyclerView?.let {
                val adapter = recyclerView?.adapter as BaseQuickAdapter<T, *>
                adapter.setNewInstance(null)
                errorView.let {
                    adapter.setEmptyView(it)
                }
                val imageView = errorView.findViewById<ImageView>(R.id.error_img)
                errorView.findViewById<TextView>(R.id.error_text).text = errorMsg
                imageView.setImageResource(errorIcon)
                errorView.findViewById<ViewGroup>(R.id.error_bg).setBackgroundColor(errorBg)

                statePageTopMarge?.let {
                    errorView.findViewById<LinearLayout>(R.id.error_bg).gravity =
                        Gravity.CENTER_HORIZONTAL
                    val p =
                        imageView.layoutParams as LinearLayout.LayoutParams
                    p.topMargin = it
                    imageView.layoutParams = p
                }
               hideLoading()
                errorView.clickNoRepeat {
                    if (isClick){
                        showLoading()
                        onRefreshData()
                    }
                }

            }
        }


    }

    fun setEmptyPageData(
        emptyImg: Int = MultiStatePage.config.emptyIcon,
        emptyText: String = MultiStatePage.config.emptyMsg,
        emptyBgColor: Int = R.color.color_bg.getResColor(),
        isClick:Boolean=true,
    ) {
        this.emptyMsg = emptyText
        this.emptyIcon = emptyImg
        this.emptyBg = emptyBgColor
        this.isClick=isClick
    }

    fun setErrorPageData(
        errorIcon: Int = MultiStatePage.config.errorIcon,
        errorMsg: String = MultiStatePage.config.errorMsg,
        errorBgColor: Int = Color.WHITE
    ) {
        this.errorMsg = errorMsg
        this.errorIcon = errorIcon
        this.errorBg = errorBgColor
    }

    fun setLoadingPageData(loadingMsg: String = MultiStatePage.config.loadingMsg) {
        this.loadingMsg = loadingMsg
    }

}