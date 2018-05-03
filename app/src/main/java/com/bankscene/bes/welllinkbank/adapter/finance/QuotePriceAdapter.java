package com.bankscene.bes.welllinkbank.adapter.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.biz.QuoteBiz;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/3/5.16:46
 */

public class QuotePriceAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<QuoteBiz> quotes;

    public QuotePriceAdapter(Context mContext, ArrayList<QuoteBiz> quotes) {
        this.mContext = mContext;
        this.quotes = quotes;
    }

    @Override
    public int getCount() {
        return quotes.size();
    }

    @Override
    public Object getItem(int position) {
        return quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuoteHolder qh=null;
        QuoteBiz qb=quotes.get(position);
        if (null==convertView){
            qh=new QuoteHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.quote_price_item,null);
            qh.tv_cur=convertView.findViewById(R.id.tv_cur);
            qh.tv_tel_in=convertView.findViewById(R.id.tv_tel_in);
            qh.tv_tel_out=convertView.findViewById(R.id.tv_tel_out);
            qh.tv_oof_in=convertView.findViewById(R.id.tv_oof_in);
            qh.tv_oof_out=convertView.findViewById(R.id.tv_oof_out);
            convertView.setTag(qh);
        }else {
            qh= (QuoteHolder) convertView.getTag();
        }
        if (position%2==0){
            convertView.setBackgroundResource(R.color.quote_item_bg);
        }else {
            convertView.setBackgroundResource(R.color.white);
        }
        qh.tv_cur.setText(qb.getCurrency());
        qh.tv_tel_in.setText(qb.getTel_in_price());
        qh.tv_tel_out.setText(qb.getTel_out_price());
        qh.tv_oof_in.setText(qb.getOof_in_price());
        qh.tv_oof_out.setText(qb.getOof_out_price());
        return convertView;
    }

    class QuoteHolder{
        TextView tv_cur,tv_tel_in,tv_tel_out,tv_oof_in,tv_oof_out;
    }
}
