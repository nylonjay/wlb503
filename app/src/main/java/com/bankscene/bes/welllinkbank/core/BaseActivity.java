package com.bankscene.bes.welllinkbank.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.SharedPreferenceUtil;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogCallBack;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogUtils;
import com.bankscene.bes.welllinkbank.Util.lock.LockUtils;
import com.bankscene.bes.welllinkbank.Util.notice.NoticeUtils;
import com.bankscene.bes.welllinkbank.activity.LoginActivity;
import com.bankscene.bes.welllinkbank.activity.gesture.LoginGestureActivity;
import com.bankscene.bes.welllinkbank.adapter.common.GlideCircleTransform;
import com.bankscene.bes.welllinkbank.adapter.common.GlideRoundTransform;
import com.bankscene.bes.welllinkbank.adapter.common.ImageShape;
import com.bankscene.bes.welllinkbank.biz.FinanceMainBiz;
import com.bankscene.bes.welllinkbank.db1.DBHelper;
import com.bankscene.bes.welllinkbank.db1.Data;
import com.bankscene.bes.welllinkbank.db1.DataKey;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    public final static int NORMAL_BACK = 0x003;
    public final static int FINISH_BACK = 0x009;

    protected Fragment firstContent;
    protected Fragment content;

    private int backFlag = NORMAL_BACK;

    private InputMethodManager imm;
    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;

    protected NoticeUtils noticeUtils;
    protected DialogUtils dialogUtils;
    private LockUtils lockUtils;
    public String TAG="Nylon";
    private CompositeDisposable compositeDisposable;

    protected TranslucentActionBar actionBar;
    protected View rootView;
    LocaleChangeReceiver lcr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TAG = getClass().getSimpleName();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setLayoutId());
        //绑定控件
        //监听系统语言广播
//        IntentFilter inf=new IntentFilter();
//        inf.addAction(Intent.ACTION_LOCALE_CHANGED);
//        lcr=new LocaleChangeReceiver();
//        registerReceiver(lcr,inf);
        unbinder = ButterKnife.bind(this);


        noticeUtils = new NoticeUtils(this);
        dialogUtils = new DialogUtils(this);
        lockUtils = new LockUtils(this);



        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Trace.e(TAG,"BASE oncreate");
        initImmersionBar();
        initListener();

        initData();

        initView();

        setListener();
    }

    @Override
    public void recreate() {
        super.recreate();
    }

    public Map GetUserIndexMap(){
        Gson gson=new Gson();

        Map map= gson.fromJson(SharedPreferenceUtil.get(this,BaseApplication.USER_INDEX,"").toString(),Map.class);
        Trace.e("GETUSERINDEXMAP:",map.toString());
        return  map;
    }
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setActionBar();
        Trace.e(TAG,"BASE onresume");
        initImmersionBar();
//        ActivityManager mAm = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
//        for (ActivityManager.RunningTaskInfo rti : taskList)
//            Trace.e("showRunningTasks", rti.baseActivity.getClassName() + "-----" + rti.topActivity.getClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        noticeUtils.cancelNotice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noticeUtils.cancelNotice();
        unbinder.unbind();
        this.imm = null;
        if (mImmersionBar != null)
            mImmersionBar.destroy();  //在BaseActivity里销毁

