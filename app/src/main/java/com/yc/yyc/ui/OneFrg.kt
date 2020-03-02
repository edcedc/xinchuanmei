package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yc.yyc.R
import com.yc.yyc.adapter.MyPagerAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.event.SwitchingChannelInEvent
import com.yc.yyc.mvp.impl.OneContract
import com.yc.yyc.mvp.presenter.OnePresenter
import kotlinx.android.synthetic.main.f_one.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/23
 * Time: 18:19
 */
class OneFrg: BaseFragment(), OneContract.View, View.OnClickListener{


    val mPresenter by lazy { OnePresenter() }

    override fun getLayoutId(): Int = R.layout.f_one

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setSofia(true)
        mPresenter.onLabel()
        iv_label.setOnClickListener(this)
        iv_msg.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        EventBus.getDefault().register(this)
    }

    override fun setLabel(list: ArrayList<DataBean>) {
        val mFragments = ArrayList<Fragment>()
        val strings = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            val bean = list[i]
            strings[i] = bean.name
            val bundle = Bundle()
                when(i){
                0 ->{
                    var frg = HomeOFrg()
                    bundle.putString("id", bean.id)
                    frg.setArguments(bundle)
                    mFragments.add(frg)
                }
                1 ->{
                    var frg = HomeTFrg()
                    bundle.putString("id", bean.id)
                    frg.setArguments(bundle)
                    mFragments.add(frg)
                }
                2 ->{
                    var frg = HomeThFrg()
                    bundle.putString("id", bean.id)
                    frg.setArguments(bundle)
                    mFragments.add(frg)
                }
                else ->{
                    var frg = HomeFFrg()
                    bundle.putString("id", bean.id)
                    frg.setArguments(bundle)
                    mFragments.add(frg)
                }
            }
        }
        viewPager.setAdapter(MyPagerAdapter(childFragmentManager, mFragments, strings))
        tb_layout.setViewPager(viewPager)
        viewPager.setOffscreenPageLimit(list.size - 1)
        tb_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {}
        })
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_label ->{
                UIHelper.startSwitchingChannelFrg(this)
            }
            R.id.iv_msg ->{
                UIHelper.startNoticelFrg(this)
            }
            R.id.tv_search ->{
                UIHelper.startSearchAct()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSwitchingChannelInEvent(event: SwitchingChannelInEvent) {
        tb_layout.currentTab = event.position
    }

    override fun setData(objects: Object) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setArticlePraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNotice(data: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBanner(list: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
