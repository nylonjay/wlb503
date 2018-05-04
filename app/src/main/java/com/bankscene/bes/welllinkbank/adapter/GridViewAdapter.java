package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;

import java.util.List;

/**
 * Created by YH on 2016/10/17.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<MenuBiz> mDatas;
    private LayoutInflater inflater;
    /**
     * 页数下标,从0开始(当前是第几页)
     */
    private int curIndex;
    /**
     * 每一页显示的个数
     */
    private int pageSize;
    private Context mContext;
    public GridViewAdapter(Context context, List<MenuBiz> mDatas, int curIndex, int pageSize) {
        this.mDatas = mDatas;
        this.pageSize = pageSize;
        this.curIndex = curIndex;
        this.mContext=context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position + curIndex * pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mainitem, null);
            vh = new ViewHolder();
            vh.iv = (ImageView) convertView.findViewById(R.id.iv);
            vh.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        //计算一下位置
        int pos = position + curIndex*pageSize;
//        Drawable drawable=mContext.getResources().getDrawable(mDatas.get(pos).getIcon_Rsid());
        Drawable drawable= ContextCompat.getDrawable(mContext,mDatas.get(pos).getIcon_Rsid());
        vh.iv.setImageDrawable(drawable);
//        vh.iv.setImageResource(mDatas.get(pos).getIcon_Rsid());
        vh.tv.setText(mContext.getResources().getString( mDatas.get(pos).getMenu_Name()));
//        Trace.e("Gridview.getView",mContext.getResources().getString( mDatas.get(pos).getMenu_Name()));
        int itemWIth= BaseApplication.WIDTH/ CommDictAction.gridColumn-4;
        AbsListView.LayoutParams layoutParams=new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,itemWIth);
        convertView.setLayoutParams(layoutParams);
        return convertView;
    }

    class ViewHolder {
        public TextView tv;
        public ImageView iv;
    }
}
