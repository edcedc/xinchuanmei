package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseListView
import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IListPresenter
import com.hazz.kotlinmvp.base.IPresenter
import com.yc.yyc.bean.DataBean
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/25
 * Time: 11:46
 */
interface OneContract {

    interface View : IBaseListView {

        fun setLabel(list: ArrayList<DataBean>)
        fun setBanner(list: ArrayList<DataBean>)
        fun setNotice(data: ArrayList<DataBean>)
        fun setArticlePraise(position: Int, praise: Int)

    }

    interface Presenter: IListPresenter<View> {

        fun onRequest(id: String?, page: Int)
        fun onLabel()
        fun onBanner()
        fun onNotice()
        fun onArticlePraise(position: Int, articleId: String?, praise: Int)

    }

}