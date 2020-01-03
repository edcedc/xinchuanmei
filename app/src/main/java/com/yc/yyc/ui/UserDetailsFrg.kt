package com.yc.yyc.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.yc.mema.event.CameraInEvent
import com.yc.yyc.R
import com.yc.yyc.base.BaseFragment
import com.yc.yyc.base.User
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.UserDetailsContract
import com.yc.yyc.mvp.presenter.UserDetailsPresenter
import com.yc.yyc.ui.bottom.CameraBottomFrg
import com.yc.yyc.utils.PictureSelectorTool
import com.yc.yyc.weight.GlideLoadingUtils
import kotlinx.android.synthetic.main.f_user_details.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/2
 * Time: 19:49
 *  用户管理
 */
class UserDetailsFrg : BaseFragment(), UserDetailsContract.View, View.OnClickListener{

    val localMediaList: MutableList<LocalMedia> = ArrayList()

    val mPresenter by lazy { UserDetailsPresenter() }

    val cameraBottomFrg by lazy { CameraBottomFrg() }

    override fun getLayoutId(): Int = R.layout.f_user_details

    override fun initParms(bundle: Bundle) {
    }

    override fun initView(rootView: View) {
        setTitle(getString(R.string.accout1))
        mPresenter.init(this)
        iv_head.setOnClickListener(this)
        tv_name.setOnClickListener(this)
        tv_phone.setOnClickListener(this)
        tv_userid.setOnClickListener(this)
        tv_autograph.setOnClickListener(this)
        tv_pwd.setOnClickListener(this)
        cameraBottomFrg.setCameraListener(object : CameraBottomFrg.onCameraListener {
            override fun photo() {
                PictureSelectorTool.photo(activity!!, CameraInEvent.HEAD_PHOTO, true)
            }

            override fun camera() {
                PictureSelectorTool.PictureSelectorImage(activity!!, CameraInEvent.HEAD_CAMEAR, true)
            }
        })
        EventBus.getDefault().register(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.iv_head ->{
                cameraBottomFrg.show(childFragmentManager, "show")
            }
            R.id.tv_name ->{
                UIHelper.startUpdateDetailsFrg(this, 1)
            }
            R.id.tv_autograph ->{
                UIHelper.startUpdateDetailsFrg(this, 2)
            }
            R.id.tv_pwd ->{
                if (tv_pwd.hint.toString().equals("未设置")){
                    UIHelper.startRegisterForgetFrg(this, 4)
                }else{
                    UIHelper.startRegisterForgetFrg(this, 3)
                }
            }
        }
    }

    override fun onSupportVisible() {
        super.onSupportVisible()
        val userObj = User.getInstance().userObj
        if (userObj != null){
            GlideLoadingUtils.load(activity, userObj.optString("head"), iv_head)
            tv_phone.text = userObj.optString("phoneNum")
            tv_userid.text = userObj.optString("userId")
            val profile = userObj.optString("profile")
            if (!profile.equals("null")){
                tv_autograph.text = profile
            }
            val password = userObj.optString("password")
            if (!password.equals("null")){
                tv_pwd.hint = "已设置"
            }else{
                tv_pwd.hint = "未设置"
            }
            if (tv_phone.text.toString().equals(userObj.optString("userNickName"))){
                tv_name.hint = "未设置"
            }else{
                tv_name.text = userObj.optString("userNickName")
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMainThreadInEvent(event: CameraInEvent) {
        if (cameraBottomFrg != null && cameraBottomFrg.isShowing) cameraBottomFrg.dismiss()
        localMediaList.clear()
        localMediaList.addAll(PictureSelector.obtainMultipleResult(event.`object` as Intent))
        val path = localMediaList.get(0).getCompressPath()
        Glide.with(activity!!).load(path).into(iv_head)
        mPresenter.onSaveUserInfo(path, null, null, null)
    }

    override fun setSuccess() {
        onSupportVisible()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}