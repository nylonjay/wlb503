package com.bankscene.bes.welllinkbank.Util.notice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.bankscene.bes.welllinkbank.MainActivity;
import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.core.SplashActivity;
import com.bankscene.bes.welllinkbank.view.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class NoticeUtils {

    private Context context;
    private List<Toast> toastList;

    public NoticeUtils(Context context) {
        this.context = context;
    }
    class TimeCount extends CountDownTimer {
        AlertDialog ad;

        public TimeCount(long millisInFuture, long countDownInterval, AlertDialog ad) {
            super(millisInFuture, countDownInterval);
            this.ad = ad;
        }

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            ad.dismiss();
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            String text = millisUntilFinished / 1000 + "秒 跳过";
//            show.setText(text);
        }
    }
    public void showNotice(String notice) {
        if (null != notice && !notice.equals("")) {
//            Toast toast = Toast.makeText(context, notice, Toast.LENGTH_SHORT);
//            toast.show();
//            if (toastList == null) {
//                toastList = new ArrayList<>();
//            }
//            toastList.add(toast);
            final AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setCancelable(false);
            new TimeCount(1500,1000,dialog).start();
            dialog.show();
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setContentView(R.layout.layout_dialog_message);
            TextView tv=window.findViewById(R.id.tv);
            tv.setText(notice);

        }
    }



    public void showNotice(int icon, CharSequence notice) {
        CustomToast toast = CustomToast.makeToast(context, icon, notice, CustomToast.SHOW_TIME);
        toast.show();
        if (toastList == null) {
            toastList = new ArrayList<>();
        }
        toastList.add(toast);
    }

    public void cancelNotice() {
        if (toastList != null) {
            for (int i = 0; i < toastList.size(); i++) {
                toastList.get(i).cancel();
            }
        }
    }
}
