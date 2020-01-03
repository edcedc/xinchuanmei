package com.yc.yyc.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.LoginContract
import com.yc.yyc.mvp.presenter.LoginPresenter
import com.yc.yyc.ui.act.LoginAct
import com.yc.yyc.utils.CountDownTimerUtils
import com.yc.yyc.utils.TabEntity
import com.yc.yyc.utils.cache.ShareSessionIdCache
import com.yc.yyc.utils.cache.SharedAccount
import kotlinx.android.synthetic.main.f_login.*
import kotlinx.android.synthetic.main.f_login.btn_commit
import kotlinx.android.synthetic.main.f_login.cb_pwd
import kotlinx.android.synthetic.main.f_login.et_phone
import kotlinx.android.synthetic.main.f_login.et_pwd
import kotlinx.android.synthetic.main.f_login.tv_agreement
import kotlinx.android.synthetic.main.f_login.tv_code
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:44
 */
class LoginFrg : BaseFragment(), LoginContract.View, View.OnClickListener{


    var mPosition: Int = 0

    val mPresenter by lazy { LoginPresenter() }

    override fun getLayoutId(): Int = R.layout.f_login

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSofia(true)
        btn_commit.setOnClickListener(this)
        tv_forget_pwd.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        tv_code.setOnClickListener(this)
        iv_colse.setOnClickListener(this)
        val mTabEntities = ArrayList<CustomTabEntity>()
        val strings = arrayOf(getString(R.string.login1), getString(R.string.login2))
        for (s in strings) {
            mTabEntities.add(TabEntity(s))
        }
        tb_layout.setTabData(mTabEntities)
        tb_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                mPosition = position
                if (position == 0) {
                    et_phone.visibility = View.VISIBLE
                    ly_code.visibility = View.VISIBLE
                    et_phone_account.visibility = View.GONE
                    ly_phone.visibility = View.VISIBLE
                    ly_pwd.visibility = View.GONE
                    tv_forget_pwd.visibility = View.GONE
                } else {
                    et_phone.visibility = View.GONE
                    ly_code.visibility = View.GONE
                    et_phone_account.visibility = View.VISIBLE
                    ly_phone.visibility = View.GONE
                    ly_pwd.visibility = View.VISIBLE
                    tv_forget_pwd.visibility = View.VISIBLE
                }
            }

            override fun onTabReselect(position: Int) {}
        })

        cb_pwd.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                //如果选中，显示密码
                et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                //否则隐藏密码
                et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }

        val colorSpan = ForegroundColorSpan(Color.parseColor("#DF5223"))
        val hText = SpannableString("登录/注册即代表同意《星传媒用户服务协议》")
        hText.setSpan(colorSpan, hText.length - 11, hText.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_agreement.setText(hText)

        val delegate = btn_commit.delegate
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val s = et_code.text.toString()
                if (!StringUtils.isEmpty(s) && editable.length != 0 && s.length != 0){
                    delegate.backgroundColor = activity!!.resources.getColor(R.color.org_FF9933)
                    btn_commit.setTextColor(activity!!.resources.getColor(R.color.white))
                    btn_commit.isClickable = true
                }else{
                    delegate.backgroundColor = activity!!.resources.getColor(R.color.gray_DDDDDD)
                    btn_commit.setTextColor(activity!!.resources.getColor(R.color.gray_B4B4B4))
                    btn_commit.isClickable = false
                }
            }
        })
        et_code.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val s = et_phone.text.toString()
                if (!StringUtils.isEmpty(s) && p0?.length != 0 && s.length != 0){
                    delegate.backgroundColor = activity!!.resources.getColor(R.color.org_FF9933)
                    btn_commit.setTextColor(activity!!.resources.getColor(R.color.white))
                    btn_commit.isClickable = true
                }else{
                    delegate.backgroundColor = activity!!.resources.getColor(R.color.gray_B4B4B4)
                    btn_commit.setTextColor(activity!!.resources.getColor(R.color.gray_B4B4B4))
                    btn_commit.isClickable = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_code -> {
                mPresenter.onCode(et_phone.text.toString())
            }
            R.id.tv_forget_pwd ->{
                UIHelper.startRegisterForgetFrg(this, 2)
            }
            R.id.btn_commit ->{
                if (mPosition == 0){
                    mPresenter.onCodeLogin(et_phone.text.toString(), et_code.text.toString())
                }else{
                    mPresenter.onPwdLogin(et_phone_account.text.toString(), et_pwd.text.toString())
                }
            }
            R.id.tv_register ->{
                UIHelper.startRegisterForgetFrg(this, 1)
            }
            R.id.iv_colse ->{
                activity!!.finish()
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        val account = SharedAccount.getInstance(activity)
        val mobile = account.getMobile()
        val pwd = account.getPwd()
        if (!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(pwd)) {
            et_phone_account.setText(mobile)
            et_pwd.setText(pwd)
        }
    }

    override fun setCode() {
        CountDownTimerUtils(activity, 60000, 1000, tv_code).start()
    }

    override fun setSuccess() {
        ShareSessionIdCache.getInstance(Utils.getApp()).saveUserId(User.getInstance().userId)
        ActivityUtils.finishActivity(LoginAct::class.java)
    }

     override fun errorText(text: String?, errorCode: Int) {
    }

}