package com.bankscene.bes.welllinkbank.activity.mine;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset;
import com.bankscene.bes.welllinkbank.common.Config;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.PermissionResult;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import butterknife.BindView;

public class AboutUs extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.ll_head)
    LinearLayout ll_head;
    @BindView(R.id.tv_version_code)
    TextView tv_version_code;
    @BindView(R.id.re_dis)
    RelativeLayout re_dis;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.re_call)
    RelativeLayout re_call;
    @BindView(R.id.re_visiteweb)
    RelativeLayout re_visiteweb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.ABOUT_US), R.string.wlb_arrow_l,"", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                AboutUs.this.finish();
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
        int hei= BaseApplication.HEIGHT*3/13;
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hei);
        ll_head.setLayoutParams(layoutParams);
        try {
            tv_version_code.setText(this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        re_visiteweb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(AboutUs.this, WebViewActivity.class);
                in.putExtra("showActionBar","true");
                in.putExtra("url","http://www.wlbank.com.mo/");
                in.putExtra("title",getResources().getString(R.string.portal_site));
                startActivity(in);
            }
        });

        re_dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(AboutUs.this, WebViewActivity.class);
                in.putExtra("showActionBar","true");
                in.putExtra("url", Config.HOST_ADDRESS+"/static/app/server.html");
                in.putExtra("title",getResources().getString(R.string.clause));
                startActivity(in);
            }
        });
        re_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(CALL_PHONE, new PermissionResult() {
                    @Override
                    public void executeMethod() {
                        diallPhone(tv_call.getText().toString().trim());
                    }

                    @Override
                    public void onDenied() {

                    }
                });
            }
        });
    }
    public void diallPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_about_us;
    }
}
