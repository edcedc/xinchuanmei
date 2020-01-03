package com.yc.yyc.ui.act

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.StringUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.Permission
import com.yc.yyc.MainActivity
import com.yc.yyc.R
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.controller.UIHelper
import com.yc.yyc.mvp.impl.LoginContract
import com.yc.yyc.mvp.presenter.LoginPresenter
import com.yc.yyc.utils.cache.SharedAccount
import com.yc.yyc.weight.RuntimeRationale

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/22
 * Time: 18:11
 */
class SplashAct : BaseActivity(), LoginContract.View {

    val mPresenter by lazy { LoginPresenter() }

    var REQUEST_CODE_SETTING: Int = 1

    val mHandle_splash = 0
    val mHandle_permission = 1

    override fun getLayoutId(): Int = R.layout.f_splash

    override fun initParms(bundle: Bundle) {
    }

    override fun initView() {
        mPresenter.init(this)
        handler?.sendEmptyMessageDelayed(mHandle_permission, 1000)
    }

    var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                mHandle_splash -> startNext()
                mHandle_permission -> setHasPermission()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (handler != null) {
            handler?.removeCallbacksAndMessages(null)
            handler = null
        }
    }

    /**
     * 设置权限
     */
    fun setHasPermission() {
        AndPermission.with(this)
            .runtime()
            .permission(
                Manifest.permission.READ_EXTERNAL_STORAGE,//写入外部存储, 允许程序写入外部存储，如SD卡
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA//拍照权限, 允许访问摄像头进行拍照
            )
            .rationale(RuntimeRationale())
            .onGranted { setPermissionOk() }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, permissions)) {
                    showSettingDialog(this, permissions)
                } else {
                    setPermissionCancel()
                }
            }
            .start()
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SETTING -> {
                showToast("The user comes back from the settings page.")
            }
        }
    }

    /**
     * Display setting dialog.
     */
    fun showSettingDialog(context: Context, permissions: List<String>) {
        val permissionNames = Permission.transformText(context, permissions)
        val message =
            context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames))

        AlertDialog.Builder(context)
            .setCancelable(false)
            .setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(R.string.setting,
                { dialog, which -> setPermission() })
            .setNegativeButton(R.string.cancel,
                { dialog, which -> setPermissionCancel() })
            .show()
    }

    /**
     * Set permissions.
     */
    fun setPermission() {
        AndPermission.with(this)
            .runtime()
            .setting()
            .start(REQUEST_CODE_SETTING)
    }

    /**
     * 权限有任何一个失败都会走的方法
     */
    fun setPermissionCancel() {
//        act?.finish()
        setHasPermission()
        showToast("请给需要的权限，以免出现异常")
    }

    /**
     * 权限都成功
     */
    fun setPermissionOk() {
        val account = SharedAccount.getInstance(act)
        val mobile = account.getMobile()
        val pwd = account.getPwd()
        if (!StringUtils.isEmpty(mobile) && !StringUtils.isEmpty(pwd)) {
            mPresenter.onPwdLogin(mobile, pwd)
        }else{
            startNext()
        }
    }

    override fun errorText(text: String?, errorCode: Int) {
        startNext()
    }

    fun startNext() {
        UIHelper.startMainAct()
        ActivityUtils.finishAllActivities()
    }

    override fun setCode() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setSuccess() {
        startNext()
    }

}