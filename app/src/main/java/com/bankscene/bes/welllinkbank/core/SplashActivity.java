package com.bankscene.bes.welllinkbank.core;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.UpdateInfoService;
import com.bankscene.bes.welllinkbank.biz.UpdateInfo;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.google.gson.Gson;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;
import com.okhttplib.callback.ProgressCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tianwei on 2017/5/12.
 */

public class SplashActivity extends HttpActivity implements ActivityCompat.OnRequestPermissionsResultCallback{


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
    private UpdateInfoService updateInfoService;
    private UpdateInfo updateinfo;
    private ProgressDialog progressDialog;
    private TextView tv_version;
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
        tv_version= (TextView) findViewById(R.id.tv_version);
        tv_version.setText(getCurrentVersion(SplashActivity.this));
        QueryNoticeInfo();
    }
    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    private void initViewPager() {
//        new TimeCount(500,30000).start();

//        QueryNoticeInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (timeCount != null) {
            timeCount.cancel();
        }
        super.onDestroy();
    }
    Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    String filepath= (String) msg.obj;
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.fromFile(new File("/storage/emulated/0/okHttp_download/",filepath.substring(filepath.lastIndexOf("/")))),
//                            "application/vnd.android.package-archive");
//                    SplashActivity.this.startActivity(intent);
                    String[] arr=filepath.split("/");
                    File apkfile = new File("/storage/emulated/0/okHttp_download/",arr[arr.length-1]);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                            "application/vnd.android.package-archive");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(i);
                    break;
            }
        }
    };
    void update() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "Test.apk")),
                "application/vnd.android.package-archive");
        this.startActivity(intent);
    }
    private void CheckVersionInfo(){
        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        doHttpAsyncWhioutDialog(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//                        .addHead("cookie", DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.GetVersionInfo)
                        .setRequestType(RequestType.GET)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        Trace.e("versioninf==",info.getRetDetail());
//                        {	version:"1.0",	description:"新版本上線",	url:"xxxx.com"}
                        try {
                            JSONObject result=new JSONObject(info.getRetDetail());

                            final float newVersion = Float
                                    .parseFloat(result.optString("version")
                                            .toString().trim());
                            final float ver=Float.parseFloat(getCurrentVersion(SplashActivity.this));
                            if (ver<newVersion){
//                                String msg = "当前客户端版本低于最新版本，请更新最新版本"
//                                        + newVersion+"";
//                                msg += "\r\n";
//                                msg += result.getString("description");
                                updateInfoService = new UpdateInfoService(SplashActivity.this);
                                updateinfo=new Gson().fromJson(info.getRetDetail(),UpdateInfo.class);
                                showUpdateDialog();
                            }else {
                                QueryNoticeInfo();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
//                        ToastUtils.showShortToast(getResources().getString(R.string.load_data_fail));
//                        ZbPermission.needPermission(SplashActivity.this, REQUEST_THREE_PERMISSION, needPermissions);
                        checkPermissions(needPermissions);
                    }
                });
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle("请升级APP至版本" + updateinfo.getVersion());
        builder.setMessage(updateinfo.getDescription());
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    downFile(updateinfo.getUrl());
                } else {
                    Toast.makeText(SplashActivity.this, "SD卡不可用，请插入SD卡",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QueryNoticeInfo();
            }
        });
        builder.create().show();
    }
    void downFile(final String url) {
        progressDialog = new ProgressDialog(SplashActivity.this);    //进度条，在下载的时候实时更新进度，提高用户友好度
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载");
        progressDialog.setMessage("请稍候...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.show();
//        updateInfoService.downLoadFile(url, progressDialog,handler1);
        downloadFile(progressDialog,handler1,url);
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
//                        ActivityCompat.requestPermissions(SplashActivity.this,needPermissions,REQUEST_THREE_PERMISSION);
//                        ZbPermission.needPermission(SplashActivity.this, REQUEST_THREE_PERMISSION, needPermissions);
                        checkPermissions(needPermissions);
                    }

                    @Override
                    public void onFailure(HttpInfo info) throws IOException {
//                        ToastUtils.showShortToast(getResources().getString(R.string.load_data_fail));
//                        ZbPermission.needPermission(SplashActivity.this, REQUEST_THREE_PERMISSION, needPermissions);
                        checkPermissions(needPermissions);
                    }
                });
    }

    public void Go(){
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        SplashActivity.this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        ZbPermission.onRequestPermissionsResult(SplashActivity.this, requestCode, permissions, grantResults);
        CheckVersionInfo();
    }
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSON_REQUESTCODE);
        }else {
            Go();
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


    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
