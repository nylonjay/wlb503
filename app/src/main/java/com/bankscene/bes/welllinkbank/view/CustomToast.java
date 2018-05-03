package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bankscene.bes.welllinkbank.R;


public class CustomToast extends Toast {

    public static final int SHOW_TIME = 1000;

    public CustomToast(Context context) {
        super(context);
    }

    public static CustomToast makeToast(Context context,
                                        int icon, CharSequence text, int duration) {
        CustomToast result = new CustomToast(context);

        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout._view_toast, null);

        IconFontTextView tips_icon = (IconFontTextView) view.findViewById(R.id.tips_icon);
        tips_icon.setText(context.getResources().getString(icon));
        tips_icon.setVisibility(View.VISIBLE);

        TextView tips_msg = (TextView) view.findViewById(R.id.tips_msg);
        tips_msg.setText(text);
        tips_msg.setVisibility(View.VISIBLE);

        result.setView(view);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }

    public static CustomToast makeToast(Context context, CharSequence text,
                                        int duration) {
        CustomToast result = new CustomToast(context);

        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout._view_toast, null);

        TextView tips_msg = (TextView) view.findViewById(R.id.tips_msg);
        tips_msg.setVisibility(View.VISIBLE);
        tips_msg.setText(text);

        result.setView(view);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }

    public static CustomToast makeToast(Context context, int icon, int duration) {
        CustomToast result = new CustomToast(context);
        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout._view_toast, null);
        IconFontTextView tips_icon = (IconFontTextView) view.findViewById(R.id.tips_icon);
        tips_icon.setText(context.getResources().getString(icon));
        tips_icon.setVisibility(View.VISIBLE);
        result.setView(view);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setDuration(duration);

        return result;
    }

    public void cancel(CustomToast customToast) {
        customToast.cancel();
    }

}