package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IListPresenter
import com.yc.yyc.bean.DataBean
import com.yc.yyc.bean.SearchListBean
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/9
 * Time: 16:29
 */
interface SearchContract {

    interface View : IBaseListView {
        fun setHotTabList(data: ArrayList<DataBean>?)
        fun setHistory(all: List<SearchListBean>)
        fun setPraise(position: Int, praise: Int)

    }

    interface Presenter: IListPresenter<View> {

        fun onHotTabList()
        fun onHistory()
        fun onRequest(pagerNumber: Int, searchContent: String?)
        fun onRequestStar(pagerNumber: Int, searchContent: String?)
        fun onPraise(position: Int, id: String?, praise: Int)

    }

}