package com.bankscene.bes.welllinkbank.activity.mine.gesture;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import butterknife.BindView;

public class GestureSetResult extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.gesture_setting), R.string.wlb_arrow_l, "", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                GestureSetResult.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initView() {
        super.initView();
        int hei= BaseApplication.HEIGHT*4/13;
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hei);
        layoutParams.setMargins(0,10,0,0);
        ll_head.setLayoutParams(layoutParams);

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gesture_set_result;
    }

    public void onTapOK(View v){
        startActivity(new Intent(GestureSetResult.this, MainActivity.class));
        GestureSetResult.this.finish();

    }

}
