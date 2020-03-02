package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.LogUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.mvp.impl.OneContract
import com.yc.yyc.mvp.presenter.OnePresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_home_o.*
import java.util.ArrayList
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.event.ArticleInEvent
import com.yc.yyc.ui.act.HtmlAct
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/27
 * Time: 16:58
 */
class HomeThFrg : BaseFragment(), OneContract.View {

    var id: String? = null

    val mPresenter by lazy { OnePresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { HomeOAdapter(it, this, listBean) } }

    var isRequest = true

    override fun getLayoutId(): Int = R.layout.f_home_th

    override fun initParms(bundle: Bundle) {
        id = bundle.getString("id")
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (isRequest) {
            refreshLayout?.autoRefresh()//第一次进入触发自动刷新，演示效果
//            mPresenter.onNotice()
            isRequest = false
        }
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        (recyclerView.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false // 取消动画效果
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
//                mPresenter.onBanner()
                mPresenter.onRequest(id, pagerNumber)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(id, pagerNumber)
            }
        })
        EventBus.getDefault().register(this)
    }

    override fun setData(objects: Object) {
        var list = objects as List<DataBean>
        if (pagerNumber == 1){
            for (i in 0..list.lastIndex) {
                val bean = list[i]
                listBean.add(i, bean)
            }
        }else{
            listBean.addAll(list)
        }
        adapter?.notifyDataSetChanged()
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun setLabel(list: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBanner(list: ArrayList<DataBean>) {
        val list1 = ArrayList<String>()
        for (bean in list) {
            val split = bean.picUrl!!.split(",")
            list1.add(CloudApi.SERVLET_IMG_URL + split[0])
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

    override fun setNotice(data: ArrayList<DataBean>) {
        val list = ArrayList<String>()
        for (bean in data) {
            bean.title?.let { list.add(it) }
        }
        marqueeView.startWithList(list);
        marqueeView.setOnItemClickListener { position, textView ->
            val bean = data[position]
            UIHelper.startHtmlAct(HtmlAct.DESC, bean.content, bean.title)
        }
    }

    @Subscribe
    fun onArticleInEvent(event: ArticleInEvent) {
        for (i in listBean.indices){
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

    override fun setArticlePraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}