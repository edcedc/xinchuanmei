package com.yc.yyc.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.PopupWindow;
import com.blankj.utilcode.util.ScreenUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.SimpleCallback;
import com.yc.yyc.R;

/**
 * 作者：yc on 2018/8/23.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

public class PopupWindowTool {








    /**
     * dp转px
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }




    /**
     *  可已复用的弹窗
     * @param act
     */
    public static XPopup.Builder showDialog(Context act){
        return new XPopup.Builder(act)
                .setPopupCallback(new SimpleCallback() {
                    @Override
                    public void onCreated() {
                    }

                    @Override
                    public void beforeShow() {
                        super.beforeShow();
//                        Log.e("tag", "beforeShow，在每次show之前都会执行，可以用来进行多次的数据更新。");
                    }

                    @Override
                    public void onShow() {
                    }
                    @Override
                    public void onDismiss() {
                    }
                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    @Override
                    public boolean onBackPressed() {
//                        ToastUtils.showShort("我拦截的返回按键，按返回键XPopup不会关闭了");
                        return false;
                    }
                });
    }



}
