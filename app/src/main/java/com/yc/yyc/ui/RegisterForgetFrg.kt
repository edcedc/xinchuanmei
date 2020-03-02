package com.yc.yyc.ui

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.RegisterContract
import com.yc.yyc.mvp.presenter.RegisterPresenter
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.utils.CountDownTimerUtils
import kotlinx.android.synthetic.main.f_reg_for.*
import kotlinx.android.synthetic.main.f_reg_for.btn_commit
import kotlinx.android.synthetic.main.f_reg_for.cb_pwd
import kotlinx.android.synthetic.main.f_reg_for.et_pwd
import kotlinx.android.synthetic.main.f_reg_for.iv_colse
import kotlinx.android.synthetic.main.f_reg_for.tv_agreement

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/29
 * Time: 22:29
 */
class RegisterForgetFrg : BaseFragment(), RegisterContract.View, View.OnClickListener{

    val mPresenter by lazy { RegisterPresenter() }

    var type: Int = 0

    override fun getLayoutId(): Int = R.layout.f_reg_for

    override fun initParms(bundle: Bundle) {
        type = bundle.getInt("type")
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSofia(true)
        when(type){
            1 ->{
                tv_title.text = getString(R.string.yy5)
            }
            2 ->{
                tv_title.text = getString(R.string.yy6)
                btn_commit.text = getText(R.string.sure)
            }
            3, 4 ->{
                tv_title.text = if (type == 4) getString(R.string.yy8) else getString(R.string.yy7)
                btn_commit.text = getText(R.string.sure)
                val userObj = User.getInstance().userObj
                if (userObj != null){
                    et_phone.setText(userObj.optString("phoneNum"))
                    et_phone.setOnClickListener(null)
                    et_phone.setOnClickListener(null)
                    et_phone.isEnabled = false
                    et_phone.isFocusable = false
                }
            }
        }

        iv_colse.setOnClickListener(this)
        tv_code.setOnClickListener(this)
        btn_commit.setOnClickListener(this)
        tv_agreement.setOnClickListener(this)
        cb_pwd.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                //如果选中，显示密码
                et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //否则隐藏密码
                et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
        cb_pwd1.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                //如果选中，显示密码
                et_pwd1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //否则隐藏密码
                et_pwd1.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }

        val colorSpan = ForegroundColorSpan(Color.parseColor("#DF5223"))
        val hText = SpannableString("登录/注册即代表同意《星传媒用户服务协议》")
        hText.setSpan(colorSpan, hText.length - 11, hText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_agreement.setText(hText)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_colse ->{
                pop()
            }
            R.id.btn_commit ->{
                mPresenter.onSure(et_phone.text.toString(), et_code.text.toString(), et_pwd.text.toString(), et_pwd1.text.toString(), type, cb.isChecked)
            }
            R.id.tv_agreement ->{
                UIHelper.startHtmlAct(HtmlAct.REGISTER)
            }
            R.id.tv_code ->{
                mPresenter.onCode(et_phone?.text.toString(),type)}
            }
        }

    override fun setCode() {
        CountDownTimerUtils(activity, 60000, 1000, tv_code).start()
    }

    override fun setSuccess() {
        pop()
    }

}

