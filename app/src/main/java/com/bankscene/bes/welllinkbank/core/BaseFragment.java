package com.bankscene.bes.welllinkbank.core;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.Util.Trace;
import com.bankscene.bes.welllinkbank.Util.dialog.DialogUtils;
import com.bankscene.bes.welllinkbank.Util.notice.NoticeUtils;
import com.bankscene.bes.welllinkbank.adapter.common.GlideCircleTransform;
import com.bankscene.bes.welllinkbank.adapter.common.GlideRoundTransform;
import com.bankscene.bes.welllinkbank.adapter.common.ImageShape;
import com.bankscene.bes.welllinkbank.view.translucent.TranslucentActionBar;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.okhttplib.HttpInfo;
import com.okhttplib.OkHttpUtil;
import com.okhttplib.callback.Callback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment extends Fragment {

    public static final int QUERYLOGINSTATE=111;
    public static final int REFRESHGRIDVIEW=222;
    protected BaseActivity activity;
    protected View rootView;
    protected TranslucentActionBar actionBar;
    protected boolean mIsVisible;
    protected boolean mIsPrepare;
    protected boolean mIsImmersion;
    protected static String _REJCODE="_RejCode";
    protected static String _REJMSG="_RejMsg";
    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;
    public String TAG="";
    private DialogUtils dialogUtils;
    public Gson gson;
    public NoticeUtils noticeUtils;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Trace.e(TAG,"onAttach");
        activity = (BaseActivity) context;
        noticeUtils=new NoticeUtils(activity);
        gson=new Gson();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(setLayoutId(), container, false);

        return rootView;
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
                            .transform(new GlideCircleTransform(activity))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(imageView);
                    break;
                case ImageShape.ROUND:
                    Glide.with(this).load(imagePath)
                            .transform(new GlideRoundTransform(activity))
                            .placeholder(defaultImage)
                            .error(errorImage)
                            .into(imageView);
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(activity, "", Toast.LENGTH_SHORT).show();
        }
    }

//    JSONArray jsonError = response.optJSONArray("jsonError");
//                        if (jsonError != null) {
//        JSONObject error = jsonError.optJSONObject(0);
//        if (error != null) {
//            String _exceptionMessageCode = error.optString("_exceptionMessageCode");
//            if (_exceptionMessageCode.equals("role.invalid_user") ||
//                    _exceptionMessageCode.equals("validation.login_failed")) {
//                State.isLogin = false;
//                apiInterface.onDataError("您还未登录，请先登录!");
//            } else {
//                apiInterface.onDataError(error.optString("_exceptionMessage"));
//            }
//        }
//    } else {
//        apiInterface.onNext(response);
//    }

    protected void doHttpAsync(HttpInfo info, final Callback callback){
//       activity. getMainHandler().sendEmptyMessage(SHOW_DIALOG);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Trace.e(TAG,"fragment created");
        unbinder = ButterKnife.bind(this, rootView);
        dialogUtils=new DialogUtils(activity);
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {
            initData();
            if (isImmersionBarEnabled())
                initImmersionBar();
        }
        actionBar = activity.getTranslucentActionBar();
        initView();
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        initImmersionBar();
        if (!isLazyLoad()) {
            setActionBar();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mImmersionBar != null)
            mImmersionBar.destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    protected boolean isLazyLoad() {
        return false;
    }

    protected boolean isImmersionBarEnabled() {
        return true;
    }

    protected void onVisible() {
        onLazyLoad();
    }

    protected void onInvisible() {

    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            setActionBar();
            initData();
        }
        if (mIsVisible && mIsImmersion && isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    protected abstract int setLayoutId();

    protected void initData() {

    }

    protected void initView() {

    }

    protected void setListener() {

    }

    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(activity);
        mImmersionBar.statusBarDarkFont(true, 0.2f).init();
//        mImmersionBar.keyboardEnable(true).navigationBarWithKitkatEnable(false).init();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findActivityViewById(@IdRes int id) {
        return (T) activity.findViewById(id);
    }

    public void setActionBar() {
    }

    public void onRefresh() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
    }

//    public void share() {
//        if (getActivity() != null && (getActivity() instanceof ShareActivity)) {
//            ((ShareActivity) getActivity()).share();
//        }
//    }

    public void showNotice(String notice) {
        activity.showNotice(notice);
    }
    public void showAccountList2(ArrayList<String> datalist){
        ArrayList  datas=datalist;
        dialogUtils.ShowDialogList2(activity.getResources().getString(R.string.languagelist),datas,activity);
    }

    public void showNotice(int icon, CharSequence notice) {
        activity.showNotice(icon, notice);
    }

    public String getTimeStamp() {
        return System.currentTimeMillis() + BaseApplication.TimeStamp + "";
    }

    public boolean isNetworkAvailable() {
        return activity.isNetworkAvailable(activity);
    }

    private boolean isNull() {
        return activity == null;
    }

    public void hideSoftInput() {
        if (isNull()) return;
        activity.hideSoftInput();
    }

    public void addFragment(Fragment fragment) {
        if (isNull()) return;
        if (fragment == null) {
            return;
        }
        String tag = fragment.getClass().getName();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left,
                R.anim.in_from_left, R.anim.out_to_right);
        ft.hide(this);
        ft.add(R.id.content, fragment, tag);
        activity.setFirstContent(fragment);
        ft.addToBackStack(tag);
        ft.commitAllowingStateLoss();
    }

    public void switchFragment(Fragment fragment) {
        if (isNull()) return;
        activity.switchFragment(fragment);
    }

    public void backStackFragment() {
        if (isNull()) return;
        activity.backStackFragment();
    }

    public void backStackFragment(int times) {
        if (isNull()) return;
        activity.backStackFragment(times);
    }

    public void backStackFragment(String className) {
        if (isNull()) return;
        activity.backStackFragment(className);
    }

    public void setBackground(View view, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            view.setBackgroundColor(color);
        else
            view.setBackgroundColor(color);
    }

    public void setProgressDisplay(boolean isShow) {
        if (isNull()) return;
        activity.setProgressDisplay(isShow);

    }

    public void setLockTips(String tips) {
        if (isNull()) return;
        activity.setLockTips(tips);
    }

    public void setOnKeyBackFlag(int backFlag) {
        if (isNull()) return;
        activity.setOnKeyBackFlag(backFlag);
    }

    public void setImage(ImageView imageView, Object imagePath, int type) {
        if (isNull()) return;
        activity.setImage(imageView, imagePath, type);
    }



//    public void gotoLogin() {
//        if (State.gestureLoginOpen) {
//            startActivity(new Intent(activity, LoginGestureActivity.class));
//        } else {
//            startActivity(new Intent(activity, LoginActivity.class));
//        }
//    }

    public void addDisposable(Disposable disposable) {
        if (isNull()) return;
        activity.addDisposable(disposable);
    }
}
