package com.yc.yyc.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.StringUtils
import com.flyco.roundview.RoundLinearLayout
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.luck.picture.lib.entity.LocalMedia
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.utils.PictureSelectorTool
import com.yc.yyc.weight.CircleImageView
import com.yc.yyc.weight.GlideLoadingUtils
import com.yc.yyc.weight.WithScrollGridView
import com.yc.yyc.weight.WithScrollListView

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 16:34
 */
class FourAdapter(
    act: Context,
    root: BaseFragment,
    listBean: List<DataBean>
) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {


    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_four, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        val iv_img = viewHolder.getView<AppCompatImageView>(R.id.iv_img)
        if (position == 0){
            iv_img.visibility = View.VISIBLE
            GlideLoadingUtils.loadRounded(act, CloudApi.SERVLET_IMG_URL + bean.picUrl, iv_img)
        }else{
            iv_img.visibility = View.GONE
        }
        viewHolder.setText(R.id.tv_title, bean.title)
        viewHolder.setText(R.id.tv_content, bean.content)

        val layout = viewHolder.getView<RoundLinearLayout>(R.id.layout)
        val listView = viewHolder.getView<WithScrollListView>(R.id.listView)
        val list = bean.starDisList
        if (list != null && list.size != 0){
            viewHolder.setText(R.id.tv_zan, list.size)
            layout.visibility = View.VISIBLE
            val adapter = CommentChildAdapter(act, list)
            listView.adapter = adapter
        }else{
            layout.visibility = View.GONE
        }


        viewHolder.itemView.setOnClickListener {
            UIHelper.startTopicAct(bean)
        }
    }

    var listener: OnClickListener? = null

    interface OnClickListener {
        fun onStarPraise(position: Int, id: String?, isPraise: Int)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}
