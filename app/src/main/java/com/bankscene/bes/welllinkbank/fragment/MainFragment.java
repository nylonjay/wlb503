package com.bankscene.bes.welllinkbank.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginTabActivity;
import com.bankscene.bes.welllinkbank.activity.MenuList;
import com.bankscene.bes.welllinkbank.biz.AdvertiseBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.recyclergrid.PageIndicatorView;
import com.bankscene.bes.welllinkbank.recyclergrid.PageRecyclerView;
import com.bankscene.bes.welllinkbank.recyclergrid.RecyclerViewSpacesItemDecoration;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.utils.BannerUtils;
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
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Nylon on 2018/2/2.11:03
 */

public class MainFragment extends BaseFragment{
    //    @BindView(R.id.banner)
//    Banner banner;
    @BindView(R.id.cusom_swipe_view)
    PageRecyclerView mRecyclerView;
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    @BindView(R.id.indicator)
    PageIndicatorView indicatorView;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    private ArrayList<MenuBiz> gridDataList;
    private PageRecyclerView.PageAdapter myAdapter;
    private List<Map<String, String>> adMapList = new ArrayList<Map<String, String>>();
    private List<String> adImageUrlList = new ArrayList<String>();

    @Override
    public void setActionBar() {

        actionBar.setActionBar(getResources().getString(R.string.app_name), 0, " ", R.string.wlb_login,"", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {

            }

            @Override
            public void onRightClick() {
                CheckLoginState();
//                if (!State.isLogin)
//                    activity.startActivity(new Intent(activity, LoginActivity.class));
//                else
//                    ToastUtils.showShortToast("已登陆");
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
                        messageEvent.setWhat(1);
                        EventBus.getDefault().post(messageEvent);


                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        noticeUtils.showNotice(info.getRetDetail());
                    }
                });
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
    protected void initView() {
//        Map url=new HashMap();
//        url.put("url","http://www.wlbank.com.mo/cn/contact");
//        adMapList.add(url);

        convenientBanner = BannerUtils.initImageBar(activity, convenientBanner, adImageUrlList, adMapList);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseApplication.WIDTH * 4 / 13);
        convenientBanner.setLayoutParams(layoutParams);
        EventBus.getDefault().register(this);
        iniBanner2();
        iniSwipePre();
        initSwipeView();
        GetAdvertiseMent();


    }


    @Override
    protected void initData() {
        super.initData();

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
        switch (messageEvent.getWhat()){
            case 1:
                String result=messageEvent.getMessage();
                if (result.contains(_REJCODE)&&result.contains("000000")){
                    showNotice(getResources().getString(R.string.logined));
                }else {
//                    DBHelper.getInstance().clear();
                    startActivity(new Intent(activity,LoginTabActivity.class));
                }
//                refreshSelf();
                break;
        }
    }
    private void iniSwipePre(){
        Trace.e("mainf","gridlistsize=="+gridDataList.size());
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
//        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,2);//top间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,2);//底部间距

//        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION,2);//左间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION,4);//右间距

        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        //不知道为什么只能加横向的间距  于是我就在Item里面在底部加了一条线

//        GridLayoutManager layoutManager =
//                new GridLayoutManager(activity, CommDictAction.gridColumn, GridLayoutManager.VERTICAL, false);
//        mRecyclerView.setLayoutManager(layoutManager);
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(CommDictAction.gridColumn, 2, true));
        mRecyclerView.setIndicator(indicatorView);
//          final ArrayList<HashMap<String, Object>> dataList=new FinanceMainBiz(activity).getData();
        // 设置行数和列数
        mRecyclerView.setPageSize(CommDictAction.gridRow, CommDictAction.gridColumn);
    }
    private void initSwipeView() {
        mRecyclerView.setAdapter(myAdapter = mRecyclerView.new PageAdapter(gridDataList, new PageRecyclerView.CallBack() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(activity).inflate(R.layout.mainitem, parent, false);
                return new MyHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ((MyHolder) holder).tv.setText(getResources().getString(gridDataList.get(position).getMenu_Name()));
                ((MyHolder) holder).image.setBackgroundResource(gridDataList.get(position).getIcon_Rsid());


            }

            @Override
            public void onItemClickListener(View view, int position) {
                Intent in=null;
                int menuName=gridDataList.get(position).getMenu_Name();
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
                }
                if (null!=in)
                    startActivity(in);
            }

            @Override
            public void onItemLongClickListener(View view, int position) {
//                Toast.makeText(activity, "删除："
//                        + gridDataList.get(position), Toast.LENGTH_SHORT).show();
//                myAdapter.remove(position);
            }
        }));

    }
    public class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv = null;
        public ImageView image=null;
        public MyHolder(View itemView) {
            super(itemView);
            image=(ImageView) itemView.findViewById(R.id.iv);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
    @Override
    protected int setLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onResume() {
        super.onResume();Trace.e("MainFragment","pagesize==");
//        iniBanner2();
//        initSwipeView();
//        mRecyclerView.ResetIndicator();

        BannerUtils.startTurning(convenientBanner);
    }

    @Override
    public void onPause() {
        super.onPause();
//        banner.stopAutoPlay();
        BannerUtils.stopTurning(convenientBanner);
    }

    private void iniBanner2() {
        if (null!=gridDataList){
            gridDataList.clear();
        }else {
            gridDataList=new ArrayList<>();
        }
//        String jsonListTest=SharedPreferenceUtil.get(activity,BaseApplication.USER_INDEX,"")+"";
//        Type type1=new TypeToken<List<MenuBiz>>(){}.getType();
//        ArrayList<MenuBiz> DataList=gson.fromJson(jsonListTest, type1);
//        for (int i=0;i<DataList.size();i++){
//            if (DataList.get(i).is_Checked()){
//                gridDataList.add(DataList.get(i));
//            }
//        }
//        MenuBiz menuBiz=new MenuBiz();
//        menuBiz.setIs_Checked(true);
//        menuBiz.setIcon_Rsid(R.mipmap.wlb_icon_custom);
//        menuBiz.setMenu_Name(activity.getResources().getString(R.string.custom));
//        gridDataList.add(menuBiz);

        Trace.e("gridDatalits.size==",gridDataList.size()+"");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Trace.e("mainframent","ondetach");
        mRecyclerView=null;
        myAdapter=null;
        gridDataList=null;
        indicatorView=null;
    }
}
