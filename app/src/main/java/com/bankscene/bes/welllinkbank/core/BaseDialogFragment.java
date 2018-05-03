package com.bankscene.bes.welllinkbank.core;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.bankscene.bes.welllinkbank.R;


/**
 * Created by tianwei on 2017/6/2.
 */

public abstract class BaseDialogFragment extends DialogFragment {

    public String TAG = "BaseDialogFragment";
    public boolean isLocked = false;
    public View lock;
    public BaseActivity activity;

    public BaseDialogFragment(){

    }

    @Override
    public void onStart()
    {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        Dialog dialog = getDialog();
        Window window = dialog.getWindow();
        getDialog().getWindow().setLayout( dm.widthPixels, getDialog().getWindow().getAttributes().height );
        getDialog().getWindow().setBackgroundDrawable( new ColorDrawable(getResources().getColor(R.color.background)));
        window.getAttributes().windowAnimations = getStyle();

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        return inflater.inflate(getLayout(), container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TAG = getClass().getSimpleName();
        activity = (BaseActivity) getActivity();
    }

    public abstract int getLayout();

    public abstract int getStyle();

    public void dismiss(){
        dismissAllowingStateLoss();
    }

    public void show(FragmentManager fragmentManager) {
        fragmentManager.executePendingTransactions();
        if (!isAdded()) {
            show(fragmentManager, TAG);
        }
    }

    public void showLock(boolean flag){
        if(flag){
            showProgress();
        }else {
            dismissProgress();
        }
    }

    private void showProgress() {
        if (lock != null){
            if (!isLocked()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        {
                            lock.setVisibility(View.VISIBLE);
                        }
                    }
                });
                lock.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isLocked()) {
                            dismissProgress();
                        }
                    }
                }, 20 * 1000);
            }
        }
    }

    private void dismissProgress() {
        if (lock != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lock.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean isLocked() {
        return lock.getVisibility() == View.VISIBLE;
    }
}
