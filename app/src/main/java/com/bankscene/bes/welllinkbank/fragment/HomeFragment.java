package com.bankscene.bes.welllinkbank.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.SharedPreferenceUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginActivity;
import com.bankscene.bes.welllinkbank.activity.LoginTabActivity;
import com.bankscene.bes.welllinkbank.activity.MenuList;
import com.bankscene.bes.welllinkbank.activity.finance.TransferActivity;
import com.bankscene.bes.welllinkbank.activity.finance.balance.QueryBalance;
import com.bankscene.bes.welllinkbank.activity.finance.query.QueryCertificateNum;
import com.bankscene.bes.welllinkbank.activity.finance.quoteprice.ForeignQuotePrice;
import com.bankscene.bes.welllinkbank.activity.finance.record.TransactionRecord;
import com.bankscene.bes.welllinkbank.adapter.GridViewAdapter;
import com.bankscene.bes.welllinkbank.adapter.ViewPagerAdapter;
import com.bankscene.bes.welllinkbank.biz.AdvertiseBiz;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.recyclergrid.PageIndicatorView;
import com.bankscene.bes.welllinkbank.recyclergrid.PageRecyclerView;
import com.bankscene.bes.welllinkbank.recyclergrid.RecyclerViewSpacesItemDecoration;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.utils.BannerUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Response;

/**
 * Created by Nylon on 2018/1/26.10:22
 */

public class HomeFragment extends BaseFragment{
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    //    @BindView(R.id.cusom_swipe_view)
//    PageRecyclerView mRecyclerView;
//    @BindView(R.id.indicator)
//    PageIndicatorView indicatorView;
    @BindView(R.id.ll_dot)
    LinearLayout mLlDot;
    @BindView(R.id.viewpager)
    ViewPager mPager;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    private List<Map<String, String>> adMapList = new ArrayList<Map<String, String>>();
    private List<String> adImageUrlList = new ArrayList<String>();
    private ArrayList<MenuBiz> gridDataList;
    private LayoutInflater inflater;
    private int pageCount;
    private int pageSize=9;
    private ArrayList<View> mPagerList;
    private int curIndex=0;
    private String TAG="HomeFragment";
    private final int EVENT_RELOAD_FRAGMENTS=2;

    public void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.app_name),0, " ", R.string.wlb_login,"", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
                CheckLoginState();
            }
        });
        actionBar.setStatusBarHeight();
        actionBar.setTitleImageVisible();
    }
    private void CheckLoginState() {

        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.QueryLoginState)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {

//                        Response response=info.getResponse();
                        String result=info.getRetDetail();
//                        Trace.e("Nylon",result);
                        MessageEvent messageEvent=new MessageEvent(result);
                        messageEvent.setWhat(BaseFragment.QUERYLOGINSTATE);
                        EventBus.getDefault().post(messageEvent);
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        noticeUtils.showNotice(info.getRetDetail());
                    }
                });
    }


    @Override
    protected void initView() {
        TAG=this.getClass().getName();
        EventBus.getDefault().register(this);
        convenientBanner = BannerUtils.initImageBar(activity, convenientBanner, adImageUrlList, adMapList);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseApplication.WIDTH * 4 / 13);
        convenientBanner.setLayoutParams(layoutParams);
        iniBanner2();
        iniViewPager();
        GetAdvertiseMent();
    }

    private void iniViewPager() {
        inflater = LayoutInflater.from(activity);
        //总页数=总数/每页的个数，取整
        pageCount = (int) Math.ceil(gridDataList.size() * 1.0 / pageSize);
        Trace.e(TAG,"size=="+gridDataList.size()+"pagecount=="+pageCount);
        mPagerList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出的一个新实例
            GridView gridView = inflater.inflate(R.layout.gridview, null).findViewById(R.id.grid_1);
            gridView.setAdapter(new GridViewAdapter(activity, gridDataList, i, pageSize));
            mPagerList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent in=null;
                    int pos = position + curIndex * pageSize;
                    int menuName=gridDataList.get(pos).getMenu_Name();
                    Trace.e("menuName==",activity.getResources().getString(menuName));
                    if (menuName==R.string.account_query) {
                        in   =new Intent(activity, WebViewActivity.class);
//                        in.putExtra("url", "https://www.baidu.com");
                        in.putExtra("url", Constant.QUERY_ACCOUNT_LIST);
                    }else if (menuName==R.string.transaction_history) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.ACCOUNT_TRANS_HISTORY);
                    }else if (menuName==R.string.loan_inquiry) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.THIRD_TRANS);
                    }else if (menuName==R.string.synonym_transfer) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.SAMENAME_TRANSFER);
                    }else if (menuName==R.string.my_regular) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.REGULAR_QUERY_LIST);
                    }else if (menuName==R.string.regular_billing) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.REGULAR_OPEN);
                    }else if (menuName==R.string.regular_withdrawal) {
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.REGULAR_CLOSE);
                    }else if (menuName==R.string.custom){
                        in=new Intent(activity,MenuList.class);
                    }else if (menuName==R.string.bank_rate){
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.Query_Rate);
                    }else if (menuName==R.string.exchange_rate_quotation){
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.Query_prce);
                    }else if (menuName==R.string.remittance){
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.APPLY_TRANSFER);
                    }else if (menuName==R.string.my_loans){
                        in   =new Intent(activity, WebViewActivity.class);
                        in.putExtra("url", Constant.MY_LOANS);
                    }
                    if (null!=in)
                        startActivity(in);
                }
            });
        }
        Trace.e(TAG,"size=="+gridDataList.size()+"pagecount=="+pageCount+"mPagerList=="+mPagerList.size() );
