package com.yc.yyc.mvp.presenter

import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.HtmlContract
import com.yc.yyc.ui.act.HtmlAct

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:34
 */
class HtmlPresenter  : BasePresenter<HtmlContract.View>(), HtmlContract.Presenter{

    override fun onUrl(type: Int) {
        informationAgreement(type)
    }

    fun informationAgreement(type: Int) {
        var disposable = RetrofitManager.service.agreeGetAgreeList()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    if (bean.code == ErrorStatus.SUCCESS){
                        val data = bean.result
                        when(type){
                            HtmlAct.ABOUT ->{
                                val dataBean = data!![2]
                                mRootView?.setUrl(MyApplication.mContext.resources.getString(R.string.about), dataBean?.context)
                            }
                            HtmlAct.USER_AGREEMENT ->{
                                val dataBean = data!![0]
                                mRootView?.setUrl(MyApplication.mContext.resources.getString(R.string.yy10), dataBean?.context)
                            }
                            HtmlAct.REGISTER ->{
                                val dataBean = data!![4]
                                mRootView?.setUrl("发布协议", dataBean?.context)
                            }
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

}