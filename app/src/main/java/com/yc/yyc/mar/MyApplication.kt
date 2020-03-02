package com.yc.yyc.mar

import android.app.Application
import android.content.Context
import com.yc.yyc.service.InitializeService
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.tencent.bugly.crashreport.CrashReport
import com.yc.yyc.R

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/9/20
 * Time: 15:33
 */
class MyApplication : Application(){

    companion object {

        var mContext: Context by Delegates.notNull()
            private set

        init {
            //设置全局默认配置（优先级最低，会被其他设置覆盖）
            SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
                //开始设置全局的基本参数（可以被下面的DefaultRefreshHeaderCreator覆盖）
                layout.setReboundDuration(1000)
                layout.setFooterHeight(100f)
                layout.setDisableContentWhenLoading(false)
                layout.setPrimaryColorsId(R.color.org_FF9933, android.R.color.white)
                layout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
//                layout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
            }
        }

    }

    override fun onCreate() {
        super.onCreate()
        mContext = this;
        InitializeService.start(this)
        CrashReport.initCrashReport(applicationContext, "454975ed09", true)
    }

}
