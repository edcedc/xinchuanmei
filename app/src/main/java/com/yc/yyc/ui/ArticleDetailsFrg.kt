package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.yc.yyc.R
import com.yc.yyc.adapter.ArticleCommentAdapter
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.ArticleDetailsContract
import com.yc.yyc.mvp.presenter.ArticleDescPresenter
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.ui.bottom.CommentBottomFrg
import com.yc.yyc.ui.bottom.CommentStyteBottomFrg
import com.yc.yyc.utils.NumberFormatUtils
import com.yc.yyc.weight.GlideLoadingUtils
import com.yc.yyc.weight.LinearDividerItemDecoration
import com.yc.yyc.weight.NOX5WebView
import com.yc.yyc.weight.X5WebView
import kotlinx.android.synthetic.main.f_article_desc.*
import kotlinx.android.synthetic.main.f_article_desc.recyclerView
import kotlinx.android.synthetic.main.i_home_o_s.*
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 10:02
 */
class ArticleDetailsFrg : BaseFragment(), ArticleDetailsContract.View, View.OnClickListener {


    var bean = DataBean()

    var imgUrl: String? = null

    val mPresenter by lazy { ArticleDescPresenter() }

    val commentBottomFrg by lazy { CommentBottomFrg() }

    val commentStyteBottomFrg by lazy { CommentStyteBottomFrg() }

    val adapter by lazy { activity?.let { ArticleCommentAdapter(it, this, listBean) } }

    val listBean = ArrayList<DataBean>()

    var progressBar: ProgressBar? = null
    var webView: NOX5WebView? = null
    var empty_view: LinearLayout? = null

    override fun getLayoutId(): Int = R.layout.f_article_desc

    override fun initParms(bundle: Bundle) {
        bean = Gson().fromJson(bundle.getString("bean"), DataBean::class.java)
    }

    override fun initView(rootView: View) {
        mPresenter.init(this)
        setSofia(true)
        progressBar = activity!!.findViewById(R.id.progressBar)
        webView = activity!!.findViewById(R.id.webView)
        empty_view = activity!!.findViewById(R.id.empty_view)
        iv_colse.setOnClickListener(this)
        iv_img.setOnClickListener(this)
        tv_collect.setOnClickListener(this)
        tv_zan.setOnClickListener(this)
        tv_comment.setOnClickListener(this)
        showUiLoading()
        mPresenter.onBlockchain(bean.category, bean.url)

//        webView!!.loadUrl(bean.url)
        webView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                LogUtils.e(url)
                return true
            }

            override fun onReceivedError(var1: WebView, var2: Int, var3: String, var4: String) {
                progressBar!!.visibility = View.GONE
                ToastUtils.showShort("网页加载失败")
            }
        })
        //进度条
        webView!!.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar!!.visibility = View.GONE
                    hideLoading()
                    return
                }
                progressBar!!.visibility = View.VISIBLE
                progressBar!!.setProgress(newProgress)
            }
        })

        refreshLayout?.autoRefresh()
        mPresenter.onBanner()
        mPresenter.onSaveHistory(bean.articleId)
        mPresenter.onRequest(bean.articleId, pagerNumber)
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
        tv_zan.text = "赞" + NumberFormatUtils.formatNum(bean.whitePraise, false).toString()

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
                UIHelper.startContactFrg(this@ArticleDetailsFrg, 1)
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

    override fun setData(objects: Object) {
        var list = objects as List<DataBean>
        if (pagerNumber == 1) {
            listBean.clear()
        }
        listBean.addAll(list)
        adapter?.notifyDataSetChanged()
        tv_comment_num.text = "评论" + listBean.size
        tv_jingcai.text = "精彩评论（" + listBean.size + "）"
        empty_view!!.visibility = if (listBean.size == 0) View.VISIBLE else View.GONE
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        super.setRefreshLayoutMode(listBean.size, totalRow)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.iv_colse -> {
                activity!!.finish()
            }
            R.id.iv_img -> {
                UIHelper.startHtmlAct(HtmlAct.ADV, imgUrl, tv_title.text.toString())
            }
            R.id.tv_collect -> {
                if (!(activity as BaseActivity).isLogin()) return
                mPresenter.onCollect(bean.articleId, bean.cIsTrue)
            }
            R.id.tv_zan -> {
                if (!(activity as BaseActivity).isLogin()) return
                mPresenter.onPraise(bean.articleId, bean.praise)
            }
            R.id.tv_comment -> {
                if (!(activity as BaseActivity).isLogin()) return
                commentBottomFrg.show(childFragmentManager, "dialog");
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView!!.removeAllViews()
        webView!!.destroy()
    }

    override fun setBanner(bean: DataBean) {
        imgUrl = bean.url
        tv_title.text = bean?.title
        tv_desc.text = bean?.remark
        GlideLoadingUtils.loadRounded(activity, CloudApi.SERVLET_IMG_URL + bean.picUrl, iv_img)
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
        tv_zan.text = "赞" + NumberFormatUtils.formatNum(bean.whitePraise, false).toString()
    }

    override fun setDiscuss(i: Int, result: DataBean?) {
        if (result != null) {
            listBean.add(0, result)
            adapter!!.notifyDataSetChanged()
            tv_comment_num.text = "评论" + listBean.size
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

    override fun setDesc(json: JSONObject) {
        tv_titlee.text = json.optString("title")
        tv_time.text = bean.createTime!!.substring(5, bean.createTime!!.length) + "    阅读" + NumberFormatUtils.formatNum(bean.readNum, false)
        webView!!.loadDataWithBaseURL(null, json.optString("content"), "text/html", "utf-8", null)
    }

}