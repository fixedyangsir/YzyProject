package com.yzy.lib_common.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.constant.RefreshState

import com.yzy.lib_common.R
import com.yzy.lib_common.ext.init


/**
 * Created by yzy on 2020/8/5.
 */
abstract class BaseXRefreshLayout(context: Context?, attrs: AttributeSet?) :
    SmartRefreshLayout(context, attrs) {

    companion object {

        const val PAGE_SIZE = 10
        const val FIRST_PAGENO = 1
    }

    var recyclerView: RecyclerView? = null


    var needRefresh: Boolean = true
    var needLoadMore: Boolean = false


    var xRefreshListener: XRefreshListener?=null

    /**
     * 起始页位置
     */
    private var firstPageNo = FIRST_PAGENO

    /**
     * 当前页码
     */
    var pageNo = firstPageNo

    /**
     * 页面大小
     */
    private var pageSize = PAGE_SIZE

    /**
     * 完成首次加载
     */
    var isDoRefresh = false

    /**
     * 是否显示错误界面
     */
    private var needShowErrorPage = true

    /**
     * 是否显示加载界面
     */
    private var needShowLoadingPage = true


    protected var loadService: LoadService<*>? = null


    protected var emptyMsg = MultiStatePage.config.emptyMsg
    protected var emptyIcon = MultiStatePage.config.emptyIcon

    protected var loadingMsg = MultiStatePage.config.loadingMsg

    protected var errorMsg = MultiStatePage.config.errorMsg
    protected var errorIcon = MultiStatePage.config.errorIcon

    protected var forceUseLoadService = false


    @SuppressLint("RestrictedApi")
    fun init(

        recyclerView: RecyclerView? = null,
        contentView: ViewGroup? = null,
        adapter: BaseQuickAdapter<*, *>? = null,
        needRefresh: Boolean = true,
        needLoadMore: Boolean = false,
        needShowErrorPage: Boolean = true,
        needShowLoadingPage: Boolean = true,
        rvLayoutManager: RecyclerView.LayoutManager? = null,
        rvAnimator: RecyclerView.ItemAnimator? = null,
        decoration: RecyclerView.ItemDecoration? = null,
        firstPageNo: Int = FIRST_PAGENO,
        pageSize: Int = PAGE_SIZE,
        refreshHeader: RefreshHeader = MaterialHeader(context),
        refreshFooter: RefreshFooter = BallPulseFooter(context),
        forceUseLoadService: Boolean = false,
        xRefreshListener: XRefreshListener?=null

    ) {
        setReboundDuration(500)
        this.xRefreshListener=xRefreshListener


        adapter?.headerWithEmptyEnable = true



        setRefreshHeader(refreshHeader)
        setRefreshFooter(refreshFooter)

        setEnableNestedScroll(true)
        setEnableLoadMore(needLoadMore)
        setEnableAutoLoadMore(true)
        setEnableOverScrollBounce(false)
        setEnableOverScrollDrag(false)
        setEnableRefresh(needRefresh)
        setDisableContentWhenRefresh(false) //是否在刷新的时候禁止列表的操作

        setOnRefreshListener {
            onRefreshData()

        }
        setOnLoadMoreListener {
            if (isRefreshingData()){
                finishLoadMore(0)
                return@setOnLoadMoreListener
            }
            xRefreshListener?.onLoadMore(pageNo)
        }


        refreshFooter.setPrimaryColors(
            context.getColor(R.color.colorAccent),
            context.getColor(R.color.colorAccent)
        )

        this.needLoadMore = needLoadMore
        this.needRefresh = needRefresh
        this.needShowErrorPage = needShowErrorPage
        this.needShowLoadingPage = needShowLoadingPage
        this.firstPageNo = firstPageNo
        this.pageNo = firstPageNo
        this.pageSize = pageSize
        this.forceUseLoadService = forceUseLoadService
        recyclerView?.let {
            this.recyclerView =
                recyclerView.init(adapter, rvLayoutManager, rvAnimator, decoration)
        }

        contentView?.let {
            loadService = LoadSir.getDefault().register(it) {
                isDoRefresh = false
                onRefreshData()
            }
        }


    }

    fun isUseLoadService(): Boolean {
        if (forceUseLoadService) {
            return true
        }
        recyclerView?.let {
            return false
        }
        return true
    }
    var isRefreshLoading=false
    /**
     * 刷新数据
     */
    fun onRefreshData() {
        if (isRefreshLoading){
            finishRefresh(0)
            return
        }
        if (!isDoRefresh && needShowLoadingPage) {
            showLoading()
        } else {
            loadService?.showSuccess()
        }

        reSetRefreshingState()
        xRefreshListener?.onRefresh()
    }


    /**
     * 是否是刷新数据操作
     */
    fun isRefreshingData(): Boolean {
        return pageNo == firstPageNo
    }

    fun reSetRefreshingState() {
        isRefreshLoading=true
        pageNo = firstPageNo
        setEnableLoadMore(needLoadMore)
        resetNoMoreData()

    }

    fun onFinish() {
        isDoRefresh = true
        finishLoadMore(0)
        finishRefresh(0)
        loadService?.showSuccess()
        isRefreshLoading = false
    }

    fun onError() {
        if (recyclerView == null) return
        isDoRefresh = true
        finishLoadMore(0)
        finishRefresh(0)
        if (needShowErrorPage) {
            showError()
        } else {
            loadService?.showSuccess() ?: showError()
        }
        isRefreshLoading=false
    }

    /**
     * 设置list数据
     * @param data
     */
    fun <T> setListData(data: MutableList<T>?, hasMoreData: Boolean? = null) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        if (!needLoadMore || this.pageNo == firstPageNo) {
            isDoRefresh = true
            refreshList(data, hasMoreData)
        } else {
            loadMoreList(data, hasMoreData)
        }
    }

    fun <T> addData(data: T?) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>
        data?.let {
            adapter.addData(data)
        }
    }

    fun <T> addDatas(list: MutableList<T>?) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>
        list?.let {
            adapter.addData(list)
        }

    }

    fun <T> removeData(data: T?) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>
        data?.let {
            adapter.remove(data)
            if (adapter.data.size == 0) {
                showEmpty()
            }
        }
    }

    fun <T> removeData(position: Int) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>

        adapter.removeAt(position)
        if (adapter.data.size == 0) {
            showEmpty()
        }

    }

    private fun <T> loadMoreList(list: MutableList<T>?, hasMoreData: Boolean?) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>

        if (!list.isNullOrEmpty()) {
            pageNo++
            adapter?.addData(list)
        }
        if (hasMoreData != null) {
            //优先以这个参数为主
            if ((list.isNullOrEmpty() || !hasMoreData)) {
                if (mRefreshFooter is ClassicsFooter){
                    finishLoadMoreWithNoMoreData()
                }else{
                    this.finishLoadMore(0)
                }
                setEnableLoadMore(false)

             //   this.finishLoadMore(0)
            }else{

                this.finishLoadMore(0)
            }
            return
        }
        if ((list.isNullOrEmpty() || list.size < pageSize)) {
            if (mRefreshFooter is ClassicsFooter){
                finishLoadMoreWithNoMoreData()
            }else{
                this.finishLoadMore(0)
            }
            setEnableLoadMore(false)

        }else{

            this.finishLoadMore(0)
        }
    }

    open fun <T> refreshList(newsList: MutableList<T>?, hasMoreData: Boolean? = null) {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return
        }
        val adapter = recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>

        this.finishRefresh(0)


        if (newsList.isNullOrEmpty()) {
            showEmpty()
        } else {
            pageNo++
            adapter?.setList(newsList)
            loadService?.showSuccess()
        }
        isRefreshLoading = false
        //判断是否有分页

        if (!needLoadMore) {
            return
        }
        if (hasMoreData != null) {
            //优先以这个参数为主
            if ((newsList.isNullOrEmpty() || !hasMoreData)) {

                if (mRefreshFooter is ClassicsFooter){
                    finishLoadMoreWithNoMoreData()
                }else{
                    this.finishLoadMore(0)
                }
                setEnableLoadMore(false)
            }else{

                this.finishLoadMore(0)
            }
            return
        }
        if ((newsList.isNullOrEmpty() || newsList.size < pageSize)) {

            if (mRefreshFooter is ClassicsFooter){
                finishLoadMoreWithNoMoreData()
            }else{
                this.finishLoadMore(0)
            }
            setEnableLoadMore(false)
        }else{
            this.finishLoadMore(0)
        }


    }

    fun <T> getAdapter(): BaseQuickAdapter<T, BaseViewHolder>? {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return null
        }
        return recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>
    }

    fun <T> getListDatas(): MutableList<T>? {
        if (recyclerView == null || recyclerView?.adapter == null) {
            return mutableListOf()
        }
        return (recyclerView?.adapter as BaseQuickAdapter<T, BaseViewHolder>).data
    }

    fun  setDefaultPageSize(size:Int){
        this.pageSize=size
    }

    protected abstract fun showLoading()
    protected abstract fun showEmpty()
    protected abstract fun showError()


    interface XRefreshListener{
        fun onRefresh()
        fun onLoadMore(pageNo:Int)

    }

}