package com.yc.yyc.mvp.impl

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.luck.picture.lib.entity.LocalMedia
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/30
 * Time: 11:47
 */
interface ReleaseContract {

    interface View : IBaseView {
        fun onSuccess()

    }

    interface Presenter: IPresenter<View> {

        fun onRelease(
            anonymous: Int,
            title: String?,
            content: String?,
            list: ArrayList<LocalMedia>?
        )

    }
}