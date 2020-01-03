package com.yc.yyc.ui.act

import android.content.Intent
import android.os.Bundle
import com.yc.mema.event.CameraInEvent
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.ui.LoginFrg
import org.greenrobot.eventbus.EventBus

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/18
 * Time: 21:00
 *  登录
 */
class LoginAct : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initParms(bundle: Bundle) {
    }

    override fun initView() {
        if (findFragment(LoginFrg::class.java) == null) {
            loadRootFragment(R.id.fl_container, LoginFrg::class.java.newInstance())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(data?.let { CameraInEvent(requestCode, it) })
        }
    }

}
