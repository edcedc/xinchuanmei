package com.yc.yyc

import android.content.Intent
import android.os.Bundle
import com.yc.mema.event.CameraInEvent
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.ui.MainFrg
import org.greenrobot.eventbus.EventBus

class MainActivity : BaseActivity(){

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        if (findFragment(MainFrg::class.java) == null) {
            loadRootFragment(R.id.fl_container, MainFrg::class.java.newInstance())
        }
    }

    override fun initParms(bundle: Bundle) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            EventBus.getDefault().post(data?.let { CameraInEvent(requestCode, it) })
        }
    }

}
