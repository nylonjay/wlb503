package com.bankscene.bes.welllinkbank.activity.finance.record;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.view.AccountSpannerItem;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class TransactionRecord extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.tv_7)
    TextView tv_7;
    @BindView(R.id.tv_60)
    TextView tv_60;
    @BindView(R.id.asi_acc)
    AccountSpannerItem asi_acc;
    @BindView(R.id.asi_currency)
    AccountSpannerItem asi_currency;
    @BindView(R.id.re_type_start)
    RelativeLayout re_type_start;
    @BindView(R.id.re_type_end)
    RelativeLayout re_type_end;
    @BindView(R.id.btn_query)
    Button btn_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.query_accout_trans_record), R.string.wlb_arrow_l, "", R.string.wlb_home_nomal, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                TransactionRecord.this.finish();
            }

            @Override
            public void onRightClick() {
                startActivity(new Intent(TransactionRecord.this, MainActivity.class));
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initView() {
        super.initView();
        tv_7.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tv_60.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(TransactionRecord.this,TransactionQueryResult.class);
                ArrayList<HashMap<String,String>> maps=new ArrayList<HashMap<String, String>>();
                for (int i=0;i<6;i++){
                    HashMap map=new HashMap();
                    map.put("key",getResources().getString(R.string.client_name));
                    map.put("value","周星馳");
                    maps.add(map);
                }
//                map.put(getResources().getString(R.string.currency_type),"周星馳");
//                map.put(getResources().getString(R.string.usable_balance),"周星馳");
//                map.put(getResources().getString(R.string.query_date),"周星馳");
//                map.put(getResources().getString(R.string.query_trade_in_date),"周星馳");
                in.putExtra("map",maps);
                startActivity(in);
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_transaction_record;
    }
}
