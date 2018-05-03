package com.bankscene.bes.welllinkbank.adapter.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nylon on 2018/3/2.15:31
 */

public class TransPreAdapter extends BaseAdapter{
    ArrayList<HashMap<String,String>> maps;
    Context mContext;
    public TransPreAdapter(ArrayList<HashMap<String, String>> maps,Context context) {
        this.maps = maps;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return maps.size();
    }

    @Override
    public Object getItem(int position) {
        return maps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransPre tp;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.trans_confim_item,null);
            tp=new TransPre();
            tp.tv_1=convertView.findViewById(R.id.tv_1);
            tp.tv_2=convertView.findViewById(R.id.tv_2);
            convertView.setTag(tp);
        }else {
            tp= (TransPre) convertView.getTag();
        }
        tp.tv_1.setText(maps.get(position).get("key"));
        tp.tv_2.setText(maps.get(position).get("value"));

        return convertView;
    }

    class TransPre{
        TextView tv_1,tv_2;
    }
}
