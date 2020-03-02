package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.adapter.MyPagerAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.CollectContract
import com.yc.yyc.mvp.presenter.CollectPresenter
import com.yc.yyc.utils.TabEntity
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_collect.*
import kotlinx.android.synthetic.main.f_home_o.*
import me.yokeyword.fragmentation.SupportFragment
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/2
 * Time: 19:11
 *  收藏
 */
class CollectFrg : BaseFragment(){

    val strings = arrayOf("新闻", "动态")
    val mFragments = ArrayList<Fragment>()

    override fun getLayoutId(): Int = R.layout.f_collect

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.my_collection))
        setSwipeBackEnable(false)
        mFragments.add(CollectArticleFrg())
        mFragments.add(CollectStarFrg())
        viewPager.setAdapter(MyPagerAdapter(childFragmentManager, mFragments, strings))
        tb_layout.setViewPager(viewPager)
        viewPager.setOffscreenPageLimit(strings.size - 1)
        tb_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {}
        })
    }


    /**
     * start other BrotherFragment
     */
    fun startBrotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }

}