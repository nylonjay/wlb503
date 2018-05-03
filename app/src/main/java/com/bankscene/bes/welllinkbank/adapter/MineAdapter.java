package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;

/**
 * Created by Nylon on 2018/2/27.10:41
 */

public class MineAdapter extends BaseAdapter {
    int[] imgs;
    String[] words;
    Context mContext;
    public MineAdapter(int[] images,String[] names,Context context){
        imgs=images;
        words=names;
        mContext=context;
    }
    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int i) {
        return words[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh=null;
        View item=null;
        if (null==view){
            item= LayoutInflater.from(mContext).inflate(R.layout.mine_item,null);
            vh=new ViewHolder();
            vh.iv=(ImageView) item.findViewById(R.id.img_icon);
            vh.tv=(TextView) item.findViewById(R.id.tv_txt);
            vh.view_empty=item.findViewById(R.id.view_empty);
            vh.re_content=item.findViewById(R.id.re_content);
            item.setTag(vh);
        }else {
            item=view;
            vh= (ViewHolder) item.getTag();
        }
        if (!" ".equals(words[i])){
            vh.iv.setBackgroundDrawable(mContext.getResources().getDrawable(imgs[i]));
            vh.tv.setText(words[i]);
            vh.view_empty.setVisibility(View.GONE);
            vh.re_content.setVisibility(View.VISIBLE);
        }else {
            vh.view_empty.setVisibility(View.VISIBLE);
            vh.re_content.setVisibility(View.GONE);
//            View empty_view=LayoutInflater.from(mContext).inflate(R.layout.item_empty,null);
//            return empty_view;
        }
        return item;
    }

    class ViewHolder{
        ImageView iv;
        TextView tv;
        View view_empty;
        RelativeLayout re_content;
    }

}
