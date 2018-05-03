package com.bankscene.bes.welllinkbank.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.fragment.HomeFragment;
import com.bankscene.bes.welllinkbank.fragment.NoticeFragment;
import com.bankscene.bes.welllinkbank.fragment.MainFragment;
import com.bankscene.bes.welllinkbank.fragment.MineFragment;
import com.bankscene.bes.welllinkbank.fragment.FInanceFragment;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;


/**
 * Created by tianwei on 2015/12/22.
 */
public class MainPagerAdapter extends BaseFragmentPagerAdapter {

    private Context context;
    private String[] titles = new String[4];
    private static int[] iconUnselected = new int[]{R.string.wlb_home_nomal, R.string.wlb_finance_nomal,
            R.string.wlb_notice_normal,R.string.wlb_mine_normal
//            , R.string.icons_life0, R.string.icons_user0
    };
    private static int[] iconSelected = new int[]{R.string.wlb_home_selected, R.string.wlb_finance_selected,
            R.string.wlb_notice_selected,R.string.wlb_mine_selected
// R.string.icons_life1, R.string.icons_user1
    };

    public static int previous = -1;
    public MainPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles[0]=context.getResources().getString(R.string.Home);
        titles[1]=context.getResources().getString(R.string.finance);
        titles[2]=context.getResources().getString(R.string.Notice);
        titles[3]=context.getResources().getString(R.string.Mine);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FInanceFragment();
            case 2:
                return new NoticeFragment();
            case 3:
                return new MineFragment();
//            case 3:
//                return new FourthFragment();
//            case 4:
//                return new AccountFragment();
            default:
                return new MainFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    public View getTabView(final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tablayout, null);

        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(titles[position]);

        IconFontTextView icon = (IconFontTextView) view.findViewById(R.id.icon);
        icon.setText(context.getResources().getString(iconUnselected[position]));
        return view;
    }

    public static int getSelectedIcon(int position) {
        return iconSelected[position];
    }

    public static int getUnselectedIcon(int position) {
        return iconUnselected[position];
    }
}

