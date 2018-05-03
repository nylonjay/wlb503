package com.bankscene.bes.welllinkbank.activity.finance.quoteprice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.activity.finance.balance.QueryBalance;
import com.bankscene.bes.welllinkbank.adapter.finance.QuotePriceAdapter;
import com.bankscene.bes.welllinkbank.biz.QuoteBiz;
import com.bankscene.bes.welllinkbank.common.Currency;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;

public class ForeignQuotePrice extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.tv_excel_date)
    TextView tv_excel_date;
    @BindView(R.id.list_2)
    ListView list_2;
    private String ExcelFormat="yyyy/MM/dd HH:mm:ss";
    private QuotePriceAdapter qpa;
    private ArrayList<QuoteBiz> qbs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.query_acc_balance), R.string.wlb_arrow_l, "", R.string.wlb_home_nomal, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                goHome(ForeignQuotePrice.this);
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected void initData() {
        super.initData();
        qbs=new ArrayList<>();
        String[] crrs=Currency.getCurrs();
        for (int i = 0; i< crrs.length; i++){
            QuoteBiz qb=new QuoteBiz();
            qb.setCurrency(crrs[i]);
            qb.setTel_in_price("456.00");
            qb.setTel_out_price("56.12");
            qb.setOof_in_price("6546.00");
            qb.setOof_out_price("6656.77");
            qbs.add(qb);
        }


    }

    @Override
    protected void initView() {
        super.initView();
        Date date=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat(ExcelFormat);
        String datestring=sdf.format(date);
        tv_excel_date.setText(datestring);
        qpa=new QuotePriceAdapter(this,qbs);
        list_2.setAdapter(qpa);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_foreign_quote_price;
    }
}
