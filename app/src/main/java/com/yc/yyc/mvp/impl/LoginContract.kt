package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/19
 * Time: 17:02
 */
interface LoginContract{

    interface View : IBaseView{

        fun setCode()
        fun setSuccess()

    }

    interface Presenter: IPresenter<View> {

        fun onPwdLogin(phone : String, pwd : String)

        fun onCodeLogin(phone : String, code : String)

        fun onCode(phone : String)

    }

}