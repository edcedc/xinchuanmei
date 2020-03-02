package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter
import com.yc.yyc.bean.DataBean

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/6
 * Time: 14:08
 */
interface RecommendContract {
    interface View : IBaseListView {

        fun setBanner(list: ArrayList<DataBean>)
        fun setArticlePraise(position: Int, praise: Int)

    }

    interface Presenter: IListPresenter<View> {

        fun onRequest(page: Int, type: Int)
        fun onBanner()
        fun onArticlePraise(position: Int, articleId: String?, praise: Int)

    }
}