//        mPager.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //设置viewpageAdapter
        mPager.setAdapter(new ViewPagerAdapter(mPagerList));
        //设置小圆点
        setOvalLayout();

    }
    private void setOvalLayout() {
        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
        }
        //默认显示第一页
        mLlDot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Trace.e(TAG,"Gridview is visible?"+mPager.getChildAt(0));
                //取消选中
                mLlDot.getChildAt(curIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_normal);
                //选中
                mLlDot.getChildAt(position).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);

                curIndex = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Trace.e(TAG ,"initViewPageFinished");
    }

    @Override
    public void onResume() {
        super.onResume();
        BannerUtils.startTurning(convenientBanner);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onPause() {
        super.onPause();
        BannerUtils.stopTurning(convenientBanner);
    }

    private void iniBanner2() {
//        gridDataList = new FinanceMainBiz(activity).getFinanceData();
        Trace.e(TAG,"开始初始化数据");
        gridDataList=new ArrayList<>();
        String jsonListTest= SharedPreferenceUtil.get(activity,BaseApplication.USER_INDEX,"")+"";
        Trace.e("userindex=====",jsonListTest);
        Type type1=new TypeToken<List<MenuBiz>>(){}.getType();
        ArrayList<MenuBiz> DataList=gson.fromJson(jsonListTest, type1);
        for (int i=0;i<DataList.size();i++){
            if (DataList.get(i).is_Checked()){
                gridDataList.add(DataList.get(i));
            }
        }
        Trace.e("mbs.size==",""+DataList.size());
        MenuBiz menuBiz=new MenuBiz();
        menuBiz.setIs_Checked(true);
        menuBiz.setIcon_Rsid(R.mipmap.wlb_icon_custom);
        menuBiz.setMenu_Name(R.string.custom);
        gridDataList.add(menuBiz);

    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
        switch (messageEvent.getWhat()){
            case REFRESHGRIDVIEW:
                Trace.e(TAG,"EVENT_RELOAD_FRAGMENTS");
                mLlDot.removeAllViews();
                curIndex=0;
                iniBanner2();
                iniViewPager();
                break;
            case QUERYLOGINSTATE:
                String result=messageEvent.getMessage();
                if (result.contains(_REJCODE)&&result.contains("000000")){
                    showNotice(getResources().getString(R.string.logined));
                }else {
                    startActivity(new Intent(activity,LoginTabActivity.class));
                }
                break;

        }
    }

    private void GetAdvertiseMent() {
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        params.put("Location","1");
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.GetAdverTiseMent)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        try {
                            JSONObject json=new JSONObject(info.getRetDetail());
                            String List=json.optString("List");
                            if (!TextUtils.isEmpty(List)){
                                Type type1=new TypeToken<List<AdvertiseBiz>>(){}.getType();
                                ArrayList<AdvertiseBiz> DataList=gson.fromJson(List, type1);
                                for (AdvertiseBiz adb:DataList){
                                    adImageUrlList.add(adb.getPath());
                                    Map map=new HashMap();
                                    map.put("url",adb.getAdvertUrl());
                                    adMapList.add(map);
                                }
                                convenientBanner = BannerUtils.initImageBar(activity, convenientBanner, adImageUrlList, adMapList);
                            }
//asdasda
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        noticeUtils.showNotice(info.getRetDetail());
                    }
                });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Trace.e("mainframent","ondetach");
    }
}
