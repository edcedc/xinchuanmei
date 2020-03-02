package com.yc.yyc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.weight.CircleImageView
import com.yc.yyc.weight.GlideLoadingUtils

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/7
 * Time: 17:30
 */
class FollowUserAdapter (act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {
    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_follow_user, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        viewHolder.setText(R.id.tv_name, bean.userNickName)
        GlideLoadingUtils.load(act, bean.head, viewHolder.getView<CircleImageView>(R.id.iv_head), true)
        viewHolder.itemView.setOnClickListener {
            listener!!.onFollow(position, bean.sheUserId, 0)
        }
    }

    var listener: OnClickListener? = null

    interface OnClickListener {
        fun onFollow(position: Int, userId: String?, isPraise: Int)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

}