package com.bankscene.bes.welllinkbank.adapter.finance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.biz.BlanceBiz;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/3/5.11:06
 */

public class BlanceInfoAdapter extends BaseAdapter {

    ArrayList<BlanceBiz> bbs;
    Context mContext;

    public BlanceInfoAdapter(ArrayList<BlanceBiz> bbs, Context mContext) {
        this.bbs = bbs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return bbs.size();
    }

    @Override
    public Object getItem(int position) {
        return bbs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlanceBiz bbz=bbs.get(position);
        BlanceHolder bh=null;
        if (null==convertView){
            bh=new BlanceHolder();
            convertView= LayoutInflater.from(mContext).inflate(R.layout.query_balance_item,null);
            bh.tv_acctype=convertView.findViewById(R.id.tv_actype);
            bh.tv_accnum=convertView.findViewById(R.id.tv_acnum);
            bh.curr_balance=convertView.findViewById(R.id.tv_curbla);
            bh.usable_balance=convertView.findViewById(R.id.tv_useful);
            convertView.setTag(bh);
        }else {
            bh= (BlanceHolder) convertView.getTag();
        }
        bh.tv_acctype.setText(bbz.getAcc_type());
        bh.tv_accnum.setText(bbz.getAcc_num());
        bh.curr_balance.setText(bbz.getCurrent_balance());
        bh.usable_balance.setText(bbz.getUsable_balance());

        return convertView;
    }

    class BlanceHolder{
        TextView tv_acctype;
        TextView tv_accnum;
        TextView curr_balance;
        TextView usable_balance;
    }
}
