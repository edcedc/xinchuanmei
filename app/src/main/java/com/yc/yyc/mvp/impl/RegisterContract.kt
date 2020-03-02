package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 15:12
 */
interface RegisterContract {

    interface View : IBaseView {
         fun setCode()
        fun setSuccess()
    }

    interface Presenter: IPresenter<View> {

        fun onCode(phone: String, type: Int)

        fun onSure(
            phone: String,
            code: String,
            pwd: String,
            pwd1: String,
            type: Int,
            checked: Boolean
        )

    }

}