package com.bankscene.bes.welllinkbank.activity.finance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.adapter.finance.TransPreAdapter;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;

public class TransferPreview extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.mlistview)
    ListView mListview;

    private ArrayList<HashMap<String,String>> maps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setActionBar() {
    actionBar.setActionBar(getResources().getString(R.string.transfer_title), R.string.wlb_arrow_l, "", R.string.wlb_home_nomal, "", new ActionBarClickListener() {
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
        maps=new ArrayList<>();
        for (int i=0;i<10;i++){
            HashMap map=new HashMap();
            map.put("key","支出金額"+i);
            map.put("value","3423423"+i);
            maps.add(map);
        }
        TransPreAdapter Tap=new TransPreAdapter(maps,this);
        mListview.setAdapter(Tap);
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_transfer_preview;
    }
}
