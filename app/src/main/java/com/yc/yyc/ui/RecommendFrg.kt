package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.RecommendAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.mvp.impl.RecommendContract
import com.yc.yyc.mvp.presenter.RecommendPresenter
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_home_o.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/6
 * Time: 14:07
 *  推荐
 */
class RecommendFrg : BaseFragment(), RecommendContract.View{

    val mPresenter by lazy { RecommendPresenter() }

    val adapter by lazy { RecommendAdapter(activity!!, this, listBean) }

    val listBean = ArrayList<DataBean>()

    val type = 2

    override fun getLayoutId(): Int = R.layout.f_recommend

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
//        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        (recyclerView.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false // 取消动画效果
//        mPresenter.onBanner()
        showUiLoading()
        refreshLayout?.autoRefresh()
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(pagerNumber, type)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(pagerNumber, type)
            }
        })

        adapter!!.setOnClickListener(object : RecommendAdapter.OnClickListener{
            override fun onArticlePraise(position: Int, articleId: String?, isPraise: Int) {
                mPresenter.onArticlePraise(position, articleId, isPraise)
            }
        })
        EventBus.getDefault().register(this)
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun setBanner(list: ArrayList<DataBean>) {
        val list1 = java.util.ArrayList<String>()
        for (bean in list) {
            list1.add(CloudApi.SERVLET_IMG_URL + bean.picUrl)
        }
        banner.initBanner(list1, true)//开启3D画廊效果
            .addPageMargin(10, 50)//参数1page之间的间距,参数2中间item距离边界的间距
            .addPoint(6)//添加指示器
            .addStartTimer(3)//自动轮播5秒间隔
            .addPointBottom(7)
            .addRoundCorners(10)//圆角
            .finishConfig()//这句必须加
            .addBannerListener({ position ->
                val bean = list[position]
                UIHelper.startHtmlAct(HtmlAct.ADV, bean.url, bean.source)
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

    override fun setArticlePraise(position: Int, praise: Int) {
        val bean = listBean.get(position)
        if (praise == 1){
            bean.whitePraise += 1
        }else{
            bean.whitePraise -= 1
        }
        bean.praise = praise
        adapter!!.notifyItemChanged(position)
    }

    @Subscribe
    fun onArticleInEvent(event: ArticleInEvent) {
        for (i in 0..listBean.size){
            val bean = listBean[i]
            if (event.articleId.equals(bean.articleId)){
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

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}