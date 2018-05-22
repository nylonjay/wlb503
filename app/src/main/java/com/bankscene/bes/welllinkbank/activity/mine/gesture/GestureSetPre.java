package com.bankscene.bes.welllinkbank.activity.mine.gesture;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.gesture.LoginGestureActivity;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset2;
import com.bankscene.bes.welllinkbank.adapter.CommonListAdapter;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.MenuListViewActivity;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.togglebtn.togglebutton.ToggleButton;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import butterknife.BindView;

public class GestureSetPre extends ShareActivity implements View.OnClickListener{
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.re_change_gs)
    RelativeLayout re_change_gs;
    @BindView(R.id.tg_use_gs)
    ToggleButton tg_use_gs;
    @BindView(R.id.tg_show_trail)
    ToggleButton tg_show_trail;
    @BindView(R.id.re_show_trail)
    RelativeLayout re_show_trail;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.gesture_setting), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {GestureSetPre.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_gesture_set_pre;
    }

    @Override
    protected void initView() {
        super.initView();
        Trace.e("username==",DBHelper.getDataByKey(DataKey.userName));
        user= getUserState(DBHelper.getDataByKey(DataKey.userName));
        Trace.e("userOpen?",user.toString());
        re_change_gs.setOnClickListener(this);

        if (user.isGestureOpen())
            tg_use_gs.setToggleOn();
        else
            tg_use_gs.setToggleOff();

        if (State.showTrail)
            tg_show_trail.setToggleOn();
        else
            tg_show_trail.setToggleOff();

        tg_use_gs.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    user.setGestureOpen(true);
                    DBHelper.insert(new Data(DataKey.login_type,"true"));
                }
                else{
                    user.setGestureOpen(false);
                    DBHelper.insert(new Data(DataKey.login_type,"false"));
                }
                saveUserState(DBHelper.getDataByKey(DataKey.userName),user.isGestureOpen(),user.isGestureSetted());
            }
        });

        tg_show_trail.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                State.showTrail=on;
            }
        });
        re_show_trail.setVisibility(View.GONE);//暂时不知道怎么隐藏轨迹
        findViewById(R.id.v_trail).setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.re_change_gs:
                startActivity(new Intent(GestureSetPre.this, LoginGestureActivity.class));
                break;

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
