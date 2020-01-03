package com.yc.yyc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.StringUtils
import com.flyco.roundview.RoundTextView
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.utils.TimeUtil
import com.yc.yyc.weight.CircleImageView
import com.yc.yyc.weight.GlideLoadingUtils

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 11:31
 */
class ArticleCommentAdapter (act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, listBean as ArrayList<DataBean>) {

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_article_comment, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.head, viewHolder.getView<CircleImageView>(R.id.iv_head), true)
        viewHolder.setText(R.id.tv_title, bean.userNickName)
        viewHolder.setText(R.id.tv_content, bean.content)
        viewHolder.setText(R.id.tv_time, TimeUtil.getTimeFormatText(bean.createTime))
        val tv_zan = viewHolder.getView<AppCompatTextView>(R.id.tv_zan)
        val tv_cai = viewHolder.getView<AppCompatTextView>(R.id.tv_cai)

        if (bean.praise == 1){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y38,null), null, null, null)
            tv_cai.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y37,null), null, null, null)
            tv_zan.setTextColor(act.resources.getColor(R.color.org_FF9933))
            tv_cai.setTextColor(act.resources.getColor(R.color.gray_999999))
        }else if (bean.praise == 2){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y35,null), null, null, null)
            tv_cai.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y39,null), null, null, null)
            tv_zan.setTextColor(act.resources.getColor(R.color.gray_999999))
            tv_cai.setTextColor(act.resources.getColor(R.color.yellow_BA9F6C))
        }

        val tv_desc = viewHolder.getView<RoundTextView>(R.id.tv_desc)
        val sheContent = bean.sheContent
        if (!StringUtils.isEmpty(sheContent)){
            tv_desc.visibility = View.VISIBLE
            viewHolder.setText(R.id.tv_desc, "@" + bean.userNickName + ":" + sheContent)
        }else{
            tv_desc.visibility = View.GONE
        }

        tv_zan.setOnClickListener {
            if (!(act as BaseActivity).isLogin())return@setOnClickListener
            if (bean.state == 1){
                return@setOnClickListener
            }
            listener!!.onArticlePraise(position, bean.articleId, 1, bean.discussId)
        }
        tv_cai.setOnClickListener {
            if (!(act as BaseActivity).isLogin())return@setOnClickListener
            if (bean.state == 2){
                return@setOnClickListener
            }
            listener!!.onArticlePraise(position, bean.articleId, 2, bean.discussId)
        }
        viewHolder.itemView.setOnClickListener {
            listener!!.onArticleComment(position, bean.articleId, bean.discussId, bean.userId,
                if (bean.sheContent == null) bean.content else bean.sheContent
            )
        }
    }

    var listener: OnClickListener? = null

    interface OnClickListener {
        fun onArticlePraise(
            position: Int,
            articleId: String?,
            isPraise: Int,
            discussId: String?
        )
        fun onArticleComment(
            position: Int,
            articleId: String?,
            discussId: String?,
            puserId: String?,
            content: String?
        )
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}