package com.bankscene.bes.welllinkbank.activity.finance.query;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.activity.finance.quoteprice.ForeignQuotePrice;
import com.bankscene.bes.welllinkbank.adapter.finance.CertiNumAdapter;
import com.bankscene.bes.welllinkbank.biz.CerNumBiz;
import com.bankscene.bes.welllinkbank.view.CustomListView;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 存单号查询
 * */

public class QueryCertificateNum extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.list_2)
    ListView list_2;
    private ArrayList<CerNumBiz> cbs;
    private CertiNumAdapter cna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_query_certificate_num);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.query_certi_num), R.string.wlb_arrow_l, "", R.string.wlb_home_nomal, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                goHome(QueryCertificateNum.this);
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initData() {
        super.initData();
        cbs=new ArrayList<>();
        for (int i=0;i<20;i++){
            CerNumBiz cnb=new CerNumBiz();
            cnb.setCurrency("USD");
            cnb.setCertiNum("1231");
            cnb.setOut_date("2018/3/5");
            cnb.setState("正常");
            cnb.setCerti_type("定期存款");
            cbs.add(cnb);
        }
        cna=new CertiNumAdapter(this,cbs);
        list_2.setAdapter(cna);
    }

    @Override
    protected void initView() {
        super.initView();

    }
    public void submit(View v){

    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_query_certificate_num;
    }
}
