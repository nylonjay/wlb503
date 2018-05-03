package com.bankscene.bes.welllinkbank.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
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

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.SharedPreferenceUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginTabActivity;
import com.bankscene.bes.welllinkbank.activity.MenuList;
import com.bankscene.bes.welllinkbank.activity.PassWordDialogActivity;
import com.bankscene.bes.welllinkbank.activity.finance.TransferActivity;
import com.bankscene.bes.welllinkbank.activity.finance.balance.QueryBalance;
import com.bankscene.bes.welllinkbank.activity.finance.query.QueryCertificateNum;
import com.bankscene.bes.welllinkbank.activity.finance.quoteprice.ForeignQuotePrice;
import com.bankscene.bes.welllinkbank.activity.finance.record.TransactionRecord;
import com.bankscene.bes.welllinkbank.adapter.GridViewAdapter;
import com.bankscene.bes.welllinkbank.adapter.ViewPagerAdapter;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.bankscene.bes.welllinkbank.recyclergrid.PageIndicatorView;
import com.bankscene.bes.welllinkbank.recyclergrid.PageRecyclerView;
import com.bankscene.bes.welllinkbank.recyclergrid.RecyclerViewSpacesItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

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
 * Created by Nylon on 2018/1/26.10:22
 */

public class FInanceFragment extends BaseFragment implements View.OnClickListener{
    //    @BindView(R.id.cusom_swipe_view)
//    CustomGridView cusom_swipe_view;
    @BindView(R.id.re_content)
    RelativeLayout re_content;
    @BindView(R.id.ll_2transfer)
    LinearLayout ll_2transfer;
    @BindView(R.id.ll_third)
    LinearLayout ll_third;
    @BindView(R.id.ll_my_acc)
    LinearLayout ll_my_acc;
    @BindView(R.id.finace_login)
    ImageView finance_login;

    @BindView(R.id.ll_dot)
    LinearLayout mLlDot;
    @BindView(R.id.viewpager)
    ViewPager mPager;

    private LayoutInflater inflater;
    private int pageCount;
    private int pageSize=9;
    private ArrayList<View> mPagerList;
    private int curIndex=0;
    private ArrayList<MenuBiz> gridDataList;
    @Override
    public void setActionBar() {
//        super.setActionBar();
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.navigationBarWithKitkatEnable(false).init();
    }


    @Override
    protected void initView() {
        super.initView();
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BaseApplication.WIDTH * 6 / 13);
        re_content.setLayoutParams(layoutParams);
        iniBanner2();
        iniViewPager();
        ll_2transfer.setOnClickListener(this);
        ll_my_acc.setOnClickListener(this);
        ll_third.setOnClickListener(this);
        finance_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CheckLoginState();
//                startActivity(new Intent(activity, LoginTabActivity.class));
            }
        });
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

                        String result=info.getRetDetail();
//                        Trace.e("Nylon",result);
                        Message msg=new Message();
                        msg.what=QUERYLOGINSTATE;
                        msg.obj=result;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        noticeUtils.showNotice(info.getRetDetail());
                    }
                });
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 111:
                    String result=msg.obj+"";
                if (result.contains(_REJCODE)&&result.contains("000000")){
                    showNotice(getResources().getString(R.string.logined));
                }else {
                    startActivity(new Intent(activity,LoginTabActivity.class));
                }
                    break;
            }
        }
    };

