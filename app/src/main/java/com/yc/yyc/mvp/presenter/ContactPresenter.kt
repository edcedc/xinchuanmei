package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.ContactContract
import com.yc.yyc.mvp.impl.HtmlContract
import com.yc.yyc.ui.act.HtmlAct

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:34
 */
class ContactPresenter  : BasePresenter<ContactContract.View>(), ContactContract.Presenter{

    override fun onSaveStarReport(phone: String, content: String) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(content)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_) as String)
            return
        }
        mRootView?.showLoading()
        var disposable = RetrofitManager.service.starSaveStarReport(phone, content)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setSuccess()
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

    override fun onSaveElation(phone: String, content: String) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(content)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_) as String)
            return
        }
        mRootView?.showLoading()
        var disposable = RetrofitManager.service.articleSaveElation(phone, content)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setSuccess()
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