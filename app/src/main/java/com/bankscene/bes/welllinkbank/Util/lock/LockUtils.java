package com.bankscene.bes.welllinkbank.Util.lock;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;


public class LockUtils {

    private Activity activity;
    private FrameLayout mProgress;
    private TextView mLockTips;

    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (isLocked()) {
                dismissProgress();
            }
        }
    };

    public LockUtils(Activity activity) {
        this.activity = activity;
    }

    public void setProgressDisplay(boolean isShow) {
        if (isShow) {
            showProgress();
        } else {
            dismissProgress();
        }
    }

    private boolean isLocked() {
        try {
            if (null != mProgress) {
                return mProgress.isShown();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void setLockTips(String tips) {
        if (mLockTips != null) {
            mLockTips.setText(tips);
        }
    }

    private void showProgress() {
        if (!isLocked()) {
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgress == null) {
                        mProgress = (FrameLayout) LayoutInflater.from(activity).inflate(R.layout._view_lock, null);
                        mLockTips = (TextView) mProgress.findViewById(R.id.lock_tips);
                        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                                ActionBar.LayoutParams.MATCH_PARENT);
                        activity.addContentView(mProgress, params);
                        mProgress.setVisibility(View.VISIBLE);
                    } else {
                        mProgress.setVisibility(View.VISIBLE);
                    }
                }
            });
            mProgress.postDelayed(runnable, 20 * 1000);
        }
    }

    private void dismissProgress() {
        if (mProgress != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgress.setVisibility(View.GONE);
                }
            });
        }
    }
}
