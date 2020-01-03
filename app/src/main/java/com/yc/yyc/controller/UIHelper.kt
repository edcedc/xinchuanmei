package com.yc.yyc.controller

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.google.gson.Gson
import com.yc.yyc.MainActivity
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.bean.DataBean
import com.yc.yyc.ui.*
import com.yc.yyc.ui.act.*


/**
 * Created by Administrator on 2017/2/22.
 */

class UIHelper private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object {

        fun startMainAct() {
            ActivityUtils.startActivity(MainActivity::class.java)
        }

       fun startLoginAct() {
            ActivityUtils.startActivity(LoginAct::class.java)
        }

        /**
         *  发布
         */
       fun startReleaseAct() {
            ActivityUtils.startActivity(ReleaseAct::class.java)
        }

        /**
         *  各种H5
         */
       fun startHtmlAct(type : Int) {
           val bundle = Bundle()
           bundle.putInt("type", type)
           ActivityUtils.startActivity(bundle, HtmlAct::class.java)
        }
       fun startHtmlAct(type : Int, url : String?) {
           val bundle = Bundle()
           bundle.putInt("type", type)
           bundle.putString("url", url)
           ActivityUtils.startActivity(bundle, HtmlAct::class.java)
        }
       fun startHtmlAct(type : Int, url : String?, title: String?) {
           val bundle = Bundle()
           bundle.putInt("type", type)
           bundle.putString("url", url)
           bundle.putString("title", title)
           ActivityUtils.startActivity(bundle, HtmlAct::class.java)
        }

        /**
         *  视频页面
         */
        fun startVideoAct(video: String, image: String) {
            var bundle = Bundle()
            bundle.putString("video", video)
            bundle.putString("image", image)
            ActivityUtils.startActivity(bundle, VideoAct::class.java)
        }

        /**
         *  注册/找回密码/修改/设置密码
         */
        fun startRegisterForgetFrg(root: BaseFragment, type: Int) {
            var frg = RegisterForgetFrg()
            var bundle = Bundle()
            bundle.putInt("type", type)
            frg.setArguments(bundle)
            root.start(frg)
        }

        /**
         *  切换频道
         */
        fun startSwitchingChannelFrg(root: BaseFragment) {
            var frg = SwitchingChannelFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  星公告
         */
        fun startNoticelFrg(root: BaseFragment) {
            var frg = NoticelFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  详情
         */
        fun startArticleDescAct(bean: DataBean) {
            val bundle = Bundle()
            bundle.putString("bean", Gson().toJson(bean))
            ActivityUtils.startActivity(bundle, ArticleDescAct::class.java)
        }

        /**
         *  收藏
         */
        fun startCollectFrg(root: BaseFragment) {
            var frg = CollectFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  历史记录
         */
        fun startHistoryFrg(root: BaseFragment) {
            var frg = HistoryFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  联系我们
         */
        fun startContactFrg(root: BaseFragment) {
            var frg = ContactFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  账户管理
         */
        fun startUserDetailsFrg(root: BaseFragment) {
            var frg = UserDetailsFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  更新用户资料
         */
        fun startUpdateDetailsFrg(root: BaseFragment, type: Int) {
            var frg = UpdateDetailsFrg()
            var bundle = Bundle()
            bundle.putInt("type", type)
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

        /**
         *  设置
         */
        fun startSetFrg(root: BaseFragment) {
            var frg = SetFrg()
            var bundle = Bundle()
            frg.setArguments(bundle)
            val fragment = root.parentFragment
            if (fragment == null) {
                root.start(frg)
            } else {
                (root.parentFragment as MainFrg).startBrotherFragment(frg)
            }
        }

    }

}