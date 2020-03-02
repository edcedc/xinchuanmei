package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.ThreeAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.event.StarInEvent
import com.yc.yyc.mvp.impl.ThreeContract
import com.yc.yyc.mvp.presenter.ThreePresenter
import kotlinx.android.synthetic.main.f_three.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/10/24
 * Time: 11:40
 */
class UserStarFrg : BaseFragment(), ThreeContract.View{

    var isFolloww = 0

    var like: String? = null

    var userId: String? = null

    val mPresenter by lazy { ThreePresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { ThreeAdapter(activity!!, this, listBean, 2)}

    override fun getLayoutId(): Int = R.layout.b_title_recycler

    override fun initParms(bundle: Bundle) {
        like = bundle.getString("like")
        userId = bundle.getString("userId")
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setTitle(like)
        setRecyclerViewType(recyclerView = recyclerView)
        setMargins(recyclerView, 20, 0, 20, 0)
//        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        refreshLayout?.autoRefresh()
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onIfFollowUser(userId)
                mPresenter.onRequest(pagerNumber, like)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(pagerNumber, like)
            }
        })
        adapter.setOnClickListener(object : ThreeAdapter.OnClickListener{
            override fun onStarPraise(position: Int, id: String?, isPraise: Int) {
                mPresenter.onPraise(position, id, isPraise)
            }
        })
        EventBus.getDefault().register(this)
    }

    override fun setData(objects: Object) {
        var list = objects as List<DataBean>
        if (pagerNumber == 1){
            listBean.clear()
        }
        listBean.addAll(list)

        var listDel = ArrayList<DataBean>()
        for (i in listBean.indices){
            val bean = listBean[i]
            if (bean.anonymous == 2){
                listDel.add(bean)
            }
        }
        listBean.removeAll(listDel)

        adapter?.notifyDataSetChanged()
//        showEmpty(listBean)
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun setPraise(position: Int, praise: Int) {
        val bean = listBean.get(position)
        if (praise == 1){
            bean.whitePraise += 1
        }else{
            bean.whitePraise -= 1
        }
        bean.praise = praise
        adapter.notifyItemChanged(position)
    }

    override fun setOnRightClickListener() {
        super.setOnRightClickListener()
        mPresenter.onSaveStarFollow(userId, isFolloww)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onStarInEvent(event: StarInEvent) {
        for (i in 0..listBean.size){
            val bean = listBean[i]
            if (event.id.equals(bean.starId)){
                if (event.cIsTrue != -1){
                    bean.cIsTrue = event.cIsTrue
                }
                if (event.praise != -1){
                    bean.praise = event.praise
                    if (event.praise == 1){
                        bean.whitePraise += 1
                    }else{
                        bean.whitePraise -= 1
                    }
                }
                break
            }
        }
    }


    override fun setFollow(i: Int) {
        if (i == 0){
            setTitle(like, "关注")
        }else{
            setTitle(like, "取消关注")
        }
        isFolloww = i
    }

}