package com.bankscene.bes.welllinkbank.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.bankscene.bes.welllinkbank.R;


public class PopupWindowUtils {

    public final static int STYLE_I = 1;
    public final static int STYLE_II = 2;

    private Activity activity;
    private PopupWindow popupWindow;
    private View layoutView;
    private View rootView;
    private int style;

    public PopupWindowUtils(Activity activity, View layoutView, View rootView, int style) {

        this.activity = activity;
        this.layoutView = layoutView;
        this.rootView = rootView;
        this.style = style;
        switch (style) {
            case STYLE_I:
                init_Style_I();
                break;
            case STYLE_II:
                init_Style_II();
                break;
        }
    }

    private void init_Style_I() {
        popupWindow = new PopupWindow(layoutView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow_Style_I);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void init_Style_II() {
        popupWindow = new PopupWindow(layoutView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.PopupWindow_Style_II);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    public void show() {
        switch (style) {
            case STYLE_I:
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
            case STYLE_II:
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                break;
        }
        backgroundAlpha(0.4f);
    }

    public void show(int position) {
        switch (style) {
            case STYLE_I:
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, position);
                break;
            case STYLE_II:
                popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, position);
                break;
        }
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    public void setDismissListener(PopupWindow.OnDismissListener listener){
        popupWindow.setOnDismissListener(listener);
    }

}
