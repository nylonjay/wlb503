package com.bankscene.bes.welllinkbank.activity.finance.query;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.biz.RegularQery;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/3/3.15:47
 */

public class Tran_Qury_Adapter extends BaseAdapter {
    Context mContext;
    ArrayList<RegularQery> rqs;

    public Tran_Qury_Adapter(Context mContext, ArrayList<RegularQery> rqs) {
        this.mContext = mContext;
        this.rqs = rqs;
    }

    @Override
    public int getCount() {
        return rqs.size();
    }

    @Override
    public Object getItem(int position) {
        return rqs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TransResult tr;
        if (null==convertView){
            tr=new TransResult();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.trans_query_result_item,null);
            tr.tv_date=(TextView) convertView.findViewById(R.id.tv_date);
            tr.tv_bus_type=(TextView) convertView.findViewById(R.id.tv_bus_type);
            tr.tv_cur_type=(TextView) convertView.findViewById(R.id.tv_currency);
            tr.tv_trade_am=(TextView) convertView.findViewById(R.id.tv_trade_sum);
            convertView.setTag(tr);
        }else {
            tr= (TransResult) convertView.getTag();
        }
        tr.tv_date.setText(rqs.get(position).getDate());
        tr.tv_bus_type.setText(rqs.get(position).getBuss_type());
        tr.tv_cur_type.setText(rqs.get(position).getCurrency_type());
        tr.tv_trade_am.setText(rqs.get(position).getTrade_amount());
        return convertView;
    }
    class TransResult{
    TextView tv_date,tv_bus_type,tv_cur_type,tv_trade_am;
    }
}
