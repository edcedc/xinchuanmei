package com.lzj.gallery.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lzj.gallery.library.R;
import com.lzj.gallery.library.views.RoundImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/11/28.
 * banner的适配器
 */

public class BannerPagerAdapter  extends PagerAdapter {
    private List<String> mList;
    private Context mContext;
    private int defaultImg=R.mipmap.ic_banner_error;//默认图片
    private int mRoundCorners=-1;

    /**
     * 默认
     * @param defaultImg
     */
    public void setDefaultImg(int defaultImg) {
        this.defaultImg = defaultImg;
    }

    /**
     * 设置圆角
     * @param mRoundCorners
     */
    public void setmRoundCorners(int mRoundCorners) {
        this.mRoundCorners = mRoundCorners;
    }

    /**
     * 点击回调
     */
    public static interface OnClickImagesListener {
        void onImagesClick(int position);
    }
    private OnClickImagesListener mImagesListener;

    public void setOnClickImagesListener(OnClickImagesListener listener) {
        mImagesListener = listener;

    }

    public BannerPagerAdapter(List<String> list,Context context){
        this.mList = list;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return 500000;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.banner_img_layout,container,false);
        RoundImageView imageView =  view.findViewById(R.id.img);

        final int index=position % mList.size();
        LoadImage(mList.get(index),imageView);
        //OnClick
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImagesListener.onImagesClick(index);
            }
        });

        container.addView(view);
        return view;
    }

    /**
     * 加载图片
     */
    public  void LoadImage(String url, RoundImageView imageview) {
        RoundedCorners roundedCorners = new RoundedCorners(mRoundCorners);
        //通过RequestOptions扩展功能,override:采样率,因为ImageView就这么大,可以压缩图片,降低内存消耗
        // RequestOptions options = RequestOptions.bitmapTransform(roundedCorners).override(300, 300);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(mContext)
                .load(url)
//                .apply(options)
                .into(imageview);
    }


}