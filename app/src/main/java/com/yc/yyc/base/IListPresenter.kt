package com.hazz.kotlinmvp.base

import android.app.Activity


/**
 * @author Jake.Ho
 * created: 2017/10/25
 * desc: Presenter 基类
 */


interface IListPresenter<in V: IBaseListView> {

    fun init(mRootView: V)

    fun detachView()

}
