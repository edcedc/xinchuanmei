package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.base.User
import com.yc.yyc.mvp.impl.UserDetailsContract
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.HashMap

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/2
 * Time: 20:31
 */
class UserDetailsPresenter : BasePresenter<UserDetailsContract.View>(), UserDetailsContract.Presenter{

    override fun onSaveUserInfo(head: String?, userNickName: String?, profile: String?, password: String?) {
        val map = HashMap<String, RequestBody>()
        if (head != null){
            val create = RequestBody.create(MediaType.parse("image/png"), File(head))
            map?.put("file" +
                    "\";filename=\"" + File(head).name, create)
        }
        if (!StringUtils.isEmpty(userNickName)){
            map?.put("userNickName", toRequestBody(userNickName as String))
        }
        if (!StringUtils.isEmpty(profile)){
            map?.put("profile", toRequestBody(profile as String))
        }
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.userUpdate(map)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ json ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    val bean = JSONObject(json.string())
                    if (bean.optInt("code") == ErrorStatus.SUCCESS) {
                        val data = bean.optJSONObject("result")
                        User.getInstance().userObj = data
                        mRootView?.setSuccess()
                    }else{
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

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

}