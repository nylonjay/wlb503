package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bankscene.bes.welllinkbank.R;

/**
 * Created by Nylon on 2018/2/27.15:42
 */

public class NoticeAdapter extends RecyclerView.Adapter{
    Context mContext;
    public NoticeAdapter(Context context){
        mContext=context;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoticeItem(LayoutInflater.from(mContext).inflate(R.layout.notice_item,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 50;
    }
    class NoticeItem extends RecyclerView.ViewHolder{

        public NoticeItem(View itemView) {
            super(itemView);
        }
    }

}
