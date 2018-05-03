package com.bankscene.bes.welllinkbank.activity.finance.record;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.activity.finance.query.Tran_Qury_Adapter;
import com.bankscene.bes.welllinkbank.adapter.finance.TransPreAdapter;
import com.bankscene.bes.welllinkbank.biz.RegularQery;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class TransactionQueryResult extends ShareActivity {
    Intent in;
    ArrayList<HashMap<String,String>> maps;
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.mlistview)
    ListView mlistview;
    @BindView(R.id.tv_next_month)
    TextView tv_next_month;
    @BindView(R.id.list_2)
    ListView list_2;
    TransPreAdapter tpa;
    Tran_Qury_Adapter tqa;
    ArrayList<RegularQery> rqs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        in=getIntent();
        maps= (ArrayList<HashMap<String, String>>) in.getSerializableExtra("map");
        rqs=new ArrayList<>();
        for (int i=0;i<4;i++){
            RegularQery rq=new RegularQery();
            rq.setDate("2018/3/"+i+1);
            rq.setBuss_type("結息"+i);
            rq.setCurrency_type("幣別"+i);
            rq.setTrade_amount("+100"+i);
            rqs.add(rq);
        }
        tqa=new Tran_Qury_Adapter(this,rqs);

    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.query_accout_trans_record), R.string.wlb_arrow_l, "", R.string.wlb_home_nomal, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                goHome(TransactionQueryResult.this);
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initView() {
        super.initView();
        tpa=new TransPreAdapter(maps,this);
        mlistview.setAdapter(tpa);
        tv_next_month.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );
        tqa=new Tran_Qury_Adapter(this,rqs);
        list_2.setAdapter(tqa);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_transaction_query_result;
    }
}
