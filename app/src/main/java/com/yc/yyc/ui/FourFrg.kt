package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.FourAdapter
import com.yc.yyc.adapter.ThreeAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.FourContract
import com.yc.yyc.mvp.impl.ThreeContract
import com.yc.yyc.mvp.presenter.FourPresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_four.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:40
 */
class FourFrg : BaseFragment(), FourContract.View{

    override fun setData(objects: Object) {
        var list = objects as ArrayList<DataBean>
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
    val mPresenter by lazy { FourPresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { FourAdapter(it, this, listBean) } }

    override fun getLayoutId(): Int = R.layout.f_four

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        showUiLoading()
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
//        adapter.setOnClickListener(object : ThreeAdapter.OnClickListener{
//            override fun onStarPraise(position: Int, id: String?, isPraise: Int) {
//                mPresenter.onPraise(position, id, isPraise)
//            }
//        })
    }

}