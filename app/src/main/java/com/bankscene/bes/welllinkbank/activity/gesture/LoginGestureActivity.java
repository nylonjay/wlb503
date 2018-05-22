package com.bankscene.bes.welllinkbank.activity.gesture;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.ShareActivity;
import com.bankscene.bes.welllinkbank.Util.FileUtil;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.activity.mine.gesture.GestureSetResult;
import com.bankscene.bes.welllinkbank.adapter.common.ImageShape;
import com.bankscene.bes.welllinkbank.common.CommDictAction;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.core.HttpActivity;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.module.User;
import com.bankscene.bes.welllinkbank.view.translucent.ActionBarClickListener;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.csii.gesturekeyboard.GestureContentView;
import com.csii.gesturekeyboard.GestureDrawline;
import com.csii.gesturekeyboard.LockIndicator;
import com.csii.gesturekeyboard.ResId;
import com.csii.gesturekeyboard.SHAUtils;
import com.kh.keyboard.CSIICypher;
import com.kh.keyboard.SecurityCypherException;
import com.okhttplib.HttpInfo;
import com.okhttplib.annotation.RequestType;
import com.okhttplib.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import cn.cloudcore.iprotect.utils.AlgUtils;

public class LoginGestureActivity extends HttpActivity {
    @BindView(R.id.actionBar)
    TranslucentActionBar actionBar;
    private TextView mTextReset;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private boolean mIsFirst = true;
    private String mFirstPwd;
    private boolean isEncrypt = false;
    String timestamp;
    User user;
    static{
        ResId.connLineColorId = R.color.main_theme_color;
        ResId.gestureNodeNormalResId = R.mipmap.oval_select_nomal;
        ResId.gestureNodePressedResId = R.mipmap.group_all;
        ResId.gestureNodeSelectedWrong = R.mipmap.gesture_pattern_selected_wrong;
        ResId.patternNodeNormal = R.mipmap.oval_select_nomal;
        ResId.patternNodePressed = R.mipmap.wlb_circle;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_gesture);
        GetTimeStampAndKeyWithoutEditor();
        user=getUserState(DBHelper.getDataByKey(DataKey.userName));
        mTextReset = (TextView) findViewById(R.id.text_reset);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {

            @Override
            public void onGestureCodeInput(String inputCode) {
                if (TextUtils.isEmpty(inputCode) || inputCode.length() < 6) {
                    mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>"+ getResources().getString(R.string.atleast4)+"</font>"));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }

                if (mIsFirst) {
                    mFirstPwd = inputCode;
                    // 更新选择的图案
                    mLockIndicator.setPath(inputCode);
                    mGestureContentView.clearDrawlineState(0L);
                    mTextReset.setClickable(true);
                    mTextReset.setText(getResources().getString(R.string.redraw));
                    mTextTip.setText(getResources().getString(R.string.redraw2confirm));
                    mTextTip.setTextColor(getResources().getColor(R.color.text_default_color));
                    mIsFirst = false;
                } else {
                    if (inputCode.equals(mFirstPwd)) {
                        mGestureContentView.clearDrawlineState(0L);
                        if(isEncrypt){
                            try {
                                String password = yunEncrypt(inputCode);
                                Toast.makeText(LoginGestureActivity.this, "设置成功，密码："+password, Toast.LENGTH_LONG).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(LoginGestureActivity.this, "加密异常", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            timestamp=System.currentTimeMillis()+"";

                            try {
                                Trace.e("inputcode==",inputCode);
                                String encyped=new CSIICypher().encryptWithJiamiJi(inputCode,dbp,hms,timestamp,"UTF-8",2);
                                SaveGestureCode(encyped.replace("+","%2B"));

                            } catch (SecurityCypherException e) {
                                e.printStackTrace();
                            }

//                            Toast.makeText(LoginGestureActivity.this, "设置成功，密码："+inputCode, Toast.LENGTH_LONG).show();
//                            noticeUtils.showNotice(getResources().getString(R.string.gesture_set_succeed));
//                            EventBus.getDefault().post(new MessageEvent(Constant.GESTURE_SETTED));
                            //跳至設置成功頁面
                        }

                    } else {
                        // 保持绘制的线，1秒后清除
//                        Toast.makeText(LoginGestureActivity.this, "两次密码不正确，请重新设置", Toast.LENGTH_SHORT).show();
                        noticeUtils.showNotice(getResources().getString(R.string.gesture_not_same));
                        mGestureContentView.clearDrawlineState(1000L);
                        mTextReset.performClick();
                    }
                }
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {

            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        mLockIndicator.setPath("");
        mTextReset.setOnClickListener(new View.OnClickListener() { //重置状态
            @Override
            public void onClick(View v) {
                mIsFirst = true;
                mLockIndicator.setPath("");
                mTextTip.setText(getResources().getString(R.string.conseal_safty));
                mTextTip.setTextColor(getResources().getColor(R.color.text_default_color));
                mTextReset.setClickable(false);
                mTextReset.setText("");
            }
        });


    }


    @Override
    protected void setActionBar() {
        actionBar.setActionBar(getResources().getString(R.string.gesture_setting), R.string.wlb_arrow_l, "", 0, "", new ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                LoginGestureActivity.this.finish();
            }

            @Override
            public void onRightClick() {

            }
        });
        actionBar.setStatusBarHeight();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_gesture;
    }

    public String yunEncrypt(String inputCode) throws Exception{
        // 服务端获取的时间戳
        String timestamp = "1473817509714";
        // 加密公钥
        String appPubKey = "e421e248b9c7d2cdfba679520593452f5603a26730d607a8a9d87e6800c09bed8906790bc4f61b63e856517d8b939883db0023a315e93eb19a77ab73a40d1d2a1173a9cb2552092429be92e83a03093cf44554a0e65f4cff17a81c65ebcb2e2f89fea8c3fb7889f7ceb30e26b1d76f41fe05896d46711a610734417076c25849";
        JSONObject object = new JSONObject();
        String gesturePwd = SHAUtils.encryptSHA(inputCode);
        object.put("gesturePwd", gesturePwd);
        String deviceId = SHAUtils.encryptSHA(getUUID(this));
        object.put("deviceId", deviceId);
        String jsonStr = object.toString();
        StringBuffer ciphertext = new StringBuffer();
        int code = AlgUtils.getCipherText(appPubKey, timestamp, jsonStr, ciphertext);
        if(code == 0){ //加密成功
            return ciphertext.toString();
        }else{ //加密失败
            return String.valueOf(code);
        }
    }

    public static String getUUID(Context mContext) {
        final TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                mContext.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(),
                ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        return deviceUuid.toString().replaceAll("-", "");
    }


    private void SaveGestureCode(final String encryped) {

        Map params=new HashMap();
        params.put("_ChannelId","PMBS");
        params.put("OperationType","01");
        params.put("DeviceId",BaseApplication.deviceId);
        params.put("PatternLockPassword",encryped);
        params.put("ConfirmPassword",encryped);
        params.put("LoginId",DBHelper.getDataByKey(DataKey.userName));
        setProgressDisplay(true);
        doHttpAsync(HttpInfo.Builder()
                        .addHead("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .addHead("cookie",DBHelper.getDataByKey(DataKey.cookie))
                        .setUrl(CommDictAction.SaveGestureCode)
                        .setRequestType(RequestType.POST)//设置请求方式
//                        .setNeedResponse(true)
                        .addParams(params)
                        .build(),
                new Callback(){
                    @Override
                    public void onSuccess(HttpInfo info) throws IOException {
                        String result=info.getRetDetail();
                        try {
                            JSONObject json=new JSONObject(result);
                            if (json.opt(_REJCODE).equals("000000")){
                                DBHelper.insert(new Data(DataKey.gesture_code,encryped));
                                user.setGestureSetted(true);
                                saveUserState(DBHelper.getDataByKey(DataKey.userName),user.isGestureOpen(),user.isGestureSetted());
                                startActivity(new Intent(LoginGestureActivity.this,GestureSetResult.class));
                                LoginGestureActivity.this.finish();
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

}
