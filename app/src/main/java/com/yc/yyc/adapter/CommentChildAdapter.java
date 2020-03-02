package com.yc.yyc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatTextView;
import com.blankj.utilcode.util.StringUtils;
import com.yc.yyc.R;
import com.yc.yyc.bean.DataBean;
import java.util.List;

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/7/29
 * Time: 12:00
 */
public class CommentChildAdapter extends BaseListViewAdapter<DataBean> {

    public CommentChildAdapter(Context act, List<DataBean> listBean) {
        super(act, listBean);
    }

    private boolean isLock = false;
    public static final int lockNum = 5;

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public boolean isLock() {
        return isLock;
    }

    @Override
    public int getCount() {
        if (!isLock){
            return listBean.size() > 5 ? 5 : listBean.size();
        }else {
            return listBean.size();
        }
    }

    @Override
    protected View getCreateVieww(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(act, R.layout.i_comment_child, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final DataBean bean = listBean.get(position);
        int parseColor = Color.parseColor("#6A717C");
        String name = bean.getUserNickName();
        String byName = bean.getSheUserNickName();
        String content = bean.getContent();
        String sheContent = bean.getSheContent();
        String pUserId = bean.getSheUserId();
        SpannableString cSp;
        if (StringUtils.isEmpty(pUserId)){
            cSp = new SpannableString(name + "：" + content);
            cSp.setSpan(new ForegroundColorSpan(parseColor), name.length() + 1, cSp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }else {
            cSp = new SpannableString(name + "回复" + byName + "：" + sheContent);
            cSp.setSpan(new ForegroundColorSpan(parseColor), name.length(), name.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            cSp.setSpan(new ForegroundColorSpan(parseColor), cSp.length() - sheContent.length(), cSp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        viewHolder.tv_content.setText(cSp);
        return convertView;
    }

    class ViewHolder{

        AppCompatTextView tv_content;

        public ViewHolder(View convertView) {
            tv_content = convertView.findViewById(R.id.tv_content);
        }
    }

}
