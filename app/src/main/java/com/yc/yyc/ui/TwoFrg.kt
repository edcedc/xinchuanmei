package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yc.yyc.R
import com.yc.yyc.adapter.MyPagerAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.controller.UIHelper
import kotlinx.android.synthetic.main.f_two.*
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:36
 */
class TwoFrg : BaseFragment(){

    private var strs = arrayOf("推荐", "热榜")

    override fun getLayoutId(): Int = R.layout.f_two

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setSwipeBackEnable(false)
        setSofia(true)
        val mFragments = ArrayList<Fragment>()
        for (i in strs.indices){
            if (i == 0){
                var frg = RecommendFrg()
                mFragments.add(frg)
            }else{
                var frg = HotArticleFrg()
                mFragments.add(frg)
            }
        }
        viewPager.setAdapter(MyPagerAdapter(childFragmentManager, mFragments, strs))
        tb_layout.setViewPager(viewPager)
        viewPager.setOffscreenPageLimit(strs.size - 1)
        tb_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {}
        })

        tv_search.setOnClickListener {
            UIHelper.startSearchAct()
        }
    }

}