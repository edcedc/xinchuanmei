package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/7
 * Time: 17:26
 */
interface FollowUserContract {

    interface View : IBaseListView {
        fun setFollow(position: Int)
    }

    interface Presenter: IListPresenter<View> {

        fun onRequest(pagerNumber: Int)
        fun onFollow(position: Int, userId: String?, praise: Int)

    }

}