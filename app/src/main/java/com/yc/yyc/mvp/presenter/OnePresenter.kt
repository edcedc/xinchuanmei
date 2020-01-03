package com.yc.yyc.mvp.presenter

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.OneContract
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/25
 * Time: 11:46
 */
class OnePresenter : BaseListPresenter<OneContract.View>(), OneContract.Presenter{

    override fun onArticlePraise(position: Int, articleId: String?, praise: Int) {
        val disposable = RetrofitManager.service.articleArticlePraise(articleId, praise)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setArticlePraise(position, praise)
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

    override fun onNotice() {
        val disposable = RetrofitManager.service.noticeGetFiveNewNotice()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        if (data != null){
                            mRootView?.setNotice(data)
                        }
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

    override fun onBanner() {
        val disposable = RetrofitManager.service.promotionGetOriginalityListRandom()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        if (data != null){
                            mRootView?.setBanner(data)
                        }
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

     private var strs = arrayOf("影视资讯", "今日头条", "财经新闻", "军事新闻", "区块链", "人工智能",
        "房产资讯", "娱乐新闻", "体育新闻")
     private var ids = arrayOf("2", "1", "32", "27", "28", "29", "37", "10", "12")

    override fun onLabel() {
        val list = ArrayList<DataBean>()
        for (i in strs.indices) {
            var name = strs[i]
            var id = ids[i]
            val bean = DataBean()
            bean.id = id
            bean.name = name
            list.add(bean)
        }
        mRootView?.setLabel(list)
    }

    override fun onRequest(id: String?, page: Int) {
        val disposable = RetrofitManager.service.articleGetArticleList(id, page)
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

}