//    @Subscribe(threadMode = ThreadMode.MainThread)
//    public void onMessageEventMainThread(MessageEvent messageEvent){
//        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
//        switch (messageEvent.getWhat()){
//            case QUERYLOGINSTATE:
//                String result=messageEvent.getMessage();
//                if (result.contains(_REJCODE)&&result.contains("000000")){
//                    showNotice(getResources().getString(R.string.logined));
//                }else {
//                    startActivity(new Intent(activity,LoginTabActivity.class));
//                }
//                break;
//
//        }
//    }

    @Override
    protected boolean isLazyLoad() {
        return true;

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


//    private void initSwipeView() {
//
//        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
////        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION,2);//top间距
//
//        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION,2);//底部间距
//
////        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION,2);//左间距
//
//        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION,4);//右间距
//
//        mRecyclerView.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
//        //不知道为什么只能加横向的间距  于是我就在Item里面在底部加了一条线
//
////        GridLayoutManager layoutManager =
////                new GridLayoutManager(activity, CommDictAction.gridColumn, GridLayoutManager.VERTICAL, false);
////        mRecyclerView.setLayoutManager(layoutManager);
////        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(CommDictAction.gridColumn, 2, true));
//        mRecyclerView.setIndicator(indicatorView);
////          final ArrayList<HashMap<String, Object>> dataList=new FinanceMainBiz(activity).getData();
//        // 设置行数和列数
//        mRecyclerView.setPageSize(CommDictAction.gridRow, CommDictAction.gridColumn);
//        // 设置页间距
////        mRecyclerView.setPageMargin(0);
//        // 设置数据
//        mRecyclerView.setAdapter(myAdapter = mRecyclerView.new PageAdapter(gridDataList, new PageRecyclerView.CallBack() {
//            @Override
//            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(activity).inflate(R.layout.mainitem, parent, false);
//                return new MyHolder(view);
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//                ((FInanceFragment.MyHolder) holder).tv.setText(getResources().getString(gridDataList.get(position).getMenu_Name()));
//                ((FInanceFragment.MyHolder) holder).image.setBackgroundResource(gridDataList.get(position).getIcon_Rsid());
//            }
//
//            @Override
//            public void onItemClickListener(View view, int position) {
//                Intent in=null;
//                int menuName= (int) gridDataList.get(position).getMenu_Name();
//                if (menuName==R.string.account_query) {
//                    in   =new Intent(activity, WebViewActivity.class);
////                        in.putExtra("url", "https://www.baidu.com");
//                    in.putExtra("url", Constant.QUERY_ACCOUNT_LIST);
//                }else if (menuName==R.string.transaction_history) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.ACCOUNT_TRANS_HISTORY);
//                }else if (menuName==R.string.loan_inquiry) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.THIRD_TRANS);
//                }else if (menuName==R.string.synonym_transfer) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.SAMENAME_TRANSFER);
//                }else if (menuName==R.string.my_regular) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.REGULAR_QUERY_LIST);
//                }else if (menuName==R.string.regular_billing) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.REGULAR_OPEN);
//                }else if (menuName==R.string.regular_withdrawal) {
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.REGULAR_CLOSE);
//                }else if (menuName==R.string.custom){
//                    in=new Intent(activity,MenuList.class);
//
//                }else if (menuName==R.string.bank_rate){
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.Query_Rate);
//                }else if (menuName==R.string.exchange_rate_quotation){
//                    in   =new Intent(activity, WebViewActivity.class);
//                    in.putExtra("url", Constant.Query_prce);
//                }
////                else if (menuName==R.string.my_loans){
////                    in   =new Intent(activity, WebViewActivity.class);
////                    in.putExtra("url", Constant.testdwdw);
////                }
//                if (null!=in)
//                    startActivity(in);
//
//            }
//
//            @Override
//            public void onItemLongClickListener(View view, int position) {
//
//            }
//
////            @Override
////            public void onItemLongClickListener(View view, int position) {
////                Toast.makeText(activity, "删除："
////                        + gridDataList.get(position), Toast.LENGTH_SHORT).show();
////                myAdapter.remove(position);
////            }
//        }));
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void iniBanner2() {
        if (null!=gridDataList){
            gridDataList.clear();
        }else {
            gridDataList=new ArrayList<>();
        }
        String jsonListTest= SharedPreferenceUtil.get(activity,BaseApplication.USER_INDEX,"")+"";
        Type type1=new TypeToken<List<MenuBiz>>(){}.getType();
        ArrayList<MenuBiz> DataList=gson.fromJson(jsonListTest, type1);
        for (int i=0;i<DataList.size();i++){
            gridDataList.add(DataList.get(i));
        }

        Trace.e("gridDatalits.size==",gridDataList.size()+"");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ll_2transfer:
                Intent in=new Intent(activity, WebViewActivity.class);
                in.putExtra("url", Constant.SAMENAME_TRANSFER);
                startActivity(in);
                break;
            case R.id.ll_my_acc:
                in   =new Intent(activity, WebViewActivity.class);
//                        in.putExtra("url", "https://www.baidu.com");
                in.putExtra("url", Constant.QUERY_ACCOUNT_LIST);
                startActivity(in);
                break;
            case R.id.ll_third:
//                in   =new Intent(activity, WebViewActivity.class);
////                        in.putExtra("url", "https://www.baidu.com");
//                in.putExtra("url", Constant.THIRD_TRANS_CONFIRM);
//                startActivity(in);
//                in = new Intent(activity, LoginSMSVerify.class);
//                in.putExtra("timestamp",System.currentTimeMillis()+"");
//                startActivity(in);
                in   =new Intent(activity, WebViewActivity.class);
                in.putExtra("url", Constant.APPLY_TRANSFER);
                startActivity(in);
                break;
        }
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
        return R.layout.fragment_second;
    }
}
