package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.FollowUserAdapter
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.FollowUserContract
import com.yc.yyc.mvp.presenter.FollowUserPresenter
import com.yc.yyc.mvp.presenter.HistoryPresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.b_title_recycler.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/7
 * Time: 17:25
 *  关注用户
 */
class FollowFrg : BaseFragment(), FollowUserContract.View{

    val mPresenter by lazy { FollowUserPresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { FollowUserAdapter(it, this, listBean) } }

    override fun getLayoutId(): Int = R.layout.b_title_recycler

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.history))
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
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
        adapter!!.setOnClickListener(object : FollowUserAdapter.OnClickListener{
            override fun onFollow(position: Int, userId: String?, isPraise: Int) {
                mPresenter.onFollow(position, userId, isPraise)
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

    override fun setFollow(position: Int) {
        listBean.removeAt(position)
        adapter!!.notifyItemRemoved(position)
        adapter!!.notifyItemChanged(position)
        showEmpty(listBean)
    }

}