package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.bean.BaseResponseBean
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.RegisterContract
import com.yc.yyc.utils.cache.SharedAccount

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 15:15
 */
class RegisterPresenter : BasePresenter<RegisterContract.View>(), RegisterContract.Presenter{


    override fun onCode(phone: String, type: Int) {
        if (StringUtils.isEmpty(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.please_phone) as  String)
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_phone) as String)
            return
        }
        mRootView?.showLoading()
        var type = if (type == 3) 4 else type
        var disposable = RetrofitManager.service.userGetCode(type, phone)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setCode()
                    }
                    showToast(bean.description as String)
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }
            })

        addSubscription(disposable)
    }

    override fun onSure(
        phone: String,
        code: String,
        pwd: String,
        pwd1: String,
        type: Int,
        checked: Boolean
    ) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code) || StringUtils.isEmpty(pwd) || StringUtils.isEmpty(pwd1)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_) as  String)
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_phone) as String)
            return
        }
        if ((pwd.length < 6 || pwd.length > 10) || (pwd1.length < 6 || pwd1.length > 10)){
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_pwd_length) as  String)
            return
        }
        if (!pwd.equals(pwd1)){
            showToast(MyApplication.mContext?.resources?.getString(R.string.please_pwd2) as String)
            return
        }
        if (!checked){
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_1) as String)
            return
        }
        mRootView?.showLoading()
        when(type){
            1 ->{
                val disposable = RetrofitManager.service.userRegister(phone, code, EncryptUtils.encryptMD5ToString(pwd))
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe({ bean ->
                        mRootView?.apply {
                            setCallback(bean, phone, pwd)
                        }
                    }, { t ->
                        mRootView?.apply {
                            //处理异常
                            mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                        }
                    })

                addSubscription(disposable)
            }
            2 ->{
                val disposable = RetrofitManager.service.userRetrievePwd(phone, code, EncryptUtils.encryptMD5ToString(pwd))
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe({ bean ->
                        mRootView?.apply {
                            setCallback(bean, phone, pwd)
                        }
                    }, { t ->
                        mRootView?.apply {
                            //处理异常
                            mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                        }
                    })

                addSubscription(disposable)
            }
            3 ,4 ->{
                val disposable = RetrofitManager.service.userChangePwd(phone, code, EncryptUtils.encryptMD5ToString(pwd))
                    .compose(SchedulerUtils.ioToMain())
                    .subscribe({ bean ->
                        mRootView?.apply {
                            setCallback(bean, phone, pwd)
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
    }

    private fun setCallback(
        bean: BaseResponseBean<Object>,
        phone: String,
        pwd: String
    ) {
        mRootView?.hideLoading()
        if (bean.code == ErrorStatus.SUCCESS) {
            SharedAccount.getInstance(MyApplication.mContext).save(phone, pwd)
            mRootView?.setSuccess()
        }
        showToast(bean.description as String)
    }

}
