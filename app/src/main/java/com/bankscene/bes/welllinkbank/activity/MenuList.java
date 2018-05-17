package com.bankscene.bes.welllinkbank.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.MenuAdapter;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * Created by Nylon on 2018/3/18.22:06
 */

public class MenuList extends ShareActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.menu_list)
    RecyclerView menu_list;
    private MenuAdapter mla;
    private ArrayList<MenuBiz> mbs;
    private int menuLength;
    Gson gson;
    //    Map<Integer,Boolean> map;
    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.menu_list), R.string.wlb_arrow_l, "", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                Type type=new TypeToken<List<MenuBiz>>(){}.getType();
                String jsonListTest=gson.toJson(mbs, type);
                DBHelper.insert(new Data(DataKey.user_index,jsonListTest));
                EventBus.getDefault().post(new MessageEvent(BaseFragment.REFRESHGRIDVIEW));
                MenuList.this.finish();
                //將功能選擇信息保存在數據庫
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.layout_menu_list;
    }

    @Override
    protected void initView() {
        super.initView();
//        map= GetUserIndexMap();
        gson=new Gson();
        String jsonListTest=DBHelper.getDataByKey(DataKey.user_index);
        if (TextUtils.isEmpty(jsonListTest)){
            mbs=new ArrayList<>();
            FinanceMainBiz fbz=new FinanceMainBiz(this);
            Object[] icons=fbz.getAllIcons();
            Object[] names=fbz.GetAllMenuNames();
            menuLength=icons.length;
            for (int i=0;i<menuLength;i++){
                MenuBiz menuBiz=new MenuBiz();
                menuBiz.setIcon_Rsid((Integer) icons[i]);
                menuBiz.setMenu_Name(((Integer) names[i]));
                menuBiz.setIs_Checked(true);
                mbs.add(menuBiz);
            }
        }else {

            Type type1=new TypeToken<List<MenuBiz>>(){}.getType();
            mbs=gson.fromJson(jsonListTest, type1);
        }
        Trace.e("mbs.size==",""+mbs.size());
        mla=new MenuAdapter(mbs,this);
        LinearLayoutManager lln=new LinearLayoutManager(this);
        menu_list.setLayoutManager(lln);
        menu_list.setAdapter(mla);
    }
}
