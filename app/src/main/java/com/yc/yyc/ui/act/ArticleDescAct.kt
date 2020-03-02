package com.yc.yyc.ui.act

import android.os.Bundle
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.ui.ArticleDetailsFrg

class ArticleDescAct : BaseActivity(){

    var bean: String? = null

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        val frg = ArticleDetailsFrg::class.java.newInstance()
        var bundle = Bundle()
        bundle.putString("bean", bean)
        frg.setArguments(bundle)
        if (findFragment(ArticleDetailsFrg::class.java) == null) {
            loadRootFragment(R.id.fl_container, frg)
        }
    }

    override fun initParms(bundle: Bundle) {
        bean = bundle.getString("bean")
    }

}
