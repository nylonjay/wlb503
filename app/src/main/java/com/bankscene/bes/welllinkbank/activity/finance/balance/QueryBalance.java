package com.bankscene.bes.welllinkbank.activity.finance.balance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.activity.finance.record.TransactionQueryResult;
import com.bankscene.bes.welllinkbank.adapter.finance.BlanceInfoAdapter;
import com.bankscene.bes.welllinkbank.biz.BlanceBiz;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;

import butterknife.BindView;

public class QueryBalance extends ShareActivity {
    ArrayList<BlanceBiz> bbs;

    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.mlistview)
    ListView mlistview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        super.initData();
        bbs=new ArrayList<>();
        BlanceBiz bz=new BlanceBiz();
        bz.setAcc_num("54668654656543");
        bz.setAcc_type("外匯通寶");
        BlanceBiz bz1=new BlanceBiz();
        bz1.setAcc_num("54668654656543");
        bz1.setAcc_type("儲蓄");
        bz1.setCurrent_balance("565456546.23");
        bz1.setUsable_balance("65894156.456");
        BlanceBiz bz2=new BlanceBiz();
        bz2.setAcc_num("54668654656543");
        bz2.setAcc_type("儲蓄");
        bz2.setCurrent_balance("565456546.23");
        bz2.setUsable_balance("65894156.456");
        bbs.add(bz);bbs.add(bz1);bbs.add(bz2);
        BlanceInfoAdapter bia=new BlanceInfoAdapter(bbs,QueryBalance.this);
        mlistview.setAdapter(bia);
    }

    @Override
    protected void initView() {
        super.initView();

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
                goHome(QueryBalance.this);
            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_query_balance;
    }
}
