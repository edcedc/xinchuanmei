package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter
import com.yc.yyc.bean.DataBean

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 10:40
 */
interface ArticleDescContract {

    interface View : IBaseListView {

        fun setBanner(bean: DataBean)
        fun setCollect(cIsTrue: Int)
        fun setPraise(praise: Int)
        fun setPraiseNum(praise: Int)
        fun setDiscuss(i: Int, result: DataBean?)
        fun setSaveArticleDispra(position: Int, praise: Int)
        fun setDelMyDiscuss(position: Int)
    }

    interface Presenter: IListPresenter<View> {

        fun onRequest(id: String?, page: Int)
        fun onBanner()
        fun onCollect(articleId: String?, cIsTrue: Int)
        fun onPraise(articleId: String?, praise: Int)
        fun onSaveArticleDiscuss(
            i: Int,
            articleId: String?,
            text: String?,
            sheContent: String?,
            sheUserId: String?
        )

        fun onSaveArticleDispra(
            position: Int,
            articleId: String?,
            praise: Int,
            discussId: String?
        )
        fun onDelMyDiscuss(position: Int, id: String)
        fun onSaveHistory(articleId: String?)
        fun onSaveDiscussReport(id: String?)

    }

}