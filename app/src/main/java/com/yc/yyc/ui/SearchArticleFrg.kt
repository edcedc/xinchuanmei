package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.bean.SearchListBean
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.event.SearchContentInEvent
import com.yc.yyc.mvp.impl.SearchContract
import com.yc.yyc.mvp.presenter.CollectPresenter
import com.yc.yyc.mvp.presenter.SearchPresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.b_not_title_recycler.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/9
 * Time: 16:28
 */
class SearchArticleFrg : BaseFragment(), SearchContract.View{
    override fun setPraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var searchContent: String? = null

    val mPresenter by lazy { SearchPresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { HomeOAdapter(it, this, listBean) } }

    override fun setHistory(all: List<SearchListBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setHotTabList(data: ArrayList<DataBean>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int = R.layout.b_not_title_recycler

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        EventBus.getDefault().register(this)
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(pagerNumber, searchContent)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(pagerNumber, searchContent)
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
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onSearchContentInEvent(event: SearchContentInEvent) {
        searchContent = event.conetent
        refreshLayout?.autoRefresh()
    }

}