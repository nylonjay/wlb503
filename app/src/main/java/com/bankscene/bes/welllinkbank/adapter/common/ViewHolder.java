package com.bankscene.bes.welllinkbank.adapter.common;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ViewHolder {
    private final SparseArray<View> mViews;

    private int mPosition;

    private View mConvertView;

    private ViewHolder(Context context, ViewGroup parent, int layoutId,
                       int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param string
     * @return
     */
    public ViewHolder setText(int viewId, String string) {
        TextView view = getView(viewId);
        view.setText(string);
        return this;
    }

    /**
     * 获取edit文本
     *
     * @param viewId
     * @return
     */
    public String getEditText(int viewId) {
        EditText ed = getView(viewId);
        String str = ed.getText().toString();
        return str;
    }

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param imagePath (int Bitmap String)
     * @param type
     * @return
     */

    public ViewHolder setImage(Context context, int viewId, Object imagePath, int type) {
        ImageView view = getView(viewId);
        try {
            switch (type) {
                case ImageShape.NORMAL:
                    Glide.with(context).load(imagePath)
                            .into(view);
                    break;
                case ImageShape.CIRCLE:
                    Glide.with(context).load(imagePath)
                            .transform(new GlideCircleTransform(context))
                            .into(view);
                    break;
                case ImageShape.ROUND:
                    Glide.with(context).load(imagePath)
                            .transform(new GlideRoundTransform(context))
                            .into(view);
                    break;
            }
        } catch (Exception e) {
//            Toast.makeText(context, "图片加载失败！", Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    public ViewHolder setImage(Context context, int viewId, Object imagePath, int type,
                               int defaultImage, int errorImage) {
        ImageView view = getView(viewId);
        try {
            switch (type) {
                case ImageShape.NORMAL:
                    Glide.with(context).load(imagePath)
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(view);
                    break;
                case ImageShape.CIRCLE:
                    Glide.with(context).load(imagePath)
                            .transform(new GlideCircleTransform(context))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(view);
                    break;
                case ImageShape.ROUND:
                    Glide.with(context).load(imagePath)
                            .transform(new GlideRoundTransform(context))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(view);
                    break;
            }
        } catch (Exception e) {
//            Toast.makeText(context, "图片加载失败！", Toast.LENGTH_SHORT).show();
        }
        return this;
    }

    /**
     * 给view设置背景色
     *
     * @param viewId
     * @param color
     * @return
     */
    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public int getPosition() {
        return mPosition;
    }

}
