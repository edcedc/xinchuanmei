package com.yc.yyc.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.TimeUtils
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.HomeOAdapter
import com.yc.yyc.adapter.HomeTAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.OneContract
import com.yc.yyc.mvp.presenter.OnePresenter
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_home_o.*
import kotlinx.android.synthetic.main.f_home_o.recyclerView
import kotlinx.android.synthetic.main.f_home_t.*
import java.util.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.yc.yyc.event.ArticleInEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat


/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/27
 * Time: 16:58
 */
class HomeTFrg : BaseFragment(), OneContract.View{

    var id :String? = null

    val mPresenter by lazy { OnePresenter() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { activity?.let { HomeTAdapter(it,this, listBean) } }

    var isRequest = true

    override fun getLayoutId(): Int = R.layout.f_home_t

    override fun initParms(bundle: Bundle) {
        id = bundle.getString("id")
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        if (isRequest){
            refreshLayout?.autoRefresh()//第一次进入触发自动刷新，演示效果
            isRequest = false
        }
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSwipeBackEnable(false)
        setRecyclerViewType(recyclerView = recyclerView)
//        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        (recyclerView.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false // 取消动画效果
        (recyclerView.getItemAnimator() as DefaultItemAnimator).setChangeDuration(0)
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(id, pagerNumber)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(id, pagerNumber)
            }
        })

        adapter!!.setOnClickListener(object : HomeTAdapter.OnClickListener{
            override fun onArticlePraise(position: Int, articleId: String?, isPraise: Int) {
                mPresenter.onArticlePraise(position, articleId, isPraise)
            }
        })

        tv_time.text = "今天" + "  " + TimeUtils.getNowString(SimpleDateFormat("MM-dd")) + "  "  + TimeUtils.getChineseWeek(TimeUtils.getNowDate())
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

    override fun setArticlePraise(position: Int, praise: Int) {
        val bean = listBean[position]
        var pra = bean.praise
        if (pra == 0 && praise == 1){
            bean.whitePraise += 1
        }else if (pra == 0 && praise == 2){
            bean.blackPraise += 1
        }else if (praise == 1){
            bean.whitePraise += 1
            bean.blackPraise = if (bean.blackPraise == 0) 0 else bean.blackPraise - 1
        }else{
            bean.blackPraise += 1
            bean.whitePraise = if (bean.whitePraise == 0) 0 else bean.whitePraise - 1
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

    override fun setLabel(list: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBanner(list: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNotice(data: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}