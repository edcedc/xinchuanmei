package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.CollectContract
import com.yc.yyc.mvp.presenter.CollectPresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_home_o.*
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/2
 * Time: 19:11
 *  收藏文章
 */
class CollectArticleFrg : BaseFragment(), CollectContract.View{
    override fun setPraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val mPresenter by lazy { CollectPresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { HomeOAdapter(it, this, listBean) } }

    override fun getLayoutId(): Int = R.layout.b_not_title_recycler

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
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