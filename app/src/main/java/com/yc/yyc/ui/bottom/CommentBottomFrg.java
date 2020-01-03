package com.yc.yyc.ui.bottom;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.DialogFragment;
import com.blankj.utilcode.util.StringUtils;
import com.yc.yyc.R;
import com.yc.yyc.base.BaseBottomSheetFrg;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2019/7/29
 * Time: 14:23
 */
public class CommentBottomFrg extends BaseBottomSheetFrg implements TextView.OnEditorActionListener {

    private AppCompatEditText etText;
    private int type = 1;

    @Override
    public int bindLayout() {
        return R.layout.f_comment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetEdit);
    }

    @Override
    public void initView(View view) {
        etText = view.findViewById(R.id.et_text);
        etText.setOnEditorActionListener(this);
        AppCompatTextView bt_submit = view.findViewById(R.id.bt_submit);
        new Handler().postDelayed(() -> showInput(etText), 200);

        view.findViewById(R.id.tv_cancel).setOnClickListener(view1 -> dismiss());

        bt_submit.setOnClickListener(view12 -> {
            String s = etText.getText().toString();
            if (StringUtils.isEmpty(s)){
                showToast("文字不能少于6个字~");
                return;
            }else {
                if (listener != null && type == 1){
                    listener.onFirstComment(s);
                    dismiss();
                }else if (listener != null && type == 2){
                    listener.onSecondComment(position, id, discussId, s, puserId, pContent);
                    dismiss();
                }
            }
        });
        etText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() >= 6){
                    bt_submit.setTextColor(act.getResources().getColor(R.color.org_FF9933));
                }else {
                    bt_submit.setTextColor(act.getResources().getColor(R.color.gray_B4B4B4));
                }
            }
        });
    }

    /**
     * 显示键盘
     *
     * @param et 输入焦点
     */
    public void showInput(final EditText et) {
        et.requestFocus();
        InputMethodManager imm = (InputMethodManager) act.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        close(false);
        etText.setText("");
        type = 1;
    }

    @Override
    protected void initParms(Bundle bundle) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        switch(i){
            case EditorInfo.IME_ACTION_SEND:
                String s = textView.getText().toString();
                if (StringUtils.isEmpty(s)){
                    dismiss();
                }else {
                    if (listener != null && type == 1){
                        listener.onFirstComment(s);
                        dismiss();
                    }else if (listener != null && type == 2){
                        listener.onSecondComment(position, id, discussId, s, puserId, pContent);
                        dismiss();
                    }
                }
                break;
        }
        return true;
    }

    private onCommentListener listener;
    public void setOnCommentListener(onCommentListener listener){
        this.listener = listener;
    }

    private int position;
    private String id;
    private String content;
    private String pContent;
    public void onFirstComment(String content){
        this.content = content;
    }

    private String discussId;
    private String puserId;
    public void onSecondComment(int position, String id, String discussId, String puserId, int type, String pContent){
        this.position = position;
        this.id = id;
        this.discussId = discussId;
        this.puserId = puserId;
        this.type = type;
        this.pContent = pContent;
    }

    public interface onCommentListener{
        void onFirstComment(String text);
        void onSecondComment(int position, String id, String discussId, String content, String puserId, String pContent);
    }

}
