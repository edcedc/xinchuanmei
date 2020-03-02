package com.yc.yyc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.weight.GlideLoadingUtils

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/6
 * Time: 14:19
 */
class RecommendAdapter(act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_recommend, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        GlideLoadingUtils.loadRounded(act, bean.picUrl, viewHolder.getView(R.id.iv_img), false, false, true, true)
        viewHolder.setText(R.id.tv_title, bean.title)
        viewHolder.setText(R.id.tv_hot, bean.lookNum.toString())
        viewHolder.itemView.setOnClickListener {
            UIHelper.startArticleDescAct(bean)
        }
        val tv_zan = viewHolder.getView<AppCompatTextView>(R.id.tv_zan)
        tv_zan.text = bean.whitePraise.toString()
        if (bean.praise == 1){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y69,null), null, null, null)
        }else {
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y68,null), null, null, null)
        }
        tv_zan.setOnClickListener {
            listener!!.onArticlePraise(position, bean.articleId, bean.praise)
        }
        tv_zan.setOnClickListener {
            listener!!.onArticlePraise(position, bean.articleId, bean.praise)
        }
    }

    var listener: OnClickListener? = null

    interface OnClickListener {
        fun onArticlePraise(position: Int, articleId: String?, isPraise: Int)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}