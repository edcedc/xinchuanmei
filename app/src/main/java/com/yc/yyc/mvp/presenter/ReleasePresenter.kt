package com.yc.yyc.mvp.presenter

import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.luck.picture.lib.entity.LocalMedia
import com.yc.yyc.R
import com.yc.yyc.mar.MyApplication
import com.yc.yyc.mvp.impl.ReleaseContract
import com.yc.yyc.utils.cache.ShareSessionIdCache
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.ArrayList
import java.util.HashMap

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/30
 * Time: 11:47
 */
class ReleasePresenter : BasePresenter<ReleaseContract.View>(), ReleaseContract.Presenter {

    override fun onRelease(anonymous: Int, title: String?, content: String?, localMediaList: ArrayList<LocalMedia>?) {
        if (StringUtils.isEmpty(content) && localMediaList?.size == 0) {
            showToast(MyApplication.mContext?.getString(R.string.error_) as String)
            return
        }
        val map = HashMap<String, RequestBody>()
        map.put("anonymous", toRequestBody(anonymous.toString()))
        map.put("content", toRequestBody(content as String))
        for (i in localMediaList!!.indices) {
            val create = RequestBody.create(MediaType.parse("image/png"), File(localMediaList[i].compressPath))
            map?.put(
                "files" +
                        "\";filename=\"" + File(localMediaList!![i].path).name, create
            )
        }

        mRootView?.showLoading()
        val disposable = RetrofitManager.service.starSaveStar(map)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS) {
                        mRootView?.onSuccess()
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

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

}