package com.yc.yyc.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.blankj.utilcode.util.StringUtils
import com.hazz.kotlinmvp.view.recyclerview.adapter.BaseRecyclerviewAdapter
import com.yc.yyc.adapter.base.ViewHolder
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.controller.CloudApi
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.ui.act.HtmlAct
import com.yc.yyc.weight.GlideLoadingUtils
import com.yc.yyc.weight.WithScrollGridView
import android.view.ViewTreeObserver
import androidx.constraintlayout.widget.ConstraintLayout
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity


/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/27
 * Time: 19:01
 */
class   HomeTAdapter(act: Context, root: BaseFragment, listBean: List<DataBean>) : BaseRecyclerviewAdapter<DataBean>(act, listBean as ArrayList<DataBean>) {

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
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.i_home_t, parent, false))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val bean = listBean[position]
        val split = bean.picUrl?.split(",")
        if (bean.articleId == null && split?.size == 1){
            return ADV_TYPE_1
        }else if (bean.articleId == null && split?.size!! > 1){
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
                GlideLoadingUtils.loadRounded(act, CloudApi.SERVLET_IMG_URL + bean.picUrl, viewHolder.getView<AppCompatImageView>(
                    R.id.iv_img))
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
                var createTime = bean?.createTime
                if (!StringUtils.isEmpty(createTime)){
                    createTime = createTime?.substring(11, createTime.length - 3)
                    viewHolder.setText(R.id.tv_time, createTime as String)
                }
                viewHolder.setText(R.id.tv_title, bean.title as String)
                val tv_content = viewHolder.getView<AppCompatTextView>(R.id.tv_content)
                tv_content.text = bean?.content
                val tv_zan = viewHolder.getView<AppCompatTextView>(R.id.tv_zan)
                val tv_cai = viewHolder.getView<AppCompatTextView>(R.id.tv_cai)
                viewHolder.setText(R.id.tv_zan, "利好(" + bean.whitePraise +
                        ")")
                viewHolder.setText(R.id.tv_cai, "利空(" + bean.blackPraise +
                        ")")

                if (bean.praise == 1){
                    tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y27,null), null, null, null)
                    tv_cai.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y18,null), null, null, null)
                }else if (bean.praise == 2){
                    tv_zan.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y17,null), null, null, null)
                    tv_cai.setCompoundDrawablesWithIntrinsicBounds(act.getResources().getDrawable(R.mipmap.y28,null), null, null, null)
                }

                val v2 = viewHolder.getView<ImageView>(R.id.v2)
                val layout = viewHolder.getView<ConstraintLayout>(R.id.layout)
                layout.getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            if (Build.VERSION.SDK_INT >= 16) {
                                layout.getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this)
                            } else {
                                layout.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this)
                            }
                            layout.getHeight() // 获取高度
                            v2.setMinimumHeight(layout.getHeight())
                        }
                    })

                tv_zan.setOnClickListener {
                    if (!(act as BaseActivity).isLogin())return@setOnClickListener
                    if (bean.state == 1){
                        return@setOnClickListener
                    }
                    listener!!.onArticlePraise(position, bean.articleId, 1)
                }
                tv_cai.setOnClickListener {
                    if (!(act as BaseActivity).isLogin())return@setOnClickListener
                    if (bean.state == 2){
                        return@setOnClickListener
                    }
                    listener!!.onArticlePraise(position, bean.articleId, 2)
                }
            }
        }

        viewHolder.itemView.setOnClickListener {
            when(getItemViewType(position)){
                ARTICLE_TYPE ->{
//                    UIHelper.startArticleDescAct(bean)
                }
                else ->{
                    UIHelper.startHtmlAct(HtmlAct.ADV, bean.url, bean.title)
                }
            }
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