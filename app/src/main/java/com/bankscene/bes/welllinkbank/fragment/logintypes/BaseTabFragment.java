package com.bankscene.bes.welllinkbank.fragment.logintypes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.mine.password.CodeReset2;
import com.bankscene.bes.welllinkbank.biz.ClientInfoBiz;
import com.bankscene.bes.welllinkbank.biz.NameImgPair;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.BaseFragment;
import com.bankscene.bes.welllinkbank.core.State;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.WlbEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by Nylon on 2018/6/4.17:13
 */

public abstract class BaseTabFragment extends BaseFragment {
    private final int SHOW_DIALOG = 0x001;
    private final int DISMISS_DIALOG = 0x002;
    private final int LOAD_SUCCEED = 0x003;
    private final int LOAD_FAILED = 0x004;
    protected String timestamp;
    protected String hms;
    protected String dbp;
    private final String requestTag = "download-tag-1001";//请求标识
    protected WlbEditText CurrentEditext;
    private User user;
    protected int LOGIN_TYPE=3;

    String redirectName;
    //用于获取  头部  导航字体
    protected String key="redirect";
    protected final int SHOW_PRE_TEXT=2;
    public abstract String getFragmentTitle();
    protected void GetTimeStampAndKeyWithoutEditor() {
        QueryPublicKey();
        RefreshTimeStamp();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        ButterKnife.bind(activity);
//        EventBus.getDefault().register(this);
        user= BaseApplication.getInstance().getUser();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (null!=getArguments())
        redirectName= (String) getArguments().get(key);
    }
    /**
     * 异步请求
     * @param info 请求信息体
     * @param callback 结果回调接口
     */
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
    protected void doHttpAsyncWhioutDialog(HttpInfo info, final Callback callback){
//        getMainHandler().sendEmptyMessage(SHOW_DIALOG);
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

    public void RefreshTimeStamp(){
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(false);
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.RefreshTimeStamp)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String redetail=info.getRetDetail();
                        try {
                            JSONObject result=new JSONObject(redetail);
                            timestamp=result.getString("Timestamp");
                            if (null!=CurrentEditext)
                                CurrentEditext.setTimestamp(timestamp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                    }
                });
    }
    public void QueryPublicKey(){
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(false);
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .setUrl(CommDictAction.QryPublicKey)
                        .setRequestType(RequestType.POST)//设置请求方式
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String redetail=info.getRetDetail();
                        try {
                            JSONObject result=new JSONObject(redetail);
                            dbp=result.optString("DbpPublicKey");
                            hms=result.optString("HsmPublicKey");
                            if (null!=CurrentEditext){
                                CurrentEditext.setDbp(dbp);
                                CurrentEditext.setHms(hms);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                    }
                });
    }

    protected void QueryClientInfo() {
        DBHelper.insert(new Data(DataKey.login_tab,LOGIN_TYPE+""));
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.QryCifInfo)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        try {

                            String result=info.getRetDetail();
                            JSONObject json=new JSONObject(result);
                            if (!json.has("jsonError")){
                                ClientInfoBiz ciz=gson.fromJson(result,ClientInfoBiz.class);
                                user.setName(ciz.getCHN_NAME());
                                user.setMobileNum(ciz.getTEL_NO());
                                user.setEnglish_name(ciz.getENG_NAME());
                                Message msg=new Message();
                                msg.what=1;
                                msg.obj=ciz;
                                handler.sendMessage(msg);
                            }else {
                                JSONArray jar=json.getJSONArray("jsonError");
                                JSONObject error=jar.getJSONObject(0);
                                ToastUtils.showShortToast(getResources().getString(R.string.get_client_info));
                                startActivity(new Intent(activity,MainActivity.class));
                            }

                        } catch (Exception e) {
                            Trace.e("saveimgfailed",e.toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
                        try{
                            ToastUtils.showShortToast(info.getRetDetail());
                            if (!TextUtils.isEmpty(redirectName)){
                                Intent in=new Intent(activity,Class.forName(redirectName));
                                startActivity(in);
                                activity.finish();
                            }else {
                                startActivity(new Intent(activity,MainActivity.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    final ClientInfoBiz ciz= (ClientInfoBiz) msg.obj;
                    try {
                        if (TextUtils.isEmpty(getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName)))) {
                            //缓存里有过登陆过的用户的头像
                            String imagepath = null;
                            if (!TextUtils.isEmpty(ciz.getImage())){
                                imagepath = FileUtil.stringToFile(ciz.getImage());
                                Trace.e("转换后的path--", imagepath);
                                DBHelper.insert(new Data(DataKey.userIcon, imagepath));
                                saveName_Image(DBHelper.getDataByKey(DataKey.userName),imagepath);
                            }
                        }else {
                            Trace.e("从内存中取到的--",  getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName)));
                            DBHelper.insert(new Data(DataKey.userIcon, getNameValueImagepath(DBHelper.getDataByKey(DataKey.userName))));
                        }
                    } catch (Exception e) {
                        Trace.e("exception 528",e.toString());
                        e.printStackTrace();
                    }
                    try{
                        if (State.isFirstLogin){
                            Intent in=new Intent(activity, CodeReset2.class);
                            startActivity(in);
                        }else {
                            if (!TextUtils.isEmpty(redirectName)){
                                Intent in=new Intent(activity,Class.forName(redirectName));
                                startActivity(in);
                            }else {
                                startActivity(new Intent(activity,MainActivity.class));
                            }
                        }
                        activity.finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    break;
            }
        }
    };

    private String getNameValueImagepath(String loginid){

        String Nips=DBHelper.getDataByKey(DataKey.nips);
        if (!TextUtils.isEmpty(Nips)){
            String imagePath="";
            Type type=new TypeToken<List<NameImgPair>>(){}.getType();
            List<NameImgPair> nips=gson.fromJson(Nips,type);
            for (NameImgPair n:nips){
                if (n.getLoginId().equals(loginid)){
                    imagePath=n.getImagePath();
                }
            }
            return imagePath;
        }else {
            return "";
        }

    }

    private void saveName_Image(String loginId, String imagepath) {
        List<NameImgPair> nips;
        Type type=new TypeToken<List<NameImgPair>>(){}.getType();
        NameImgPair nip=new NameImgPair();
        nip.setLoginId(loginId);
        nip.setImagePath(imagepath);
        String Nips=DBHelper.getDataByKey(DataKey.nips);
        if (TextUtils.isEmpty(Nips)){
            nips=new ArrayList<>();
        }else {
            nips=gson.fromJson(Nips,type);
        }
        if (!Nips.contains(loginId)){
            nips.add(nip);
        }
        String FinalNipstr=gson.toJson(nips,type);
        DBHelper.insert(new Data(DataKey.nips,FinalNipstr));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
