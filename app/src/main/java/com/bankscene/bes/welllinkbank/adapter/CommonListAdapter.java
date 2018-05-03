package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;

import java.util.List;

/**
 * Created by Nylon on 2018/3/28.16:25
 */

public class CommonListAdapter extends BaseAdapter {
    List<String> items;
    Context mContext;
    public CommonListAdapter(List<String> items,Context context) {
        this.items = items;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommHolder ch=null;
        if (convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.commin_item,null);
            ch=new CommHolder();
            ch.tv_name=convertView.findViewById(R.id.tv_name);
            convertView.setTag(ch);
        }else {
            ch= (CommHolder) convertView.getTag();
        }
        ch.tv_name.setText(items.get(position));

        return convertView;
    }

    class CommHolder{
        TextView tv_name;
    }
}
