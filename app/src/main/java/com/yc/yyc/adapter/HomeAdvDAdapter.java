package com.yc.yyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageView;
import com.blankj.utilcode.util.LogUtils;
import com.yc.yyc.R;
import com.yc.yyc.bean.DataBean;
import com.yc.yyc.mvp.impl.HtmlContract;
import com.yc.yyc.utils.cache.ACache;
import com.yc.yyc.weight.GlideImageLoader;
import com.yc.yyc.weight.GlideLoadingUtils;
import com.yc.yyc.weight.SizeImageView;

import java.util.List;

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/12/28
 * Time: 1:01
 */
public class HomeAdvDAdapter extends BaseListViewAdapter<DataBean> {

    public HomeAdvDAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_home_o_d_img, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        viewHolder.iv_img.setWH(1, 1, true);
        GlideLoadingUtils.load(act, bean.getAttachId(), viewHolder.iv_img);
        return convertView;
    }

    class ViewHolder{

        SizeImageView iv_img;

        public ViewHolder(View convertView) {
            iv_img = convertView.findViewById(R.id.iv_img);
        }
    }

}
