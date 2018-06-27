package com.bankscene.bes.welllinkbank.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.notice.NoticeUtils;
import com.bankscene.bes.welllinkbank.activity.LoginTablayoutActivity;
import com.bankscene.bes.welllinkbank.activity.mine.AboutUs;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset;
import com.bankscene.bes.welllinkbank.activity.mine.PersonInfo;
import com.bankscene.bes.welllinkbank.adapter.MineAdapter;
import com.bankscene.bes.welllinkbank.adapter.common.ImageShape;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by Nylon on 2018/2/1.10:44
 */

public class MineFragment extends BaseFragment{
    //    @BindView(R.id.actionBar)
//    TranslucentActionBar actionBar;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.mlistview)
    ListView mlistview;
    @BindView(R.id.iv_head)
    ImageView iv_head;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone_num)
    TextView tv_phone_num;
    private ArrayList<HashMap<String, Object>> gridDataList = new ArrayList<HashMap<String, Object>>();
    private SimpleAdapter adapter;
    private int[] images;
    private String[] names;
    private User user;
    NoticeUtils noticeUtils;
    private static final String RESET_HEAD_IMG ="RESET_HEAD_IMG" ;
    @Override
    public void setActionBar() {
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.navigationBarWithKitkatEnable(false).init();
//        setStatusBarUpperAPI21();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG="MineFragment";
        user=BaseApplication.getInstance().getUser();
        EventBus.getDefault().register(this);
        noticeUtils=new NoticeUtils(activity);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (State.isLogin){
            tv_name.setText(user.getName()+"  " +user.getEnglish_name());
            tv_phone_num.setText(user.getMobileNum());
            if (!TextUtils.isEmpty(DBHelper.getDataByKey(DataKey.userIcon))){
//            FileUtil.base64ToBitmap(user.getImage_base64(),BaseApplication.baseImagePath);
                setImage(iv_head, DBHelper.getDataByKey(DataKey.userIcon), ImageShape.CIRCLE,
                        R.mipmap.icon_photo, R.mipmap.icon_photo);

            }
        }else {
            setImage(iv_head,"", ImageShape.CIRCLE,
                    R.mipmap.icon_photo, R.mipmap.icon_photo);
            tv_name.setText(getResources().getString(R.string.empty));
            tv_phone_num.setText(getResources().getString(R.string.empty));
            DBHelper.insert(new Data(DataKey.cookie,""));
        }

    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
        switch (messageEvent.getMessage()){
            case Constant.LOGOUT_SUCCEED:
                if (!State.isLogin){
                    setImage(iv_head,"", ImageShape.CIRCLE,
                            R.mipmap.icon_photo, R.mipmap.icon_photo);
                    tv_name.setText(getResources().getString(R.string.empty));
                    tv_phone_num.setText(getResources().getString(R.string.empty));
                    DBHelper.insert(new Data(DataKey.cookie,""));
                }
                break;



        }
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initData() {
        super.initData();
        images=new int[]{R.mipmap.wlb_icon_smallhead,R.mipmap.wlb_icon_codesetting,R.mipmap.wlb_icon_language
                ,0,
//                R.mipmap.wlb_icon_service,
                R.mipmap.wlb_icon_aboutas,R.mipmap.wlb_icon_smallhead};
        names=new String[]{
                activity.getResources().getString(R.string.personal_info),
                activity.getResources().getString(R.string.code_setting),
                activity.getResources().getString(R.string.language),
                " ",
//                activity.getResources().getString(R.string.client_service),
                activity.getResources().getString(R.string.about_us),
                activity.getResources().getString(R.string.quit)};
    }

    @Override
    protected void initView() {
        final ArrayList<String> datas=new ArrayList<>();
        datas.add("繁體中文");
        datas.add("English");
        MineAdapter mineAdapter=new MineAdapter(images,names,activity);
        mlistview.setAdapter(mineAdapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch((int) id){
                    case 0:
                       CheckLoginState1();
                        break;
                    case 1:
                        CheckLoginState();
                        break;
                    case 2:
                        showAccountList2(datas);
                        break;
                    case 4:
                        startActivity(new Intent(activity, AboutUs.class));
                        break;
                    case 5:
                        if (State.isLogin)
                        LogOut();
                        break;
                }
            }
        });
    }

    private void CheckLoginState1() {
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
                        Trace.e("Nylon",result);
                        if (result.contains(_REJCODE)&&result.contains("000000")){
                            Intent in=new Intent(activity,PersonInfo.class);
                            activity.startActivity(in);
                        }else {
//                    DBHelper.getInstance().clear();
                            Intent in=new Intent(activity,LoginTablayoutActivity.class);
                            in.putExtra(activity.getResources().getString(R.string.className),PersonInfo.class.getName());
                            activity.startActivity(in);
                        }


                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {

                    }
                });
    }


    protected void doHttpAsync(HttpInfo info, final Callback callback){
        setProgressDisplay(true);
        OkHttpUtil.getDefault(this).doAsync(info, new Callback() {
            @Override
            public void onSuccess(HttpInfo info) throws IOException {
//                getLoadingDialog().dismiss();
                setProgressDisplay(false);
                callback.onSuccess(info);
            }

            @Override
            public void onFailure(HttpInfo info) throws IOException {
                setProgressDisplay(false);
                callback.onFailure(info);
            }
        });
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
                        String result=info.getRetDetail();
                        Trace.e("Nylon",result);
                        if (result.contains(_REJCODE)&&result.contains("000000")){
                            Intent in=new Intent(activity,CodeReset.class);
                            activity.startActivity(in);
                        }else {
//                    DBHelper.getInstance().clear();
                            Intent in=new Intent(activity,LoginTablayoutActivity.class);
                            in.putExtra(activity.getResources().getString(R.string.className),CodeReset.class.getName());
                            activity.startActivity(in);
                        }


                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {

                    }
                });
    }

    public void LogOut(){
        Map params = new HashMap();
        params.put("_ChannelId","PMBS");
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.logout)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))//添加头参数
//                        .addParam("param", "test")//添加接口参数
                        .addParams(params)
//                        .setDelayExec(2, TimeUnit.SECONDS)//延迟2秒执行
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        String result = info.getRetDetail();
                    }

                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Trace.e("logout_",info.getRetDetail().toString());
                        try {
                            JSONObject result= new JSONObject(info.getRetDetail());
                            if (result.has(_REJCODE)&&result.get(_REJCODE).equals("000000")){
                                noticeUtils.showNotice(getResources().getString(R.string.logout_succed));
                                setImage(iv_head,"", ImageShape.CIRCLE,
                                        R.mipmap.icon_photo, R.mipmap.icon_photo);
                                tv_name.setText(getResources().getString(R.string.empty));
                                tv_phone_num.setText(getResources().getString(R.string.empty));
                                DBHelper.insert(new Data(DataKey.cookie,""));
//                                DBHelper.getInstance().clear();
                                State.isLogin=false;
//                                DBHelper.getInstance().clear();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    private void refreshSelf() {
        Intent intent=new Intent(activity,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    break;
            }
        }
    };




    @Override
    protected int setLayoutId() {
        return R.layout.fragment_me;
    }

}
