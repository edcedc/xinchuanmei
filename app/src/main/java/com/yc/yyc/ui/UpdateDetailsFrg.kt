package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.mvp.impl.UserDetailsContract
import com.yc.yyc.mvp.presenter.UserDetailsPresenter
import kotlinx.android.synthetic.main.f_update_details.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 11:10
 *  更新用户资料
 */
class UpdateDetailsFrg : BaseFragment(), UserDetailsContract.View, View.OnClickListener{

    val mPresenter by lazy { UserDetailsPresenter() }

    var type: Int = 0

    override fun getLayoutId(): Int = R.layout.f_update_details

    override fun initParms(bundle: Bundle) {
        type = bundle.getInt("type")
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        when(type){
            1 -> {
                setTitle(getString(R.string.set_nick))
                et_text.hint = getString(R.string.set_nick1)
            }
            2 ->{
                setTitle(getString(R.string.autograph))
                et_text.hint = getString(R.string.yy4)
            }
        }
        btn_commit.setOnClickListener(this)
    }

    override fun setSuccess() {
        pop()
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_commit ->{
                if (et_text.text!!.length == 0)return
                when(type){
                    1 ->{
                        mPresenter.onSaveUserInfo(null, et_text.text.toString(), null, null)
                    }
                    2 ->{
                        mPresenter.onSaveUserInfo(null, null, et_text.text.toString(), null)
                    }
                }
            }
        }
    }


}