package com.bankscene.bes.welllinkbank.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.LoginSmsDialog;
import com.bankscene.bes.welllinkbank.activity.LoginTabActivity;
import com.bankscene.bes.welllinkbank.activity.MenuList;
import com.bankscene.bes.welllinkbank.adapter.GridViewAdapter;
import com.bankscene.bes.welllinkbank.adapter.ViewPagerAdapter;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.biz.MenuBiz;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.WebViewActivity;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

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
                    }else if (menuName==R.string.shares_query){
//                        in   =new Intent(activity, LoginSmsDialog.class);
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
    }

    private void iniBanner2() {
        if (null!=gridDataList){
            gridDataList.clear();
        }else {
            gridDataList=new ArrayList<>();
        }
        FinanceMainBiz fbz=new FinanceMainBiz(activity);
        Object[] icons=fbz.getAllIcons();
        Object[] names=fbz.GetAllMenuNames();
        int menuLength=icons.length;
        for (int i=0;i<menuLength;i++){
            MenuBiz menuBiz=new MenuBiz();
            menuBiz.setIcon_Rsid((Integer) icons[i]);
            menuBiz.setMenu_Name((Integer) names[i]);
            menuBiz.setIs_Checked(true);
            gridDataList.add(menuBiz);
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
