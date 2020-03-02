package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.ContactContract
import com.yc.yyc.mvp.impl.HtmlContract
import com.yc.yyc.mvp.impl.ThreeContract
import com.yc.yyc.ui.act.HtmlAct
import org.greenrobot.eventbus.EventBus

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:34
 */
class ThreePresenter  : BaseListPresenter<ThreeContract.View>(), ThreeContract.Presenter{
    override fun onSaveStarFollow(userId: String?, followw: Int) {
        var  followw = if (followw == 0) 1 else 0
        val disposable = RetrofitManager.service.starSaveStarFollow(userId, followw)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setFollow(followw)
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

    override fun onIfFollowUser(userId: String?) {
        val disposable = RetrofitManager.service.starIfFollowUser(userId)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        val result = bean.result as Double
                        mRootView?.setFollow(result.toInt())
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

    override fun onRequest(pagerNumber: Int, like: String?) {
        val disposable = RetrofitManager.service.starGetStarList(1, like, pagerNumber)
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

    fun onPraise(position: Int, id: String?, praise: Int) {
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

}