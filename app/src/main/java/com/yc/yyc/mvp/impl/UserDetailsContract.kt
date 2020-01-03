package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/2
 * Time: 20:31
 */
interface UserDetailsContract {

    interface View : IBaseView {

        fun setSuccess()

    }

    interface Presenter: IPresenter<View> {

        fun onSaveUserInfo(headPath: String?, userNickName: String?, profile: String?, password: String?)

    }
}