package com.yc.yyc.mvp.presenter

import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.bean.SearchListBean
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.CollectContract
import com.yc.yyc.mvp.impl.SearchContract
import com.yc.yyc.ui.act.HtmlAct
import org.litepal.LitePal

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/9
 * Time: 16:31
 */
class SearchPresenter  : BaseListPresenter<SearchContract.View>(), SearchContract.Presenter{

    override fun onPraise(position: Int, id: String?, praise: Int) {
        var praise = if (praise == 0) 1 else 0
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.starSaveStarPraise(id, praise)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setPraise(position, praise)
                    }
                    showToast(bean.description)
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }
    override fun onRequestStar(pagerNumber: Int, searchContent: String?) {
        val disposable = RetrofitManager.service.starGetStarList(1, searchContent, pagerNumber)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        val list = data?.content
                        if (list != null){
                            mRootView?.setData(list as Object)
                        }
                        mRootView?.setRefreshLayoutMode(data?.totalCount as Int)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }

    override fun onRequest(pagerNumber: Int, searchContent: String?) {
        val disposable = RetrofitManager.service.articleGetArticleList(searchContent,null, pagerNumber, null)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        val list = data?.content
                        if (list != null){
                            mRootView?.setData(list as Object)
                        }
                        mRootView?.setRefreshLayoutMode(data?.totalCount as Int)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }

    override fun onHistory() {
        val all = LitePal.findAll(SearchListBean::class.java)
        if (all != null && all!!.size != 0) {
            mRootView?.setHistory(all)
        }
    }

    override fun onHotTabList() {
        var disposable = RetrofitManager.service.articleGetHotTabList()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == ErrorStatus.SUCCESS){
                        val data = bean.result
                       mRootView?.setHotTabList(data)
                    }
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

}