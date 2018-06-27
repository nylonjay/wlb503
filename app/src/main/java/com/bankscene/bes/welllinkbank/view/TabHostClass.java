package com.bankscene.bes.welllinkbank.view;

import android.widget.TabHost;

/**
 * Created by Nylon on 2018/5/1.23:23
 */

public class TabHostClass {

    private TabHost tabHost;
    private String one;
    private String two;
    private String three;
    private int v1;
    private int v2;
    private int v3;
    public TabHostClass(TabHost tabHost,int v1,int v2,int v3,String one,String two ,String three) {
        this.tabHost=tabHost;
        this.one=one;
        this.two=two;
        this.three=three;
        this.v1=v1;
        this.v2=v2;
        this.v3=v3;
    }


    public TabHostClass(TabHost tabHost,int v1,int v2,String one,String two) {
        this.tabHost=tabHost;
        this.one=one;
        this.two=two;
        this.v1=v1;
        this.v2=v2;
    }

    public  void setTabHost(){
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("1").setContent(v1).setIndicator(one));
        tabHost.addTab(tabHost.newTabSpec("2").setContent(v2).setIndicator(two));
        tabHost.addTab(tabHost.newTabSpec("3").setContent(v3).setIndicator(three));
    }
}
