package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.mvp.impl.ContactContract
import com.yc.yyc.mvp.presenter.ContactPresenter
import kotlinx.android.synthetic.main.f_contact.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 15:08
 */
class ContactFrg : BaseFragment(), ContactContract.View, View.OnClickListener{

    val mPresenter by lazy { ContactPresenter() }

    override fun getLayoutId(): Int = R.layout.f_contact

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.contact_information2))
        mPresenter.init(this)
        btn_commit.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_commit ->{
                mPresenter.onSaveElation(et_phone.text.toString(), et_content.text.toString())
            }
        }
    }

}