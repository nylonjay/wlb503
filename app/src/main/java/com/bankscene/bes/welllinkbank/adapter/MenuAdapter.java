package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.view.togglebtn.togglebutton.ToggleButton;

import java.util.ArrayList;

/**
 * Created by Nylon on 2018/4/17.14:30
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder>{

    ArrayList<MenuBiz> mbs;
    Context mContext;
    MenuBiz mb;
    public MenuAdapter(ArrayList<MenuBiz> mbs, Context mContext) {

        this.mbs = mbs;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(mContext).inflate(R.layout.menu_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position+"");
        final  int index=position;
        mb=mbs.get(index);
        holder.Image.setImageResource(mb.getIcon_Rsid());
        holder.name.setText(mContext.getResources().getString(mb.getMenu_Name()));
        if (mb.is_Checked()){
            holder.tb.setToggleOn();
        }else {
            holder.tb.setToggleOff();
        }

        holder.tb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                MenuAdapter.this.mbs.get(index).setIs_Checked(on);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mbs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView Image;
        TextView name;
        ToggleButton tb;

        public ViewHolder(View view) {
            super(view);
            Image = (ImageView) view.findViewById(R.id.img_icon);
            name = (TextView) view.findViewById(R.id.tv_txt);
            tb=view.findViewById(R.id.check_toggle);

        }
    }
}
