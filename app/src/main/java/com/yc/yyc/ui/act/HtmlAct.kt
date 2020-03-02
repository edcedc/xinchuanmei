package com.yc.yyc.ui.act

import android.os.Bundle
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.mvp.impl.HtmlContract
import com.yc.yyc.mvp.presenter.HtmlPresenter
import kotlinx.android.synthetic.main.a_html.*

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/23
 * Time: 19:27
 */
class HtmlAct : BaseActivity(), HtmlContract.View {


    var type: Int = -1

    var url: String? = null
    var title: String? = null

    companion object {

        val USER_AGREEMENT = 1//用户协议
        val DESC = 2//查看详情
        val ADV = 3//平台广告
        val ABOUT = 4//关于我们
        val REGISTER = 5//注册协议

    }

    private val mPresenter by lazy { HtmlPresenter() }

    override fun getLayoutId(): Int = R.layout.a_html

    override fun initParms(bundle: Bundle) {
        type = bundle.getInt("type")
        url = bundle.getString("url")
        title = bundle.getString("title")
    }

    override fun initView() {
        setSofia(false)
        mPresenter.init(this)
        when (type) {
            DESC ->{
                setUrl(title ?: "", url)
            }
            ABOUT, USER_AGREEMENT, REGISTER ->{
                mPresenter.onUrl(type)
            }
            else ->{
                setTitle1(title ?: "")
                webView.loadUrl(url)
            }
        }
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                LogUtils.e(url)
                return true
            }

            override fun onReceivedError(var1: WebView, var2: Int, var3: String, var4: String) {
                progressBar.setVisibility(View.GONE)
                ToastUtils.showShort("网页加载失败")
            }
        })
        //进度条
        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE)
                    return
                }
                progressBar.setVisibility(View.VISIBLE)
                progressBar.setProgress(newProgress)
            }
        })
    }

    override fun setUrl(title: String?, content: String?) {
        setTitle1(title ?: "")
        webView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null)
    }

    override fun onDestroy() {
//        webView.removeAllViews()
//        webView.destroy()
        super.onDestroy()
    }

}