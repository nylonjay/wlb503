package com.bankscene.bes.welllinkbank.adapter.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/3/2.11:13
 */

public class AccountlistAdapter extends BaseAdapter {
    ArrayList<String> datas;
    Context mContext;
    int selected_position;
    public AccountlistAdapter(ArrayList<String> datas, Context context) {
        this.datas = datas;
        this.mContext=context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public void restoreRadiobtns(int position){
        Trace.e("inselected===",position+"");
        selected_position=position;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (null==convertView){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.simple_account_spinner_item,null);
            vh.ct=(TextView) convertView.findViewById(R.id.tv_name);
            vh.rs=(RadioButton) convertView.findViewById(R.id.rb_state);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.ct.setText(datas.get(position));
        if (selected_position!=position){
            vh.rs.setChecked(false);
        }else {
            vh.rs.setChecked(true);
        }
        return convertView;
    }
    class ViewHolder{
        TextView ct;
        RadioButton rs;
    }
}
