package com.yc.yyc.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.event.SwitchingChannelInEvent
import com.yc.yyc.mvp.impl.OneContract
import com.yc.yyc.mvp.presenter.OnePresenter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.f_switching.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/30
 * Time: 22:07
 *  切换频道
 */
class SwitchingChannelFrg : BaseFragment(), OneContract.View, View.OnClickListener{

    val mPresenter by lazy { OnePresenter() }

    override fun getLayoutId(): Int = R.layout.f_switching

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setSofia(true)
        mPresenter.init(this)
        mPresenter.onLabel()
        iv_colse.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_colse ->{
                pop()
            }
        }
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLabel(list: ArrayList<DataBean>) {
        fl_label.setAdapter(object : TagAdapter<DataBean>(list) {
            override fun getView(parent: FlowLayout?, position: Int, t: DataBean?): View {
                val view = View.inflate(activity, R.layout.i_label, null)
                val bean = list[position]
                val tvText = view.findViewById<TextView>(R.id.tv_text)
                tvText.text = bean.name
                return view
            }
        })

        fl_label.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
                EventBus.getDefault().post(SwitchingChannelInEvent(position))
                pop()
                return false
            }
        })
    }

    override fun setBanner(list: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setNotice(data: ArrayList<DataBean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setArticlePraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setData(objects: Object) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}