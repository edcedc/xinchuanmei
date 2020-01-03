package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.ContactContract
import com.yc.yyc.mvp.impl.HtmlContract
import com.yc.yyc.mvp.impl.ThreeContract
import com.yc.yyc.ui.act.HtmlAct

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:34
 */
class ThreePresenter  : BaseListPresenter<ThreeContract.View>(), ThreeContract.Presenter{

    override fun onRequest(pagerNumber: Int) {
        val disposable = RetrofitManager.service.starGetStarList(pagerNumber)
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