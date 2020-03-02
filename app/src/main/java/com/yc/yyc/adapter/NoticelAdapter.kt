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
import com.yc.yyc.ui.act.HtmlAct

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/31
 * Time: 9:13
 */
class NoticelAdapter (act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_noticel, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        viewHolder.setText(R.id.tv_title, bean.title)
        viewHolder.setText(R.id.tv_content, bean.textual)
        viewHolder.itemView.setOnClickListener {
            UIHelper.startHtmlAct(HtmlAct.DESC, bean.content, bean.title)
        }
    }



}