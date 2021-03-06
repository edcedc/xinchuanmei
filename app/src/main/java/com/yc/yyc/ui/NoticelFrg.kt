package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.adapter.NoticelAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.NoticelContract
import com.yc.yyc.mvp.presenter.NoticelPresenter
import com.yc.yyc.mvp.presenter.OnePresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_home_o.*
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 9:06
 *  通知
 */
class NoticelFrg : BaseFragment(), NoticelContract.View{


    val mPresenter by lazy { NoticelPresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { NoticelAdapter(it, this, listBean) } }

    override fun getLayoutId(): Int = R.layout.b_title_recycler

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setTitle(getString(R.string.noticel))
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
        showEmpty(listBean)
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

}