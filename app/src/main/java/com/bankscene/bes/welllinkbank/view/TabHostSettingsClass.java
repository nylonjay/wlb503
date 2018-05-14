package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;

/**
 * Created by Nylon on 2018/5/1.23:24
 */

public class TabHostSettingsClass {
    private TabHost tabHost;
    private TabWidget tabWidget;
    private Context context;
    public TabHostSettingsClass(Context context, TabHost tabHost, TabWidget tabWidget) {
        this.context=context;
        this.tabHost=tabHost;
        this.tabWidget=tabWidget;
    }
    public void setChangeTabSetting(){
//        tabWidget.setBackgroundResource(R.drawable.login_bg_edits);
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            TextView tView = (TextView) tabWidget.getChildAt(i).findViewById(
                    android.R.id.title);
            //修改背景
//            tabWidget.getChildAt(i).setBackgroundResource(
//                    R.color.main_theme_color);
            tView.setTextSize(13);
            //字体
            tView.setTypeface(Typeface.DEFAULT);
            if (tabHost.getCurrentTab() == i) {
                tabWidget.getChildAt(i).setBackgroundResource(
                        R.color.white);
                tView.setTextColor(context.getResources().getColorStateList(
                        R.color.blackText));
            } else {
                tabWidget.getChildAt(i).setBackgroundResource(
                        R.color.light_grey);
                tView.setTextColor(context.getResources().getColorStateList(
                        android.R.color.white));
            }
        }
    }
}
