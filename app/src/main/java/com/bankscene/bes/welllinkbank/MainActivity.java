package com.bankscene.bes.welllinkbank;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.bankscene.bes.welllinkbank.Util.ToastUtils;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.adapter.MainPagerAdapter;
import com.bankscene.bes.welllinkbank.biz.MessageEvent;
import com.bankscene.bes.welllinkbank.common.Constant;
import com.bankscene.bes.welllinkbank.core.BaseApplication;
import com.bankscene.bes.welllinkbank.view.CustomViewPager;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class MainActivity extends ShareActivity {
    private Boolean isExit = false;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    private MainPagerAdapter pagerAdapter;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    Intent in;
    int index=0;
    private final int REQUEST_THREE_PERMISSION=3;
    private int REQUEST_READ_PHONE_STATE_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FitStateUI.setImmersionStateMode(this);
        super.onCreate(savedInstanceState);
        Trace.e("Nylon","oncreate");
        EventBus.getDefault().register(this);
        in=getIntent();
        String ins=in.getStringExtra(BaseApplication.action_name);
        if (!TextUtils.isEmpty(ins)){
            index=Integer.parseInt(ins);
        }
        initTabPager(index);


    }

    @Override
    protected void setActionBar() {
    }

    @Override
    protected void initView() {

//        TEST3DES();

    }



//    private void TEST3DES() {
//        String timestamp=System.currentTimeMillis()+"";
//        String plaiName="qwertyuiopasdfgh12345678901231";
//        String module= CommDictAction.SecurityPubKey;
//        try {
//            String encyped=new CSIICypher(). encryptWithoutRemove(plaiName,module,timestamp,"utf-8",2);
//            String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"encryped/";
////            new File(path).mkdirs();
//            String target=path+"3des.txt";
//            FileUtil.saveFile(encyped,target);
//            Trace.e("encryed==",encyped);
//        } catch (SecurityCypherException e) {
//            e.printStackTrace();
//        }
//    }

    private void initTabPager(int index) {
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(5);
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(pagerAdapter.getTabView(i));
            }
        }
        tabLayout.getTabAt(index).getCustomView().setSelected(true);
        changeIcon(index);



        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }
    private void changeIcon(int position) {
        if (MainPagerAdapter.previous != -1) {
            View view = tabLayout.getTabAt(MainPagerAdapter.previous).getCustomView();
            IconFontTextView icon = (IconFontTextView) view.findViewById(R.id.icon);
            icon.setText(getResources().getString(MainPagerAdapter.getUnselectedIcon(MainPagerAdapter.previous)));
        }
        MainPagerAdapter.previous = position;
        View view = tabLayout.getTabAt(position).getCustomView();
        IconFontTextView icon = (IconFontTextView) view.findViewById(R.id.icon);
        icon.setText(getResources().getString(MainPagerAdapter.getSelectedIcon(position)));
    }

    @Override
    protected void onResume() {
        Trace.e("Nylon","onresume");
//        initImmersionBar();

        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (getSupportFragmentManager().getBackStackEntryCount() == 0||true) {
                    exitBy2Click();
                } else {
                    backStackFragment();
                }
                break;

            default:
                break;
        }
        return true;
    }
    private void exitBy2Click() {
        Trace.e(this.getClass().getName(),"exitby2click");
        if (!isExit) {
            isExit = true;
            ToastUtils.showShortToast(getResources().getString(R.string.click_again2quit));
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void initData() {
        TelephonyManager TelephonyMgr = (TelephonyManager) MainActivity.this
                .getSystemService(Context.TELEPHONY_SERVICE);
//        Trace.e("derviceID",TelephonyMgr.getDeviceId());

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)== PackageManager.PERMISSION_GRANTED){
            BaseApplication.deviceId=TelephonyMgr.getDeviceId();
        }
        //如果没有权限那么申请权限
        else{
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.CALL_PHONE},REQUEST_READ_PHONE_STATE_PERMISSION);
        }

        BaseApplication.deviceName=android.os.Build.MODEL;
    }

    @Subscribe(threadMode = ThreadMode.PostThread)
    public void onMessageEventPostThread(MessageEvent messageEvent){
        Trace.e(TAG,"POST_messageEvent==="+messageEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMessageEventMainThread(MessageEvent messageEvent){
        Trace.e(TAG,"MAIN_messageEvent==="+messageEvent.getMessage());
        switch (messageEvent.getMessage()){
            case Constant.EVENT_REFRESH_LANGUAGE:
                changeAppLanguage();
                recreate();
//                refreshSelf();
                break;
            case Constant.EVENT_RELOAD_FRAGMENTS:
//                recreate();
                break;
            case Constant.GESTURE_SETTED:
                noticeUtils.showNotice(getResources().getString(R.string.gesture_set_succeed));
                break;


        }
    }
    @Subscribe(threadMode = ThreadMode.Async)
    public void onMessageEventAsyncThread(MessageEvent messageEvent){
        Trace.e(TAG,"ASYNC_messageEvent==="+messageEvent.getMessage());
    }
    @Subscribe(threadMode = ThreadMode.BackgroundThread)
    public void onMessageEventBackgroundThread(MessageEvent messageEvent){
        Trace.e(TAG,"BACKGROUND_messageEvent==="+messageEvent.getMessage());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void recreate() {
        try {//避免重启太快 恢复
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                Trace.e("reload",fragment.getClass().toString());
                fragmentTransaction.remove(fragment);
//                fragment.onDestroy();
            }
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
        }
        super.recreate();
    }
}
