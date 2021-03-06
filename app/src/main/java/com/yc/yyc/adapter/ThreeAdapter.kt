package com.yc.yyc.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.luck.picture.lib.entity.LocalMedia
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.utils.PictureSelectorTool
import com.yc.yyc.weight.CircleImageView
import com.yc.yyc.weight.GlideLoadingUtils
import com.yc.yyc.weight.WithScrollGridView

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 16:34
 */
class ThreeAdapter(
    act: Context,
    root: BaseFragment,
    listBean: List<DataBean>,
    type: Int
) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {


    var type = type

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_star, parent, false))
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        val createTime = bean.createTime
        viewHolder.setText(R.id.tv_time, createTime!!.substring(5, createTime!!.length - 3))
        val tv_content = viewHolder.getView<AppCompatTextView>(R.id.tv_content)
        val content = bean.content
        if (!StringUtils.isEmpty(content)){
            tv_content.text = content
            tv_content.visibility = View.VISIBLE
        }else{
            tv_content.visibility = View.GONE
        }
        val tv_zan = viewHolder.getView<AppCompatTextView>(R.id.tv_zan)
        tv_zan.text = bean.whitePraise.toString()

        val iv_head = viewHolder.getView<CircleImageView>(R.id.iv_head)
        val anonymous = bean.anonymous
        if (anonymous == 2){
            iv_head.setImageResource(R.mipmap.y55);
            viewHolder.setText(R.id.tv_title, "匿名")
        }else{
            GlideLoadingUtils.load(act, bean.head, iv_head)
            viewHolder.setText(R.id.tv_title, bean.userNickName)
        }

        if (bean.praise == 1){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y38,null), null, null, null)
        }else if (bean.praise == 2){
            tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y35,null), null, null, null)
        }

        val picUrl = bean.picUrl
        if (picUrl != null){
            val split = bean.picUrl?.split(",")
            val localMediaList = ArrayList<LocalMedia>()
            val list = ArrayList<DataBean>()
            for (i in split!!.indices) {
                val bean = DataBean()
                bean.attachId = CloudApi.SERVLET_IMG_URL + split[i]
                list.add(bean)

                var localMedia = LocalMedia()
                localMedia.path = CloudApi.SERVLET_IMG_URL + split[i]
                localMediaList.add(localMedia)
            }
            var adapter = HomeAdvDAdapter(act, list)
            val gridView = viewHolder.getView<WithScrollGridView>(R.id.gridView)
            gridView.adapter = adapter
            adapter.notifyDataSetChanged()
            gridView.setOnItemClickListener { adapterView, view, i, l ->
                PictureSelectorTool.PictureMediaType(act as Activity, localMediaList, i)
            }
            if (list.size == 1) {
                gridView.numColumns = 1
            } else {
                gridView.numColumns = 3
            }
        }

        tv_zan.setOnClickListener {
            if (!(act as BaseActivity).isLogin())return@setOnClickListener
            listener!!.onStarPraise(position, bean.starId, bean.praise)
        }

        iv_head.setOnClickListener {
            if (bean.userId.equals(User.getInstance().userId))return@setOnClickListener
            if (bean.anonymous != 2 && type != 2){
                UIHelper.startUserStarFrg(root, bean.userNickName, bean.userId)
            }
        }

        viewHolder .itemView.setOnClickListener {
            UIHelper.startStarDetailsAct(bean)
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
