package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.weight.GlideLoadingUtils
import kotlinx.android.synthetic.main.f_five.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:40
 */
class FiveFrg : BaseFragment(), View.OnClickListener{


    override fun getLayoutId(): Int = R.layout.f_five

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setSwipeBackEnable(false)
        iv_msg.setOnClickListener(this)
        iv_set.setOnClickListener(this)
        iv_head.setOnClickListener(this)
        tv_collect.setOnClickListener(this)
        tv_follow.setOnClickListener(this)
        tv_history.setOnClickListener(this)
        tv_about.setOnClickListener(this)
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        setSofia(true)
        val userObj = User.getInstance().userObj
        if (userObj != null){
            tv_name.text = userObj.optString("userNickName")
            GlideLoadingUtils.load(activity, userObj.optString("head"), iv_head, true)
        }else{
            iv_head.setImageResource(R.mipmap.y51)
            tv_name.text = getText(R.string.login_register)
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_head ->{
                if (User.getInstance().isLogin){
                    UIHelper.startUserDetailsFrg(this)
                }else{
                    UIHelper.startLoginAct()
                }
            }
            R.id.tv_collect ->{
                if (!(activity as BaseActivity).isLogin())return
                UIHelper.startCollectFrg(this)
            }
            R.id.iv_set ->{
                UIHelper.startSetFrg(this)
            }
            R.id.iv_msg ->{
                UIHelper.startNoticelFrg(this)
            }
            R.id.tv_follow ->{

            }
            R.id.tv_history ->{
                UIHelper.startHistoryFrg(this)
            }
            R.id.tv_about ->{
                UIHelper.startContactFrg(this)
            }
        }
    }

}