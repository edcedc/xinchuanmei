package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:33
 */
interface ContactContract{

    interface View : IBaseView {

    }

    interface Presenter: IPresenter<View> {

        fun onSaveElation(phone : String, content: String)

    }

}
