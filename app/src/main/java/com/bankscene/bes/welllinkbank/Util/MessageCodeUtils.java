package com.bankscene.bes.welllinkbank.Util;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;

public class MessageCodeUtils extends CountDownTimer {

    private TextView button;
    private Context context;

    public MessageCodeUtils(long millisInFuture, TextView button,Context mContext) {

        super(millisInFuture - 1, 1000);
        this.button = button;
        this.context=mContext;
    }

    @Override
    public void onFinish() {
        button.setClickable(true);
        button.setTextColor(Color.parseColor("#d13231"));
        button.setSelected(false);
        button.setText(context.getResources().getString(R.string.recapture));
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);
        button.setTextColor(Color.parseColor("#666666"));
        button.setText(context.getResources().getString(R.string.recapture) + "(" + millisUntilFinished / 1000 + ")");
        button.setSelected(true);
    }

    public void stop() {
        this.cancel();
        this.onFinish();
    }
}
