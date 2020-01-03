package com.yc.yyc.ui.bottom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import com.blankj.utilcode.util.StringUtils;
import com.yc.yyc.R;
import com.yc.yyc.base.BaseBottomSheetFrg;


/**
 * 作者：yc on 2018/8/4.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 *  打开相册或相机
 */
@SuppressLint("ValidFragment")
public class CameraBottomFrg extends BaseBottomSheetFrg implements View.OnClickListener{


    private TextView tv_camera;
    private TextView tv_photo;

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.p_camera;
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        tv_camera = view.findViewById(R.id.tv_camera);
        tv_photo = view.findViewById(R.id.tv_photo);
        tv_camera.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tv_camera.setVisibility(View.VISIBLE);
        tv_photo.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancel:

                break;
             case R.id.tv_camera:
                if (listener != null){
                    listener.camera();
                }
                break;
             case R.id.tv_photo:
                 if (listener != null){
                     listener.photo();
                 }
                break;
        }
        dismiss();
    }

    private onCameraListener listener;
    public void setCameraListener(onCameraListener listener){
        this.listener = listener;
    }

    public interface onCameraListener{
        void camera();
        void photo();
    }

}
