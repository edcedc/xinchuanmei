package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter

/**
 * 作者：yc on 2018/9/6.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

interface ThreeContract {

    interface View : IBaseListView {
        fun setPraise(position: Int, praise: Int)
        fun setFollow(i: Int)
    }

    interface Presenter: IListPresenter<View> {

        fun onRequest(pagerNumber: Int, like: String?)
        fun onIfFollowUser(userId: String?)
        fun onSaveStarFollow(userId: String?, followw: Int)

    }

}
