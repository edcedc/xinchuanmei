package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.ArticleCommentAdapter
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.mvp.impl.ArticleDetailsContract
import com.yc.yyc.mvp.impl.OneContract
import com.yc.yyc.mvp.presenter.ArticleDescPresenter
import com.yc.yyc.mvp.presenter.OnePresenter
import com.yc.yyc.ui.bottom.CommentBottomFrg
import com.yc.yyc.ui.bottom.CommentStyteBottomFrg
import com.yc.yyc.weight.LinearDividerItemDecoration
import kotlinx.android.synthetic.main.f_article_desc_two.*
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/6
 * Time: 16:52
 */
class ArticleDetailsTwoFrg : BaseFragment(), ArticleDetailsContract.View, View.OnClickListener {

    var empty_view: LinearLayout? = null

    var bean = DataBean()

    val listBean = ArrayList<DataBean>()

    val mPresenter by lazy { ArticleDescPresenter() }

    val commentBottomFrg by lazy { CommentBottomFrg() }

    val commentStyteBottomFrg by lazy { CommentStyteBottomFrg() }

    val adapter by lazy { activity?.let { ArticleCommentAdapter(it, this, listBean) } }

    override fun getLayoutId(): Int = R.layout.f_article_desc_two

    override fun initParms(bundle: Bundle) {
        bean = Gson().fromJson(bundle.getString("bean"), DataBean::class.java)
    }

    override fun initView(rootView: View) {
        setTitle("快讯详情")
        mPresenter.init(this)
        empty_view = activity!!.findViewById(R.id.empty_view)
        tv_zan.setOnClickListener(this)
        tv_cai.setOnClickListener(this)
        tv_collect.setOnClickListener(this)
        tv_comment.setOnClickListener(this)

        var createTime = bean?.createTime
        if (!StringUtils.isEmpty(createTime)) {
            createTime = createTime?.substring(11, createTime.length - 3)
            tv_time.text = createTime
        }
        tv_title.text = bean.title
        tv_content.text = bean.content


        showUiLoading()
        refreshLayout?.autoRefresh()
//        mPresenter.onBanner()
        mPresenter.onSaveHistory(bean.articleId)
        setRecyclerViewType(recyclerView = recyclerView)
        recyclerView.addItemDecoration(LinearDividerItemDecoration(activity, DividerItemDecoration.VERTICAL, 2))
        recyclerView.adapter = adapter
        (recyclerView.getItemAnimator() as DefaultItemAnimator).supportsChangeAnimations = false // 取消动画效果
        refreshLayout?.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pagerNumber = 1
                mPresenter.onRequest(bean.articleId, pagerNumber)
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pagerNumber += 1
                mPresenter.onRequest(bean.articleId, pagerNumber)
            }
        })
        setCollect(bean.cIsTrue)
        setPraise(bean.praise)

        commentBottomFrg.setOnCommentListener(object : CommentBottomFrg.onCommentListener {
            override fun onSecondComment(
                position: Int,
                id: String,
                discussId: String,
                content: String,
                puserId: String,
                pContent: String
            ) {
                mPresenter.onSaveArticleDiscuss(0, bean.articleId, pContent, content, puserId)
            }

            override fun onFirstComment(text: String?) {
                mPresenter.onSaveArticleDiscuss(0, bean.articleId, text, null, null)
            }
        })
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

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun setData(objects: Object) {
        hideLoading()
        var list = objects as List<DataBean>
        if (pagerNumber == 1) {
            listBean.clear()
        }
        listBean.addAll(list)
        adapter?.notifyDataSetChanged()
        tv_jingcai.text = "精彩评论（" + listBean.size + "）"
        empty_view!!.visibility = if (listBean.size == 0) View.VISIBLE else View.GONE
    }

    override fun onClick(p0: View?) {
        if (!(activity as BaseActivity).isLogin()) return
        when (p0?.id) {
            R.id.tv_zan -> {
                if (bean.praise == 1) return
                mPresenter.onPraise(bean.articleId, 2)
            }
            R.id.tv_cai -> {
                if (bean.praise == 2) return
                mPresenter.onPraise(bean.articleId, 1)
            }
            R.id.tv_collect -> {
                mPresenter.onCollect(bean.articleId, bean.cIsTrue)
            }
            R.id.tv_comment -> {
                commentBottomFrg.show(childFragmentManager, "dialog");
            }
        }
    }

    override fun setBanner(bean: DataBean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                getResources().getDrawable(R.mipmap.y27, null),
                null,
                null,
                null
            )
            tv_cai.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.mipmap.y18, null),
                null,
                null,
                null
            )
        } else if (praise == 2) {
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.mipmap.y17, null),
                null,
                null,
                null
            )
            tv_cai.setCompoundDrawablesWithIntrinsicBounds(
                getResources().getDrawable(R.mipmap.y28, null),
                null,
                null,
                null
            )
        }
        bean.praise = praise
        tv_zan.text = "利好(" + bean.whitePraise +
                ")"
        tv_cai.text = "利空(" + bean.blackPraise +
                ")"
    }

    override fun setPraiseNum(praise: Int) {
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
        setPraise(pra)
    }

    override fun setDiscuss(i: Int, result: DataBean?) {
        if (result != null) {
            listBean.add(0, result)
            adapter!!.notifyDataSetChanged()
            tv_jingcai.text = "精彩评论（" + listBean.size + "）"
            empty_view!!.visibility = if (listBean.size == 0) View.VISIBLE else View.GONE
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

    override fun setDesc(bean: JSONObject) {
    }

}