package com.yc.yyc.mvp.presenter

import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.LogUtils
import com.google.gson.Gson
import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.base.User
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.mvp.impl.ArticleDetailsContract
import okhttp3.MediaType
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.util.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 10:40
 */
class ArticleDescPresenter : BaseListPresenter<ArticleDetailsContract.View>(), ArticleDetailsContract.Presenter{

    override fun onBlockchain(articleId: String?, url: String?) {
        if (articleId.equals("1")){
            val disposable = RetrofitManager.service.htmltextIndex("http://api.tianapi.com/txapi/toutiaotxt/index", "fc6a81bedcacffd60b6708dac0a6ffa7", url.toString())
                .compose(SchedulerUtils.ioToMain())
                .subscribe({ bean ->
                    mRootView?.apply {
                        huilai(bean)
                    }
                }, { t ->
                    mRootView?.apply {
                        //处理异常
                        mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                    }

                })
            addSubscription(disposable)
        }else{
            val disposable = RetrofitManager.service.blockchainIndex("http://api.tianapi.com/txapi/htmltext/index", "fc6a81bedcacffd60b6708dac0a6ffa7", url.toString())
                .compose(SchedulerUtils.ioToMain())
                .subscribe({ bean ->
                    mRootView?.apply {
                        huilai(bean)
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

    private fun huilai(obj :Object){
        mRootView?.hideLoading()
        val jsonString = Gson().toJson(obj)
        val bean = JSONObject(jsonString)
        if (bean.optInt("code") == 200){
            val newslist = bean.optJSONArray("newslist")
            if (newslist != null && newslist.length() != 0){
                val obj = newslist.optJSONObject(0)
                mRootView!!.setDesc(obj)
            }
        }
    }

    override fun onSaveDiscussReport(id: String?) {
        val disposable = RetrofitManager.service.articleSaveDiscussReport(id)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){

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

    override fun onSaveHistory(articleId: String?) {
        if (User.getInstance().isLogin == false)return
        val disposable = RetrofitManager.service.articlesaveHistory(articleId)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {

                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }

    override fun onDelMyDiscuss(position: Int, id: String) {
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.articleDelMyDiscuss(id)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setDelMyDiscuss(position)
                    }
//                    showToast(bean.description)
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }

    override fun onSaveArticleDispra(
        position: Int,
        articleId: String?,
        praise: Int,
        discussId: String?
    ) {
//        mRootView?.showLoading()
        val disposable = RetrofitManager.service.articleSaveArticleDispra(discussId, praise)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setSaveArticleDispra(position, praise)
                    }
//                    showToast(bean.description)
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }

    override fun onSaveArticleDiscuss(
        i: Int,
        articleId: String?,
        text: String?,
        sheContent: String?,
        sheUserId: String?
    ) {
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.articleSaveArticleDiscuss(articleId, text, sheContent, sheUserId)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        val result = bean.result
                        mRootView?.setDiscuss(i, result)
                    }
//                    showToast(bean.description)
                }
            }, { t ->
                mRootView?.apply {
                    //处理异常
                    mRootView?.errorText(ExceptionHandle.handleException(t), ExceptionHandle.errorCode)
                }

            })
        addSubscription(disposable)
    }


    override fun onPraise(articleId: String?, praise: Int) {
        var praise = if (praise == 1) 2 else 1
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.articleArticlePraise(articleId, praise)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setPraise(praise)
                        mRootView?.setPraiseNum(praise)
                        EventBus.getDefault().post(ArticleInEvent(articleId, -1, praise))
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

    override fun onCollect(articleId: String?, cIsTrue: Int) {
        var cIsTrue = if (cIsTrue == 0) 1 else 0
        mRootView?.showLoading()
        val disposable = RetrofitManager.service.articleArticleCollect(articleId, cIsTrue)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        mRootView?.setCollect(cIsTrue)
                        EventBus.getDefault().post(ArticleInEvent(articleId, cIsTrue, -1))
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

    override fun onBanner() {
        val disposable = RetrofitManager.service.promotionGetOriginalityRandom()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
//                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        if (data != null) {
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

    override fun onRequest(id: String?, page: Int) {
        val disposable = RetrofitManager.service.articleGetArticleDiscussesList(id, page)
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
//                    hideLoading()
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

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), value)
    }

}