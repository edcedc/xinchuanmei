package com.yc.yyc.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.StringUtils
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yc.yyc.R
import com.yc.yyc.adapter.MyPagerAdapter
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.bean.SearchListBean
import com.yc.yyc.event.SearchContentInEvent
import com.yc.yyc.event.SwitchingChannelInEvent
import com.yc.yyc.mvp.impl.SearchContract
import com.yc.yyc.mvp.presenter.ArticleDescPresenter
import com.yc.yyc.mvp.presenter.SearchPresenter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.f_search.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/9
 * Time: 11:32
 */
class SearchFrg : BaseFragment(), SearchContract.View, View.OnClickListener {
    override fun setPraise(position: Int, praise: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_clear -> {
                LitePal.deleteAll(SearchListBean::class.java)
                fl_history.removeAllViews()
            }
            R.id.tv_cancel -> {
                activity!!.finish()
            }
        }
    }

    override fun setHistory(all: List<SearchListBean>) {
        fl_history.setAdapter(object : TagAdapter<SearchListBean>(all) {
            override fun getView(parent: FlowLayout?, position: Int, t: SearchListBean?): View {
                val view = View.inflate(activity, R.layout.i_search_hi_label, null)
                val bean = all!![position]
                val tvText = view.findViewById<TextView>(R.id.tv_text)
                tvText.text = bean.title
                return view
            }
        })

        fl_history.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
                val toString = all!![position].title
                addHistory(toString)
                return false
            }
        })
    }

    override fun setHotTabList(list: ArrayList<DataBean>?) {
        fl_label.setAdapter(object : TagAdapter<DataBean>(list) {
            override fun getView(parent: FlowLayout?, position: Int, t: DataBean?): View {
                val view = View.inflate(activity, R.layout.i_search_label, null)
                val bean = list!![position]
                val tvText = view.findViewById<TextView>(R.id.tv_text)
                tvText.text = bean.title
                return view
            }
        })

        fl_label.setOnTagClickListener(object : TagFlowLayout.OnTagClickListener {
            override fun onTagClick(view: View?, position: Int, parent: FlowLayout?): Boolean {
                val toString = list!![position].title
                addHistory(toString)
                return false
            }
        })
    }

    override fun setRefreshLayoutMode(totalRow: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setData(objects: Object) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val mPresenter by lazy { SearchPresenter() }

    var strs = arrayOf("新闻", "动态")

    override fun getLayoutId(): Int = R.layout.f_search

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setSwipeBackEnable(false)
        mPresenter.init(this)
        tv_cancel.setOnClickListener(this)
        tv_clear.setOnClickListener(this)
        mPresenter.onHotTabList()
        mPresenter.onHistory()

        setSofia(true)
        val mFragments = ArrayList<Fragment>()
        for (i in strs.indices) {
            if (i == 0) {
                var frg = SearchArticleFrg()
                mFragments.add(frg)
            } else {
                var frg = SearchStarFrg()
                mFragments.add(frg)
            }
        }
        viewPager.setAdapter(MyPagerAdapter(childFragmentManager, mFragments, strs))
        tb_layout.setViewPager(viewPager)
        viewPager.setOffscreenPageLimit(strs.size - 1)
        tb_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                viewPager.setCurrentItem(position)
            }

            override fun onTabReselect(position: Int) {}
        })

        et_text.setOnEditorActionListener({ v, actionId, event ->
            //判断是否是“完成”键
            if (actionId === EditorInfo.IME_ACTION_SEARCH) {
                //隐藏软键盘
                val imm = v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm.isActive) {
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
                }
                val toString = et_text.text.toString()
                addHistory(toString)
                true
            }
            false
        })
    }

    fun addHistory(content: String?) {
        if (StringUtils.isEmpty(content)) return
        val bean = SearchListBean()
        bean.title = content
        bean.save()
        EventBus.getDefault().post(SearchContentInEvent(content))
        mPresenter.onHistory()
        gp_label.visibility = View.GONE
        gp_pager.visibility = View.VISIBLE
    }

}