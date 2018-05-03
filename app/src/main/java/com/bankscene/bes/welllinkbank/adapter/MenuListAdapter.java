package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.SharedPreferenceUtil;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.CerNumBiz;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.togglebtn.togglebutton.ToggleButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Nylon on 2018/3/18.22:42
 */

public class MenuListAdapter extends BaseAdapter {
    ArrayList<MenuBiz> mbs;
    Context mContext;
    MenuBiz mb;
    public MenuListAdapter(ArrayList<MenuBiz> mbs, Context mContext) {

        this.mbs = mbs;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return mbs.size();
    }

    @Override
    public Object getItem(int position) {
        return mbs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MenuHolder menuHolder=null;
        mb= (MenuBiz) getItem(position);
        if (convertView==null){
            menuHolder=new MenuHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.menu_item,null);
            menuHolder.iv=convertView.findViewById(R.id.img_icon);
            menuHolder.tv=convertView.findViewById(R.id.tv_txt);
            menuHolder.aSwitch=convertView.findViewById(R.id.check_toggle);
            final MenuHolder mh=menuHolder;
            menuHolder.aSwitch.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    MenuBiz info = (MenuBiz) mh.aSwitch.getTag();
                    info.setIs_Checked(on);
                    mb.setIs_Checked(on);
                }
            });
            convertView.setTag(menuHolder);
            menuHolder.aSwitch.setTag(mb);
        }else {
            menuHolder= (MenuHolder) convertView.getTag();
            menuHolder.aSwitch.setTag(mb);
        }
        menuHolder.iv.setBackgroundResource(mb.getIcon_Rsid());
        menuHolder.tv.setText(mb.getMenu_Name());
//        if (mb.getMenu_Name().equals(mContext.getResources().getString(R.string.custom))){
//            convertView.setVisibility(View.GONE);
////            convertView.findViewById(R.id.re_content).setVisibility(View.GONE);
//        }
        if (!mb.is_Checked())
            menuHolder.aSwitch.toggleOff();
        else
            menuHolder.aSwitch.toggleOn();


        return convertView;
    }

    class MenuHolder{
        ToggleButton aSwitch;
        ImageView iv;
        TextView tv;
    }
}
