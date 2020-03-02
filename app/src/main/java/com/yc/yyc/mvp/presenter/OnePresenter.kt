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
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        if (data != null ){
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

     private var strs = arrayOf("区块链", "头条", "币圈", "军事", "影视", "人工智能", "娱乐", "体育", "房产")
     private var ids = arrayOf("28", "1", "3", "27", "2", "29", "10", "12", "37")

//    分类 今日头条1 影视资讯2 财经新闻32，军事新闻27，区块链28，人工智能29，房产资讯37，娱乐新闻10，体育新闻12

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
        val disposable = RetrofitManager.service.articleGetArticleList(null, id, page, null)
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