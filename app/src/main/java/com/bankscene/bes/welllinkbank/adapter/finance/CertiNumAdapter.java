package com.bankscene.bes.welllinkbank.adapter.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.biz.CerNumBiz;
import com.bankscene.bes.welllinkbank.biz.QuoteBiz;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/3/5.16:46
 */

public class CertiNumAdapter extends BaseAdapter{
    Context mContext;
    ArrayList<CerNumBiz> quotes;

    public CertiNumAdapter(Context mContext, ArrayList<CerNumBiz> quotes) {
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
        CerNumBiz qb=quotes.get(position);
        if (null==convertView){
            qh=new QuoteHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.certi_num_item,null);
            qh.tv_num=convertView.findViewById(R.id.tv_num);
            qh.tv_cur=convertView.findViewById(R.id.tv_cur);
            qh.tv_tel_in=convertView.findViewById(R.id.tv_tel_in);
            qh.tv_tel_out=convertView.findViewById(R.id.tv_tel_out);
            qh.tv_oof_in=convertView.findViewById(R.id.tv_oof_in);
            qh.tv_oof_out=convertView.findViewById(R.id.tv_oof_out);
            convertView.setTag(qh);
        }else {
            qh= (QuoteHolder) convertView.getTag();
        }
        qh.tv_num.setText(position+"");
        qh.tv_cur.setText(qb.getCurrency());
        qh.tv_tel_in.setText(qb.getCertiNum());
        qh.tv_tel_out.setText(qb.getOut_date());
        qh.tv_oof_in.setText(qb.getState());
        qh.tv_oof_out.setText(qb.getCerti_type());
        return convertView;
    }

    class QuoteHolder{
        TextView tv_cur,tv_tel_in,tv_tel_out,tv_oof_in,tv_oof_out,tv_num;
    }
}
