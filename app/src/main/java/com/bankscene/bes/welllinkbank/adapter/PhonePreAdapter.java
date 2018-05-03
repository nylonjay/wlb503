package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.DesityUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Nylon on 2018/3/22.21:53
 */

public class PhonePreAdapter extends BaseAdapter {

    private Context context;
    private String[] groups;
    private TextView tv_kind_show;
    public PhonePreAdapter(Context context, String[] groups,TextView tv_kind_show) {
        this.context=context;
        this.groups=groups;
        this.tv_kind_show=tv_kind_show;
    }

    @Override
    public int getCount() {
        return groups.length;
    }

    @Override
    public Object getItem(int position) {
        return groups[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        ViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.item_popupwindow,parent,false);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
//                    tv_kind_show.getWidth(),
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    DesityUtils.dp2px(context, 40)
//                    tv_kind_show.getWidth()/2
            );
            view.setLayoutParams(param);
            holder=new ViewHolder();
            holder.tv_kind= (TextView) view.findViewById(R.id.tv_item_popupwindow);
            view.setTag(holder);
        }else {
            holder= (ViewHolder) view.getTag();
        }
        holder.tv_kind.setText(groups[position]);
        if (holder.tv_kind.getText().equals(tv_kind_show.getText())){
            ColorStateList colorStateList=context.getResources().getColorStateList(R.color.main_theme_color);
            holder.tv_kind.setTextColor(colorStateList);
        }else{
            ColorStateList colorStateList=context.getResources().getColorStateList(R.color.greyText);
            holder.tv_kind.setTextColor(colorStateList);
        }

        return view;
    }
    class ViewHolder{
        TextView tv_kind;
    }

}
