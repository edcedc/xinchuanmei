package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.R
import com.yc.yyc.base.User
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.LoginContract
import com.yc.yyc.utils.cache.ShareSessionIdCache
import com.yc.yyc.utils.cache.SharedAccount
import org.json.JSONObject

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/19
 * Time: 17:05
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    override fun onCode(phone: String) {
        if (StringUtils.isEmpty(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.please_phone) as  String)
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_phone) as String)
            return
        }
        mRootView?.showLoading()
        var disposable = RetrofitManager.service.userGetCode(16, phone)
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

    override fun onPwdLogin(phone: String, pwd: String) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(pwd)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_) as String)
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_phone) as String)
            return
        }
        if ((pwd.length < 6 || pwd.length > 10)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_pwd_length) as String)
            return
        }
        val disposable = RetrofitManager.service.userPLogin(phone, EncryptUtils.encryptMD5ToString(pwd))
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ json ->
                mRootView?.apply {
                    val bean = JSONObject(json.string())
                    if (bean.optInt("code") == ErrorStatus.SUCCESS) {
                        val data = bean.optJSONObject("result")
                        val user = data.optJSONObject("user")
                        User.getInstance().userObj = user
                        User.getInstance().isLogin = true
                        SharedAccount.getInstance(Utils.getApp()).save(phone, pwd)
                        mRootView?.setSuccess()
                    }else{
                        mRootView?.errorText(null, ExceptionHandle.errorCode)
                        showToast(bean.optString("description"))
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

    override fun onCodeLogin(phone: String, code: String) {
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_) as String)
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            showToast(MyApplication.mContext?.resources?.getString(R.string.error_phone) as String)
            return
        }
        val disposable = RetrofitManager.service.userNumLogin(phone, code)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ json ->
                mRootView?.apply {
                    val bean = JSONObject(json.string())
                    if (bean.optInt("code") == ErrorStatus.SUCCESS) {
                        val data = bean.optJSONObject("result")
                        val user = data.optJSONObject("user")
                        User.getInstance().userObj = user
                        User.getInstance().isLogin = true
                        mRootView?.setSuccess()
                    }
                    showToast(bean.optString("description"))
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