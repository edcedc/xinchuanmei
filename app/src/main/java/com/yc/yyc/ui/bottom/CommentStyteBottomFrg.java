package com.yc.yyc.ui.bottom;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import com.yc.yyc.R;
import com.yc.yyc.base.BaseBottomSheetFrg;
import com.yc.yyc.base.User;

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/7/29
 * Time: 14:23
 */
public class CommentStyteBottomFrg extends BaseBottomSheetFrg implements View.OnClickListener {

    @Override
    public int bindLayout() {
        return R.layout.p_comment_styte;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetEdit);
    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.tv_comment).setOnClickListener(this);
        view.findViewById(R.id.tv_copy).setOnClickListener(this);
        AppCompatTextView tv_del = view.findViewById(R.id.tv_del);
        tv_del.setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        if (puserId.equals(User.getInstance().getUserId())){
            tv_del.setText("删除");
            tv_del.setCompoundDrawablesWithIntrinsicBounds(null, act.getResources().getDrawable(R.mipmap.y40,null), null, null);
        }else {
            tv_del.setText("举报");
            tv_del.setCompoundDrawablesWithIntrinsicBounds(null, act.getResources().getDrawable(R.mipmap.y52,null), null, null);
        }
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_comment:
                listener.onComment(position, id, discussId, puserId, content);
                break;
            case R.id.tv_copy:
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) act.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", content);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                showToast("复制成功");
                break;
            case R.id.tv_del:
                if (puserId.equals(User.getInstance().getUserId())){
                    listener.onDel(position, discussId);
                }else {
                    listener.onReport(position, discussId);
                }
                break;
        }
        dismiss();
    }

    private onCommentListener listener;
    public void setOnCommentListener(onCommentListener listener){
        this.listener = listener;
    }
    public interface onCommentListener{
        void onComment(int position, String articleId, String discussId, String puserId, String pContent);
        void onDel(int position, String id);
        void onReport(int position, String id);
    }

    private int position;
    private String id;
    private String discussId;
    private String puserId;
    private String content;

    public void setBundle(int position, String id, String discussId, String puserId, String content){
        this.position = position;
        this.id = id;
        this.discussId = discussId;
        this.puserId = puserId;
        this.content = content;
    }


}
