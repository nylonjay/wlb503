package com.bankscene.bes.welllinkbank;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.core.BaseActivity;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.PermissionResult;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.PopupWindowUtils;

import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

public abstract class ShareActivity extends BaseActivity implements View.OnClickListener {

    public static final int READ_PHONE_STATE_CODE = 1;
    public static final int ACCESS_FINE_LOCATION_CODE = 2;
    public static final int WRITE_EXTERNAL_STORAGE_CODE = 3;
    public static final int CAMERA_CODE = 4;
    public static final int READ_EXTERNAL_STORAGE_CODE=5;
    public static final int CALL_PHONE=7;
    public static final int FILE=6;
    public PopupWindowUtils popupWindow;
    private PermissionResult permissionResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        popupWindow = new PopupWindowUtils(ShareActivity.this, getShareView(), rootView, PopupWindowUtils.STYLE_I);

    }

    public void shiftLanguage(String sta){

        if(sta.equals("zh")){
            Locale.setDefault(Locale.ENGLISH);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;
            getBaseContext().getResources().updateConfiguration(config
                    , getBaseContext().getResources().getDisplayMetrics());
            refreshSelf();
        }else{
            Locale.setDefault(Locale.CHINESE);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.CHINESE;
            getBaseContext().getResources().updateConfiguration(config
                    , getBaseContext().getResources().getDisplayMetrics());
            refreshSelf();
        }
    }
    //refresh self
    public void refreshSelf(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(BaseApplication.action_name, BaseApplication.REFRESH);
        startActivity(intent);
    }
    public void share() {
        popupWindow.show();
    }

    public void share(JSONObject data) {
        popupWindow.show();
    }
    public void changeAppLanguage() {
        String sta = DBHelper.getDataByKey(DataKey.language).equals("zh")?"en":"zh";
        // 本地语言设置
        Locale myLocale = new Locale(sta);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        DBHelper.insert(new Data(DataKey.language,sta));
    }

    private boolean isZh() {
        Locale locale =this. getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, intent);
