package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.utils.DataCleanManager
import com.yc.yyc.utils.cache.ShareSessionIdCache
import com.yc.yyc.utils.cache.SharedAccount
import kotlinx.android.synthetic.main.f_set.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 12:32
 *  设置
 */
class SetFrg : BaseFragment(), View.OnClickListener{

    override fun getLayoutId(): Int = R.layout.f_set

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.set))
        tv_clear.setOnClickListener(this)
        tv_about.setOnClickListener(this)
        tv_agreement.setOnClickListener(this)
        btn_commit.setOnClickListener(this)
        tv_clear.text = DataCleanManager.getTotalCacheSize(activity)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_clear ->{
                DataCleanManager.clearAllCache(activity)
                showToast("清除成功")
                tv_clear.text = DataCleanManager.getTotalCacheSize(activity)
            }
            R.id.tv_about ->{
                UIHelper.startHtmlAct(HtmlAct.ABOUT)
            }
            R.id.tv_agreement ->{
                UIHelper.startHtmlAct(HtmlAct.USER_AGREEMENT)
            }
            R.id.btn_commit ->{
                User.getInstance().userObj = null
                User.getInstance().isLogin = false
                ShareSessionIdCache.getInstance(activity).remove()
                SharedAccount.getInstance(activity).remove()
                pop()
            }
        }
    }

}