package com.yc.yyc.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.luck.picture.lib.entity.LocalMedia;
import com.yc.yyc.R;
import com.yc.yyc.controller.CloudApi;
import com.yc.yyc.utils.PictureSelectorTool;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * 作者：HMY
 * 时间：2016/5/12
 */
public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridTestLayout(Context context) {
        super(context);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {

//       GlideLoadingUtils.load(mContext, CloudApi.Companion.getSERVLET_IMG_URL() + url, imageView);
                 int pWidth = ScreenUtils.getScreenWidth();
                Glide.with(mContext)
                .load(CloudApi.Companion.getSERVLET_IMG_URL() + url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        int w = resource.getIntrinsicWidth();
                        int h = resource.getIntrinsicHeight();

                        int newW;
                        int newH;
                        if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                            newW = pWidth / 2;
                            newH = newW * 5 / 3;
                        } else if (h < w) {//h:w = 2:3
                            newW = pWidth * 2 / 3;
                            newH = newW * 2 / 3;
                        } else {//newH:h = newW :w
                            newW = pWidth / 2;
                            newH = h * newW / w;
                        }
                        setOneImageLayoutParams(imageView, newW, newH);
                        return false;
                    }
                })
                .error(R.drawable.no_banner)
                .into(imageView);

        /*ImageLoaderUtil.displayImage(mContext, imageView, url, ImageLoaderUtil.getPhotoImageOption(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                int newW;
                int newH;
                if (h > w * MAX_W_H_RATIO) {//h:w = 5:3
                    newW = parentWidth / 2;
                    newH = newW * 5 / 3;
                } else if (h < w) {//h:w = 2:3
                    newW = parentWidth * 2 / 3;
                    newH = newW * 2 / 3;
                } else {//newH:h = newW :w
                    newW = parentWidth / 2;
                    newH = h * newW / w;
                }
                setOneImageLayoutParams(imageView, newW, newH);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });*/
        return false;
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        GlideLoadingUtils.load(mContext, CloudApi.Companion.getSERVLET_IMG_URL() + url, imageView);
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        List<LocalMedia> list = new ArrayList<>();
        for (String s : urlList){
            LocalMedia media = new LocalMedia();
            media.setPath(CloudApi.Companion.getSERVLET_IMG_URL() + s);
            list.add(media);
        }
        PictureSelectorTool.INSTANCE.PictureMediaType((Activity) mContext, list, i);
    }
}
