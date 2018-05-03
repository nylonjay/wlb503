package com.bankscene.bes.welllinkbank.core;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.adapter.CommonListAdapter;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MenuListViewActivity extends ShareActivity implements AdapterView.OnItemClickListener{
    @BindView(R.id.actionBar)
    protected TranslucentActionBar actionBar;
    protected ListView listView;
    protected List<String> items=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSubMenu();
    }

    protected void setSubMenu() {
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        CommonListAdapter cla=new CommonListAdapter(items,this);
        listView.setAdapter(cla);
    }

    @Override
    protected void setActionBar() {

    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_menu_list_view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
