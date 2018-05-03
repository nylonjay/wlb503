package com.bankscene.bes.welllinkbank.view;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.DesityUtils;
import com.bankscene.bes.welllinkbank.adapter.PhonePreAdapter;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

public class PhonPrePopWindow extends PopupWindow {
    private ListView listView;
    private View mMenuView;
    private Context mContext;
    /* //模拟数据
     private List<String> groups;*/
    private String[] pres;
    private TextView tv_kind;
    private FragmentActivity context;
//    private TextView tv_bg_detail;

    public PhonPrePopWindow(FragmentActivity context,TextView tv_kind) {
        super(context);
        this.mContext=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_pop_numberpre, null);
        listView = (ListView) mMenuView.findViewById(R.id.listview);
        this.tv_kind=tv_kind;
        this.context=context;
//        this.tv_bg_detail=tv_bg_detail;

        addListener(listView,this);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽——>匹配不同机型的适配
        int screenWith=context.getWindowManager().getDefaultDisplay().getWidth();
        this.setWidth((screenWith*7)/12);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置允许在外点击消失
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //  this.setAnimationStyle(R.style.AnimBottom);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.base_function_block));

    }

    private void addListener(ListView listView, final PhonPrePopWindow selectKindPopWin) {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tv_kind.setText(groups_catogry.get(position).get("name"));
                // TODO Auto-generated method stub
                MessageEvent messageEvent=new MessageEvent(pres[position]);
                messageEvent.setWhat(2);
                EventBus.getDefault().post(
                        messageEvent);
                if (selectKindPopWin != null) {
                    selectKindPopWin.dismiss();
                }
//                resetTextColor();
            }
        });
        selectKindPopWin.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                MessageEvent messageEvent=new MessageEvent("dismiss");
                messageEvent.setWhat(Constant.DISMISS);
                EventBus.getDefault().post(messageEvent);
            }
        });
    }

//    private void resetTextColor(){
//
//        for (int i = 0; i < pres.length; i++) {
//            TextView tv=(TextView)listView.getChildAt(i).findViewById(R.id.tv_item_popupwindow);
//            if (pres[i].equals(tv_kind.getText().toString())){
//                tv.setTextColor(mContext.getResources().getColor(R.color.main_theme_color));
//            }else
//                tv.setTextColor(mContext.getResources().getColor(R.color.greyText));
//        }
//    }

    public void addNetData(){
//        ACache aCache=ACache.get(context);
//        String local=aCache.getAsString(BaseConfig.HOME_GRID_DATA);
//        if (local!=null){
//            Gson_Category categoryBean = GsonHelper.getPerson(local, Gson_Category.class);
        pres=new String[]{"+86","+853","+852",mContext.getResources().getString(R.string.others)};
//        for (int i = 0; i < pres.length; i++) {
//            Map<String,String> map=new HashMap<String,String>();
//            map.put("name",pres[i]);
//        }
        PhonePreAdapter groupAdapter = new PhonePreAdapter(context, pres,tv_kind);
        listView.setAdapter(groupAdapter);
    }
    public void showPopWin(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int xPos=-1;
//        int xPos=-this.getWidth()/2+view.getWidth()/2;
        addNetData();
        this.setWidth(DesityUtils.dp2px(mContext,80));
        this.showAsDropDown(view, xPos, 0);
//        if (this.isShowing()){
//            tv_bg_detail.setVisibility(View.VISIBLE);
//        }
    }
}