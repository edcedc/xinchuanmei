package com.yc.yyc.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.yc.yyc.R
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.utils.NumberFormatUtils
import com.yc.yyc.weight.GlideLoadingUtils
import com.yc.yyc.weight.WithScrollGridView
import kotlin.math.roundToInt

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/27
 * Time: 19:01
 */
class HomeOAdapter(act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, root, listBean as ArrayList<DataBean>) {

    var ARTICLE_TYPE = 0;
    var ADV_TYPE_1 = 1;
    var ADV_TYPE_2 = 2;

    override fun onCreateViewHolde(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType){
            ADV_TYPE_1 ->{
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_home_o_s, parent, false))
            }
            ADV_TYPE_2 ->{
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_home_o_d, parent, false))
            }
            else ->{
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_home_o, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val bean = listBean[position]
        val split = bean.picUrl?.split(",")
        if (bean.articleId == null && split != null && split?.size == 1){
            return ADV_TYPE_1
        }else if (bean.articleId == null && split != null && split?.size!! > 1){
            return ADV_TYPE_2
        }else{
            return ARTICLE_TYPE
        }
    }

    override fun onBindViewHolde(viewHolder: ViewHolder, position: Int) {
        val bean = listBean[position]
        when(getItemViewType(position)){
            ADV_TYPE_1 ->{
                viewHolder.setText(R.id.tv_title, bean?.title)
                viewHolder.setText(R.id.tv_desc, bean?.description)
                GlideLoadingUtils.load(act, CloudApi.SERVLET_IMG_URL + bean.picUrl, viewHolder.getView<AppCompatImageView>(R.id.iv_img))
            }
            ADV_TYPE_2 ->{
                viewHolder.setText(R.id.tv_title, bean?.title)
                viewHolder.setText(R.id.tv_desc, bean?.description)
                val split = bean.picUrl?.split(",")
                val list = ArrayList<DataBean>()
                for (i in split!!.indices) {
                    val bean = DataBean()
                    bean.attachId = CloudApi.SERVLET_IMG_URL + split[i]
                    list.add(bean)
                }
                var adapter = HomeAdvDAdapter(act, list)
                val gridView = viewHolder.getView<WithScrollGridView>(R.id.gridView)
                gridView.adapter = adapter
                adapter.notifyDataSetChanged()
                gridView.setClickable(false);
                gridView.setPressed(false);
                gridView.setEnabled(false);
            }
            else ->{
                viewHolder.setText(R.id.tv_title, bean.title as String)
                var createTime = bean?.createTime
                if (!StringUtils.isEmpty(createTime)){
                    createTime = createTime?.substring(11, createTime.length - 3)
                    viewHolder.setText(R.id.tv_time, createTime as String)
                }
                viewHolder.setText(R.id.tv_read, NumberFormatUtils.formatNum(bean.readNum, false).toString() + "阅读")
                GlideLoadingUtils.loadRounded(act, bean.picUrl, viewHolder.getView(R.id.iv_img))
                val tv_desc = viewHolder.getView<AppCompatTextView>(R.id.tv_desc)
                var description = bean?.description
                if (description.equals("A5区块链")){
                    description = "波士区快链"
                }
                tv_desc.text = description
                if (bean?.readNum > 10000){
                    tv_desc.setCompoundDrawablesWithIntrinsicBounds(
                        act.resources.getDrawable(R.mipmap.y12, null), null,
                        null, null
                    )
                }else{
                    tv_desc.setCompoundDrawablesWithIntrinsicBounds(null, null,null, null)
                }
            }
        }

        viewHolder.itemView.setOnClickListener {
            when(getItemViewType(position)){
                ARTICLE_TYPE ->{
//                    bean.url = "https:" + bean.url
                    UIHelper.startArticleDescAct(bean)
                }
                else ->{
                    UIHelper.startHtmlAct(HtmlAct.ADV, bean.url, bean.title)
                }
            }
        }
    }

}