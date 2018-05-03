package com.bankscene.bes.welllinkbank.activity.mine.password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.activity.gesture.LoginGestureActivity;
import com.bankscene.bes.welllinkbank.activity.mine.gesture.GestureSetPre;
import com.bankscene.bes.welllinkbank.adapter.CommonListAdapter;
import com.bankscene.bes.welllinkbank.core.MenuListViewActivity;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import butterknife.BindView;

public class CodeReset extends MenuListViewActivity implements AdapterView.OnItemClickListener{
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
//    @BindView(R.id.re_toreset)
//    RelativeLayout re_toreset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.wlb_divider);
//        re_toreset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CodeReset.this,CodeReset2.class));
//            }
//        });
    }

    @Override
    protected void setSubMenu() {
        items.add(getResources().getString(R.string.code_setting));
        items.add(getResources().getString(R.string.gesture_setting));
        items.add(getResources().getString(R.string.trade_code_reset));

        super.setSubMenu();
        CommonListAdapter cla=new CommonListAdapter(items,this);
        listView.setAdapter(cla);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.CODE_RESET), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            CodeReset.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                startActivity(new Intent(CodeReset.this,CodeReset2.class));
                break;
            case 1:
                startActivity(new Intent(CodeReset.this,GestureSetPre.class));
                break;
            case 2:
                startActivity(new Intent(CodeReset.this,TradeCodeReset.class));
                break;
        }
    }
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.wlb_divider;
//    }
}