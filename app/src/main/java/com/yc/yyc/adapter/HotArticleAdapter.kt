package com.yc.yyc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
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
class HotArticleAdapter(act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_hot_article, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        GlideLoadingUtils.loadRounded(act, bean.picUrl, viewHolder.getView(R.id.iv_img), false, false, true, true)
        viewHolder.setText(R.id.tv_title, bean.title)
        viewHolder.setText(R.id.tv_hot, bean.lookNum.toString())
        viewHolder.setText(R.id.tv_num, (position + 1).toString())
        viewHolder.itemView.setOnClickListener {
            UIHelper.startArticleDescAct(bean)
        }
    }

}