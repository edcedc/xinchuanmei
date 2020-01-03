package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.NoticelAdapter
import com.yc.yyc.adapter.ThreeAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.ThreeContract
import com.yc.yyc.mvp.presenter.NoticelPresenter
import com.yc.yyc.mvp.presenter.ThreePresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_three.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:40
 */
class ThreeFrg : BaseFragment(), ThreeContract.View, View.OnClickListener{

    val mPresenter by lazy { ThreePresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { ThreeAdapter(activity!!, this, listBean)}

    override fun getLayoutId(): Int = R.layout.f_three

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        iv_release.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
//        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        refreshLayout?.autoRefresh()
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(pagerNumber)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(pagerNumber)
            }
        })
    }

    override fun setData(objects: Object) {
        var list = objects as List<DataBean>
        if (pagerNumber == 1){
            listBean.clear()
        }
        listBean.addAll(list)
        adapter?.notifyDataSetChanged()
//        showEmpty(listBean)
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_release->{
                UIHelper.startReleaseAct()
            }
            R.id.tv_search ->{

            }
        }
    }

}