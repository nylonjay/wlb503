package com.bankscene.bes.welllinkbank.core;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianwei on 2017/5/12.
 */

public class SplashActivity extends HttpActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            initViewPager();
        }
    };
    private static final int PERMISSON_REQUESTCODE = 0;
    private TimeCount timeCount;
    private List<Object> imageList = new ArrayList<>();
    protected String[] needPermissions = {
//            Manifest.permission.ACCESS_COARSE_LOCATION,//定位权限
//            Manifest.permission.ACCESS_FINE_LOCATION,//定位权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储卡写入权限
            Manifest.permission.READ_EXTERNAL_STORAGE,//存储卡读取权限
            Manifest.permission.READ_PHONE_STATE,//读取手机状态权限
            Manifest.permission.CAMERA
    };
    private final int REQUEST_THREE_PERMISSION=3;

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (!SplashActivity.this.isDestroyed()){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
//            askPermission(WRITE_EXTERNAL_STORAGE_CODE, new PermissionResult() {
//                @Override
//                public void executeMethod() {
//
//                }
//
//                @Override
//                public void onDenied() {
//
//                }
//            });

        }

        @Override
        public void onTick(long millisUntilFinished) {
//            String text = millisUntilFinished / 1000 + "秒 跳过";
//            show.setText(text);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
        ToastUtils.init(true);

    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    private void initViewPager() {
//        new TimeCount(500,30000).start();

        QueryNoticeInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void onDestroy() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        super.onDestroy();
    }
    private void QueryNoticeInfo() {
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        setProgressDisplay(false);
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.QueryNoticeList)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        try {
                            JSONObject jsonObject=new JSONObject(info.getRetDetail());
                            String result=jsonObject.getString("List");
                            DBHelper.insert(new Data(DataKey.notice,result));
//                            checkPermissions(needPermissions);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ActivityCompat.requestPermissions(SplashActivity.this,needPermissions,REQUEST_THREE_PERMISSION);



                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
//                        ToastUtils.showShortToast(getResources().getString(R.string.load_data_fail));
                        ActivityCompat.requestPermissions(SplashActivity.this,needPermissions,REQUEST_THREE_PERMISSION);
                    }
                });
    }

    public void Go(){
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        SplashActivity.this.finish();
    }


    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }
    }
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_THREE_PERMISSION:
                Go();
                break;
        }
    }


    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
