package com.yc.yyc.ui.act

import android.os.Bundle
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.ui.SearchFrg

class SearchAct : BaseActivity(){


    override fun getLayoutId(): Int = R.layout.activity_main

    override fun initView() {
        val frg = SearchFrg::class.java.newInstance()
        var bundle = Bundle()
        frg.setArguments(bundle)
        if (findFragment(SearchFrg::class.java) == null) {
            loadRootFragment(R.id.fl_container, frg)
        }
    }

    override fun initParms(bundle: Bundle) {
    }

}
