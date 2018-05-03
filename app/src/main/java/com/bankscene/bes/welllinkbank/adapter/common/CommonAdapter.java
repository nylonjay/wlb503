package com.bankscene.bes.welllinkbank.adapter.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter
{
    protected LayoutInflater inflater;

    protected Context context;

    protected List<T> data;

    protected final int itemLayoutId;

    protected int countSum = -1;

    public CommonAdapter(Context context, List<T> data, int itemLayoutId)
    {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.data = data;
        this.itemLayoutId = itemLayoutId;
    }

    /**
     * 替换元素并刷新
     *
     * @param mDatas
     */
    public void refresh(List<T> mDatas)
    {
        this.data = mDatas;
        this.notifyDataSetChanged();
    }

    /**
     * 删除元素并更新
     *
     * @param position
     */
    public void deleteList(int position)
    {
        this.data.remove(position);
        this.notifyDataSetChanged();
    }

    /**
     * 定义itemCount
     */
    public CommonAdapter setCount(int i)
    {
        countSum = i;
        this.notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount()
    {
        if (countSum == -1)
        {
            return data.size();
        }
        else
        {
            return countSum;
        }
    }

    @Override
    public T getItem(int position)
    {
        if (countSum == -1)
        {
            return data.get(position);
        }
        else
        {
            return data.get(countSum % data.size());
        }

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, position, getItem(position));
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, int position, T item);

    private ViewHolder getViewHolder(int position, View convertView,
                                     ViewGroup parent)
    {
        return ViewHolder.get(context, convertView, parent, itemLayoutId,
                position);
    }

}
