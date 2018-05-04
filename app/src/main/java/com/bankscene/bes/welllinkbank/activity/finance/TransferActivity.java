package com.bankscene.bes.welllinkbank.activity.finance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.view.AccountSpannerItem;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;

import butterknife.BindView;

public class TransferActivity extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.tv_notice_bottom)
    TextView tv_info;
    @BindView(R.id.btn_preview)
    Button btn_preview;
    @BindView(R.id.rg_in_out)
    RadioGroup rg_in_out;
    @BindView(R.id.rb_in)
    RadioButton rb_in;
    @BindView(R.id.rb_out)
    RadioButton rb_out;
    @BindView(R.id.spann_currency)
    AccountSpannerItem spann_currency;
    @BindView(R.id.ed_1)
    EditText ed_1;
    @BindView(R.id.ed_2)
    EditText ed_2;

    private ArrayList<String> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
//        actionBar.setTitle(getResources().getString(R.string.transfer_title));
        actionBar.setActionBar(getResources().getString(R.string.transfer_title), R.string.wlb_arrow_l, "",R.string.wlb_home_nomal,"", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initData() {
        super.initData();
        accounts=new ArrayList<String>();
        accounts.add("人民幣");
        accounts.add("歐元");
        accounts.add("美元");
        accounts.add("日元");
        accounts.add("盧比");
        accounts.add("泰銖");
        spann_currency.setData(accounts);
    }

    @Override
    protected void initView() {
        super.initView();
        String infos=getResources().getString(R.string.notice);

//            Trace.e(TAG,infos);
        tv_info.setText(infos.replace("#","\n"));
        btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(TransferActivity.this,TransferPreview.class));
            }
        });
//        rg_in_out.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
//                switch (checkedId){
//                    case R.id.rb_in:
//                        rb_in.setChecked(true);
//                        rb_out.setChecked(false);
//                        break;
//                    case R.id.rb_out:
//                        rb_in.setChecked(false);
//                        rb_out.setChecked(true);
//                        break;
//                }
//            }
//        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_transfer;
    }
}
