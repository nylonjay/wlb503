package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bankscene.bes.welllinkbank.fragment.logintypes.BaseTabFragment;

import java.util.List;

/**
 * Package_name:com.hero.zhaoq.navigationbgsliding.adapter
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/9/8  12:40
 *
 *  注意继承自   FragmentPagerAdapter
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<BaseTabFragment> lists;

    public FragmentAdapter(FragmentManager fm, Context context, List<BaseTabFragment> lists) {
        super(fm);
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        int temp = 0;
        if(lists!=null){
            temp = lists.size();
        }
        return temp;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Fragment getItem(int position) {
        return lists.get(position);
    }

    //获取  title  字体
    @Override
    public CharSequence getPageTitle(int position) {

        String ret = null;

        ret = lists.get(position).getFragmentTitle();

        return ret;
    }
}