//        com.umeng.socialize.utils.Log.d("result", "onActivityResult");
        android.util.Log.e("result", "onActivityResult: " + requestCode + "  " + resultCode);

    }

    public void askPermission(int what, PermissionResult permissionResult) {
        int permission;
        this.permissionResult = permissionResult;
        switch (what) {
            case READ_PHONE_STATE_CODE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.READ_PHONE_STATE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            READ_PHONE_STATE_CODE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
            case ACCESS_FINE_LOCATION_CODE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION_CODE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
            case WRITE_EXTERNAL_STORAGE_CODE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            WRITE_EXTERNAL_STORAGE_CODE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
            case CAMERA_CODE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.CAMERA);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            CAMERA_CODE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
            case READ_EXTERNAL_STORAGE_CODE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_EXTERNAL_STORAGE_CODE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
            case CALL_PHONE:
                permission = ActivityCompat.checkSelfPermission(ShareActivity.this, Manifest.permission.CALL_PHONE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            ShareActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PHONE
                    );
                } else {
                    permissionResult.executeMethod();
                }
                break;
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case READ_PHONE_STATE_CODE:
//                if (permissions[0].equals(Manifest.permission.READ_PHONE_STATE)
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (null!=permissionResult)
//                    permissionResult.executeMethod();
//                } else {
////                    showNotice("未获取读取设备状态权限,请在设置-应用权限中打开！");
//                    if (null!=permissionResult)
//                    permissionResult.onDenied();
//                }
//                break;
//            case ACCESS_FINE_LOCATION_CODE:
//                if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (null!=permissionResult)
//                    permissionResult.executeMethod();
//                } else {
//                    showNotice("未获取到定位的权限,请在设置-应用权限中打开！");
//                    if (null!=permissionResult)
//                    permissionResult.onDenied();
//                }
//                break;
//            case WRITE_EXTERNAL_STORAGE_CODE:
//                if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (null!=permissionResult)
//                    permissionResult.executeMethod();
//                } else {
//                    showNotice("未获取到存储权限,请在设置-应用权限中打开！");
//                    if (null!=permissionResult)
//                    permissionResult.onDenied();
//                }
//                break;
//            case CAMERA_CODE:
//                if (permissions[0].equals(Manifest.permission.CAMERA)
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (null!=permissionResult)
//                    permissionResult.executeMethod();
//                } else {
//                    if (null!=permissionResult)
//                    permissionResult.onDenied();
//                }
//                break;
//
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void onClick(View v) {
        JSONObject shareMessage = new JSONObject();
        switch (v.getId()) {
//            case R.id.image1:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.WEIXIN)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc").equals("") ? "无标题" : shareMessage.optString("desc"))
//                        .withMedia(new UMImage(ShareActivity.this, shareMessage.optString("imgUrl")))
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
//            case R.id.image2:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc").equals("") ? "无标题" : shareMessage.optString("desc"))
//                        .withMedia(new UMImage(ShareActivity.this, shareMessage.optString("imgUrl")))
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
//            case R.id.image3:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.SINA)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc").equals("") ? "无标题" : shareMessage.optString("desc"))
//                        .withMedia(new UMImage(ShareActivity.this, shareMessage.optString("imgUrl")))
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
//            case R.id.image4:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.QQ)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc"))
//                        .withMedia(new UMImage(ShareActivity.this, shareMessage.optString("imgUrl")))
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
//            case R.id.image5:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.QZONE)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc"))
//                        .withMedia(new UMImage(ShareActivity.this, shareMessage.optString("imgUrl")))
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
//            case R.id.image6:
//                new ShareAction(ShareActivity.this)
//                        .setPlatform(SHARE_MEDIA.SMS)//
//                        .setCallback(umShareListener)//
//                        .withTitle(shareMessage.optString("cmName"))
//                        .withText(shareMessage.optString("desc") + shareMessage.optString("link") + "&share=shareSDK")
//                        .withTargetUrl(shareMessage.optString("link") + "&share=shareSDK")
//                        .share();
//                popupWindow.dismiss();
//                break;
            case R.id.image7:
                showCode(shareMessage.optString("link") + "&share=shareSDK");
                popupWindow.dismiss();
                break;
            case R.id.image8:
                ClipboardManager myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip;
                myClip = ClipData.newPlainText("text", shareMessage.optString("link"));
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(ShareActivity.this, "已复制到粘贴板！", Toast.LENGTH_LONG).show();
                popupWindow.dismiss();
                break;
            case R.id.cancel:
                popupWindow.dismiss();
                break;
        }
    }

    private void showCode(String url) {
//        final AlertDialog dlg = new AlertDialog.Builder(this).create();
//        dlg.show();
//        WindowManager.LayoutParams params =
//                dlg.getWindow().getAttributes();
//        params.width = DensityUtil.dip2px(this, 230);
//        params.height = DensityUtil.dip2px(this, 230);
//        dlg.getWindow().setAttributes(params);
//        Window window = dlg.getWindow();
//
//        window.setBackgroundDrawableResource(android.R.color.transparent);
//        window.setContentView(R.layout.erweima_layout);
//        ImageView erweima = (ImageView) window.findViewById(R.id.iv_create_erweima);
//        window.findViewById(R.id.tv_erweima_cancle).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dlg.dismiss();
//            }
//        });
//        Bitmap qrImage = ErWeiMaUtil.createQRImage(url, 200, 200);
//        erweima.setImageBitmap(qrImage);
    }

    private View getShareView() {
        View view = View.inflate(this, R.layout._view_share, null);
        view.findViewById(R.id.image1).setOnClickListener(this);
        view.findViewById(R.id.image2).setOnClickListener(this);
        view.findViewById(R.id.image3).setOnClickListener(this);
        view.findViewById(R.id.image4).setOnClickListener(this);
        view.findViewById(R.id.image5).setOnClickListener(this);
        view.findViewById(R.id.image6).setOnClickListener(this);
        view.findViewById(R.id.image7).setOnClickListener(this);
        view.findViewById(R.id.image8).setOnClickListener(this);
        view.findViewById(R.id.cancel).setOnClickListener(this);
        return view;
    }

//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//                Toast.makeText(ShareActivity.this, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(ShareActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(ShareActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
//            if (t != null) {
//                com.umeng.socialize.utils.Log.d("throw", "throw:" + t.getMessage());
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(ShareActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
//        }


}