//        IntentFilter inf=new IntentFilter();
//        inf.addAction(Intent.ACTION_LOCALE_CHANGED);
//        unregisterReceiver(lcr);
    }

    protected abstract void setActionBar();
    protected abstract int setLayoutId();

    protected void initImmersionBar() {
        if (isImmersionBarEnabled()){
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    protected void initData() {
    }

    protected void initView() {
    }

    protected void setListener() {
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }
    public void goHome(Activity activity){
        startActivity(new Intent(activity, MainActivity.class));
    }


    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    public void setOnKeyBackFlag(int flag) {
        this.backFlag = flag;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (backFlag == NORMAL_BACK){
                    backStackFragment();
                }
                else if (backFlag == FINISH_BACK){
                    finish();
                }
                break;

            default:
                break;
        }
        return true;
    }

    public Fragment getFirstContent() {
        return firstContent;
    }

    public Fragment getContent() {
        return content;
    }

    public void setFirstContent(Fragment firstContent) {
        this.firstContent = firstContent;
    }

    public void switchFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        firstContent = fragment;
        content = fragment;
        String tag = fragment.getClass().getName();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, fragment, tag).commitAllowingStateLoss();
    }

    public void backStackFragment() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            finish();
        } else {
            fm.popBackStackImmediate();
        }
    }

    public void backStackFragment(int times) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            return;
        }
        for (int i = 0; i < times; i++) {
            if (fm.getBackStackEntryCount() == 0) {
                finish();
            } else {
                fm.popBackStackImmediate();
            }
        }
    }

    public void backStackFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void hideSoftInput() {
        try {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null && manager.isActive()) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                Trace.e("info", "count == " + count);
                if (count > 0) {
                    FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                            count - 1);
                    String name = entry.getName();
                    Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
                    if (fragment instanceof BaseFragment) {
                        BaseFragment newBase = (BaseFragment) fragment;
                        firstContent = newBase;
                        newBase.setActionBar();
                        newBase.onRefresh();
                    }
                } else {
                    ((BaseFragment) content).setActionBar();
                    ((BaseFragment) content).onRefresh();
                }
            }
        });
    }

    public void setImage(ImageView imageView, Object imagePath, int type) {
        try {
            switch (type) {
                case ImageShape.NORMAL:
                    Glide.with(this).load(imagePath)
                            .into(imageView);
                    break;
                case ImageShape.CIRCLE:
                    Glide.with(this).load(imagePath)
                            .transform(new GlideCircleTransform(this))
                            .into(imageView);
                    break;
                case ImageShape.ROUND:
                    Glide.with(this).load(imagePath)
                            .transform(new GlideRoundTransform(this))
                            .into(imageView);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    public void setImage(ImageView imageView, Object imagePath, int type,
                         int defaultImage, int errorImage) {
        try {
            switch (type) {
                case ImageShape.NORMAL:
                    Glide.with(this).load(imagePath)
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(imageView);
                    break;
                case ImageShape.CIRCLE:
                    Glide.with(this).load(imagePath)
                            .transform(new GlideCircleTransform(this))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(imageView);
                    break;
                case ImageShape.ROUND:
                    Glide.with(this).load(imagePath)
                            .transform(new GlideRoundTransform(this))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(imageView);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setProgressDisplay(boolean isShow) {
        lockUtils.setProgressDisplay(isShow);
    }

    public void setLockTips(String tips) {
        lockUtils.setLockTips(tips);
    }

    public String getTimeStamp() {
        return System.currentTimeMillis() + BaseApplication.TimeStamp + "";
    }

    public void showNotice(String notice) {
        noticeUtils.showNotice(notice);
    }

    public void showNotice(int icon, CharSequence notice) {
        noticeUtils.showNotice(icon,notice);
    }

    public void ShowDialogOne(String title, String msg, String submit, DialogCallBack callback){
        dialogUtils.ShowDialogOne(title,msg,submit,callback);
    }

    public void ShowDialogTwo(String title, String msg, String submit, String cancel, DialogCallBack callback){
        dialogUtils.ShowDialogTwo(title,msg,submit,cancel,callback);
    }


//    public void gotoFindPayPassword(){
//        startActivity(new Intent(this, PayPasswordFindActivity.class));
//    }
//
//    public void gotoAddCard(){
//        startActivity(new Intent(this, CardAddActivity.class));
//    }

    public void goLogin() {
        ShowDialogTwo("温馨提示", "您还未登录，请先登录！", "确定", "取消",
                new DialogCallBack() {
                    @Override
                    public void onPositive() {
//                        gotoLogin();
                    }

                    @Override
                    public void onNegative() {
                        finish();
                    }

                });
    }

    public void gotoLogin() {
//        if (State.gestureLoginOpen) {
//            startActivity(new Intent(this, LoginGestureActivity.class));
//        } else {
//        }
        startActivity(new Intent(this, LoginActivity.class));
    }

    public TranslucentActionBar getTranslucentActionBar() {
        return actionBar;
    }

    public class LocaleChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Trace.e(TAG, "mReceiver  onReceive  intent.getAction(): "+intent.getAction());

            if(intent.getAction().equals(Intent.ACTION_LOCALE_CHANGED)) {
                Trace.e("LocaleChangeReceiver","Language change");
                DBHelper.insert(new Data(DataKey.language,getLanguage()));
            }
        }
    }


    private String getLanguage() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return "zh";
        else
            return "en";
    }
}
