package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogUtils;
import com.bankscene.bes.welllinkbank.Util.notice.NoticeUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Nylon on 2018/3/1.16:24
 */

public class AccountSpannerItem extends LinearLayout implements View.OnClickListener{
    private String type="0";
    private Context mContext;
    private String title="";
    private NoticeUtils noticeUtils;
    private DialogUtils dialogUtils;
    private ArrayList<String> datas;
    //    @BindView(R.id.spanner_tv)
    TextView spanner_tv,spanner_value;
    //    @BindView(R.id.re_type)
    RelativeLayout re_type;
    //    @BindView(R.id.icon_arrow)
    IconFontTextView icon_arrow;
    public AccountSpannerItem(Context context) {
        super(context);
        init();
    }

    public AccountSpannerItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            return;
        }
        mContext=context;
        noticeUtils=new NoticeUtils(context);
        dialogUtils=new DialogUtils(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AccountSpannerItem);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.AccountSpannerItem_spinner_tyle:
                    type = a.getString(attr);
                    break;
                case R.styleable.AccountSpannerItem_title:
                    title=a.getString(attr);

                    break;
            }
        }
        a.recycle();
        init();
    }

    private void init() {
        View contentView;

        contentView= inflate(mContext,R.layout.spanner_head,this);
        re_type=(RelativeLayout) contentView.findViewById(R.id.re_type);
        re_type.setOnClickListener(this);
        spanner_tv=(TextView) contentView.findViewById(R.id.spanner_tv);
        spanner_tv.setText(title);
        spanner_value=(TextView) contentView.findViewById(R.id.tv_value);
        icon_arrow=(IconFontTextView) contentView.findViewById(R.id.icon_arrow);
        switch (type){
            case "0":
//                layouid=R.layout.spanner_head;
                re_type.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.spanner_head_gray_selector));
//                re_type.setClickable(false);
                break;
            case "1":
                re_type.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.spanner_head_selector));
                re_type.setClickable(true);
//                layouid=R.layout.spanner_head_white;
                break;
            default:
                break;
        }
    }
    public void setData(ArrayList<String> mData){
        datas=mData;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_type:
                //弹出dialog显示列表  箭头方向改变
//                icon_arrow.setText(R.string.wl);
                if (null!=datas&&datas.size()>0){
                    showAccountList(datas);
                }
                break;
        }
    }
    public void showNotice(String notice) {
        noticeUtils.showNotice(notice);
    }
    public void showAccountList(ArrayList<String> datalist){
        datas=datalist;
        dialogUtils.ShowDialogList(mContext.getResources().getString(R.string.account_list),datas,this);
    }

    public void setResult(String s) {
        spanner_value.setText(s);
    }
    public String getResult(){
        return spanner_value.getText().toString().trim();
    }
}
