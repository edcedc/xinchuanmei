package com.yc.yyc.weight;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.yyc.R;
import com.yc.yyc.bean.DataBean;
import com.yc.yyc.controller.CloudApi;
import com.youth.banner.loader.ImageLoader;


public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        DataBean bean = (DataBean) path;
        //具体方法内容自己去选择，次方法是为了减少banner过多的依赖第三方包，所以将这个权限开放给使用者去选择
        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.no_banner);
        Glide.with(context.getApplicationContext())
                .load(CloudApi.Companion.getSERVLET_IMG_URL() + bean.getImage())
                .apply(options)
                .into(imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        //圆角
//        return new RoundAngleImageView(context);
//    }
}
