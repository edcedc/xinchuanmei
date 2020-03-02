package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.ArticleCommentAdapter
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.StarDetailsContract
import com.yc.yyc.mvp.presenter.StarDetailsPresenter
import com.yc.yyc.ui.bottom.CommentBottomFrg
import com.yc.yyc.ui.bottom.CommentStyteBottomFrg
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_topic.*
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/4
 * Time: 15:20
 */
class TopicFrg :BaseFragment(), StarDetailsContract.View, View.OnClickListener{

    var bean = DataBean()

    var imgUrl: String? = null

    val mPresenter by lazy { StarDetailsPresenter() }

    val commentBottomFrg by lazy { CommentBottomFrg() }

    val commentStyteBottomFrg by lazy { CommentStyteBottomFrg() }

    val listBean = ArrayList<DataBean>()

    val adapter by lazy { ArticleCommentAdapter(activity!!, this, listBean) }

    override fun getLayoutId(): Int = R.layout.f_topic

    override fun initParms(bundle: Bundle) {
        bean = Gson().fromJson(bundle.getString("bean"), DataBean::class.java)
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.topic_desc))
        mPresenter.init(this)
        tv_zan.setOnClickListener(this)
        tv_comment.setOnClickListener(this)
        tv_collect.setOnClickListener(this)
        tv_title.text = bean.title
        tv_desc.text = bean.content

        if (bean.praise == 1){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.y38,null), null, null)
        }else if (bean.praise == 2){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.y35,null), null, null)
        }

        showUiLoading()
        refreshLayout?.autoRefresh()
//        mPresenter.onBanner()
//        mPresenter.onSaveHistory(bean.starId)
//        mPresenter.onRequest(bean.starId, pagerNumber)
        setRecyclerViewType(recyclerView = recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        (recyclerView.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false // 取消动画效果
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(bean.starId, pagerNumber)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(bean.starId, pagerNumber)
            }
        })
        setCollect(bean.cIsTrue)
        setPraise(bean.praise)
        adapter!!.setOnClickListener(object : ArticleCommentAdapter.OnClickListener {
            override fun onArticleComment(
                position: Int,
                articleId: String?,
                discussId: String?,
                puserId: String?,
                content: String?
            ) {
                commentStyteBottomFrg.setBundle(position, articleId, discussId, puserId, content)
                commentStyteBottomFrg.show(childFragmentManager, "show")
            }

            override fun onArticlePraise(
                position: Int,
                articleId: String?,
                isPraise: Int,
                discussId: String?
            ) {
                mPresenter.onSaveArticleDispra(position, articleId, isPraise, discussId)
            }

        })
        commentBottomFrg.setOnCommentListener(object : CommentBottomFrg.onCommentListener {
            override fun onSecondComment(
                position: Int,
                id: String,
                discussId: String,
                content: String,
                puserId: String,
                pContent: String
            ) {
                mPresenter.onSaveArticleDiscuss(0, bean.starId, pContent, content, puserId)
            }

            override fun onFirstComment(text: String?) {
                mPresenter.onSaveArticleDiscuss(0, bean.starId, text, null, null)
            }
        })
        commentStyteBottomFrg.setOnCommentListener(object : CommentStyteBottomFrg.onCommentListener {
            override fun onReport(position: Int, id: String?) {
                mPresenter.onSaveDiscussReport(id)
            }

            override fun onComment(
                position: Int,
                articleId: String?,
                discussId: String?,
                puserId: String?,
                pContent: String?
            ) {
                commentBottomFrg.onSecondComment(position, articleId, discussId, puserId, 2, pContent)
                commentBottomFrg.show(childFragmentManager, "dialog");
            }

            override fun onDel(position: Int, id: String) {
                mPresenter.onDelMyDiscuss(position, id)
            }
        })
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_zan ->{
                if (!(activity as BaseActivity).isLogin()) return
                mPresenter.onPraise(bean.starId, bean.praise)
            }
            R.id.tv_comment ->{
                if (!(activity as BaseActivity).isLogin()) return
                commentBottomFrg.show(childFragmentManager, "dialog");
            }
            R.id.tv_collect ->{
                mPresenter.onCollect(bean.starId, bean.cIsTrue)
            }
        }
    }


    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun setBanner(bean: DataBean) {
//        imgUrl = bean.url
//        tv_title.text = bean?.source
//        tv_desc.text = bean?.remark
//        GlideLoadingUtils.loadRounded(activity, CloudApi.SERVLET_IMG_URL + bean?.picUrl, iv_img)
    }

    override fun setCollect(cIsTrue: Int) {
        if (cIsTrue == 1) {
            tv_collect.setCompoundDrawablesWithIntrinsicBounds(
                null,
                activity!!.getResources().getDrawable(R.mipmap.y31, null),
                null,
                null
            )
        } else {
            tv_collect.setCompoundDrawablesWithIntrinsicBounds(
                null,
                activity!!.getResources().getDrawable(R.mipmap.y30, null),
                null,
                null
            )
        }
        bean.cIsTrue = cIsTrue
    }

    override fun setPraise(praise: Int) {
        if (praise == 1) {
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(
                null,
                activity!!.getResources().getDrawable(R.mipmap.y34, null),
                null,
                null
            )
        } else {
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(
                null,
                activity!!.getResources().getDrawable(R.mipmap.y36, null),
                null,
                null
            )
        }
        bean.praise = praise
    }

    override fun setPraiseNum(praise: Int) {
        if (praise == 1) {
            bean.whitePraise += 1
        } else {
            bean.whitePraise -= 1
        }
        tv_zan.text = "y64" + bean.whitePraise
    }

    override fun setDiscuss(i: Int, result: DataBean?) {
        if (result != null) {
            listBean.add(0, result)
            adapter!!.notifyItemChanged(0)
            adapter!!.notifyItemInserted(0)
            tv_comment_num.text = "评论" + listBean.size
            tv_jingcai.text = "精彩评论（" + listBean.size + "）"
        }
    }

    override fun setSaveArticleDispra(position: Int, praise: Int) {
        val bean = listBean[position]
        var pra = bean.praise
        if (pra == 0 && praise == 1) {
            bean.whitePraise += 1
        } else if (pra == 0 && praise == 2) {
            bean.blackPraise += 1
        } else if (praise == 1) {
            bean.whitePraise += 1
            bean.blackPraise = if (bean.blackPraise == 0) 0 else bean.blackPraise - 1
        } else {
            bean.blackPraise += 1
            bean.whitePraise = if (bean.whitePraise == 0) 0 else bean.whitePraise - 1
        }
        bean.praise = praise
        adapter!!.notifyItemChanged(position)
    }

    override fun setDelMyDiscuss(position: Int) {
        listBean.removeAt(position)
        adapter!!.notifyItemRemoved(position)
        adapter!!.notifyItemChanged(position)
    }

    override fun setData(objects: Object) {
        var list = objects as List<DataBean>
        if (pagerNumber == 1) {
            listBean.clear()
        }
        listBean.addAll(list)
        adapter?.notifyDataSetChanged()
        tv_comment_num.text = "评论" + listBean.size
        tv_jingcai.text = "精彩评论（" + listBean.size + "）"
    }

}