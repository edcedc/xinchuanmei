package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/6
 * Time: 11:34
 */
interface TwoContract {

    interface View : IBaseListView {

    }

    interface Presenter: IListPresenter<View> {

    }
}