package com.yc.yyc.mvp.presenter

import com.hazz.kotlinmvp.base.BaseListPresenter
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import com.yc.yyc.base.User
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.mvp.impl.ArticleDescContract
import org.greenrobot.eventbus.EventBus

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 10:40
 */
class ArticleDescPresenter : BaseListPresenter<ArticleDescContract.View>(), ArticleDescContract.Presenter{
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
        mRootView?.showLoading()
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
        var praise = if (praise == 0) 1 else 0
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
        val disposable = RetrofitManager.service.promotionGetOriginalityListRandom()
            .compose(SchedulerUtils.ioToMain())
            .subscribe({ bean ->
                mRootView?.apply {
                    mRootView?.hideLoading()
                    if (bean.code == ErrorStatus.SUCCESS){
                        var data = bean.result
                        if (data != null){
                            mRootView?.setBanner(data[0])
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