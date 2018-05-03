package com.bankscene.bes.welllinkbank.view.translucent;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bankscene.bes.welllinkbank.R;
import com.bankscene.bes.welllinkbank.view.IconFontTextView;


/**
 * 支持渐变的 actionBar
 */

public final class TranslucentActionBar extends LinearLayout {

    private String TAG = "TranslucentActionBar";
    private String type = "0";

    private View root;
    private View statusBar;
    private View actionBarView;

    public IconFontTextView title;

    private View left;
    private View right;

    private TextView leftText;
    private TextView rightText;

    private IconFontTextView leftIcon;
    private IconFontTextView rightIcon;

    private ImageView iv_title;
    private ImageView iv_Right;
    public static final int ICON_NULL = 0x00;
    public static final int ICON_BACK = 0x01;
    public static final int ICON_SEARCH = 0x02;
    public static final int ICON_SET = 0x03;
    public static final int ICON_LOCATION = 0x04;
    public static final int ICON_HOME = 0x05;
    public static final int ICON_MORE = 0x06;
    public static final int ICON_DATE = 0x07;
    public static final int ICON_SCAN = 0x08;
    public static final int ICON_ADD = 0x09;
    public static final int ICON_SHARE = 0x10;
    public static final int ICON_KEEP = 0x11;

    public TranslucentActionBar(Context context) {
        this(context, null);
    }

    public TranslucentActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TranslucentActionBar);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.TranslucentActionBar_type:
                    type = a.getString(attr);
                    break;
            }
        }
        a.recycle();
        init();
    }

    public void setTitleImage(){
    }

    public TranslucentActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        setOrientation(HORIZONTAL);
        View contentView;
        switch (type) {
            case "0":
                contentView = inflate(getContext(), R.layout._view_actionbar, this);
                break;
            case "1":
                contentView = inflate(getContext(), R.layout._view_actionbar_white, this);
                break;
            case "2":
                contentView = inflate(getContext(), R.layout._view_actionbar_life, this);
                break;
            default:
                contentView = inflate(getContext(), R.layout._view_actionbar, this);
        }


        root = contentView.findViewById(R.id.root);
        statusBar = contentView.findViewById(R.id.statusBar);
        actionBarView = contentView.findViewById(R.id.actionBarView);

        title = (IconFontTextView) contentView.findViewById(R.id.title);
        left = contentView.findViewById(R.id.left);
        right = contentView.findViewById(R.id.right);

        leftText = (TextView) contentView.findViewById(R.id.leftText);
        rightText = (TextView) contentView.findViewById(R.id.rightText);

        leftIcon = (IconFontTextView) contentView.findViewById(R.id.leftIcon);
        rightIcon = (IconFontTextView) contentView.findViewById(R.id.rightIcon);

        iv_title=(ImageView) contentView.findViewById(R.id.iv_title);
        iv_Right=(ImageView) contentView.findViewById(R.id.iv_right);
    }

    private void setActionBarHidden() {
        actionBarView.setVisibility(GONE);
    }
    public void setRightIconWithOutIconTXT(int resid){
        iv_Right.setBackgroundResource(resid);
    }
    public void setStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup.LayoutParams params = statusBar.getLayoutParams();
            params.height = getStatusBarHeight();
            statusBar.setLayoutParams(params);
        }
    }
    public void HideStatusBarHeight() {
        if (Build.VERSION.SDK_INT >= 19) {
            ViewGroup.LayoutParams params = statusBar.getLayoutParams();
            params.height = 0;
            statusBar.setLayoutParams(params);
        }
    }
    public void setTitleImageVisible(){
        title.setVisibility(View.GONE);
        iv_title.setVisibility(View.VISIBLE);
    }
    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public void setNeedTranslucent() {
        setNeedTranslucent(true, false);
    }


    public void setNeedTranslucent(boolean translucent, boolean titleInitVisible) {
        if (translucent) {
            root.setBackgroundDrawable(null);
        }
        if (!titleInitVisible) {
            title.setVisibility(View.GONE);
        }
    }

    public void setBackground(int color) {
        root.setBackgroundResource(color);
    }

    public void setTitle(String strTitle) {
        title.setText(strTitle);
    }

    public void setTitle(int id) {
        title.setText(getResources().getString(id));
    }
    public void setTitleColor(int id){
        title.setTextColor(id);
    }
    public IconFontTextView getTitleView() {
        return title;
    }

    public void setTitleListener(View.OnClickListener listener) {
        title.setOnClickListener(listener);
    }

    public void setText(TextView textView, String text) {
        textView.setText(text);
    }

    public void setIcon(IconFontTextView iconFontTextView, int iconId) {
        iconFontTextView.setText(getIconFont(iconId));
    }

    public void setActionBar(String strTitle, int resIdLeft, String strLeft, int resIdRight, String strRight,
                             final ActionBarClickListener listener) {

        setTitle(strTitle);
        setText(leftText, strLeft);
        setText(rightText, strRight);

        setIcon(leftIcon, resIdLeft);
        setIcon(rightIcon, resIdRight);

        if (listener != null) {
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLeftClick();
                }
            });
            right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onRightClick();
                }
            });
        }
    }

    private String getIconFont(int icon) {
//        switch (icon) {
//            case ICON_NULL:
//                return "";
//            case R.string.wlb_login:
//                return getResources().getString(icon);
//            case ICON_SEARCH:
//                return getResources().getString(R.string.icons_search);
//            case ICON_SET:
//                return getResources().getString(R.string.icons_set);
//            case ICON_LOCATION:
//                return getResources().getString(R.string.icons_location);
//            case ICON_HOME:
//                return getResources().getString(R.string.icons_home);
//            case ICON_MORE:
//                return getResources().getString(R.string.icons_more);
//            case ICON_DATE:
//                return getResources().getString(R.string.icons_date);
//            case ICON_SCAN:
//                return getResources().getString(R.string.icons_scan);
//            case ICON_ADD:
//                return getResources().getString(R.string.icons_add);
//            case ICON_SHARE:
//                return getResources().getString(R.string.icons_share);
//            case ICON_KEEP:
//                return getResources().getString(R.string.icons_keep);
//            default:
        if (icon==0){
            return "";
        }
        return getResources().getString(icon);
    }

    public View getHideView() {
        return root.findViewById(R.id.lifeView);
    }

    public View getLifeView() {
        return root.findViewById(R.id.actionBarView);
    }

    public View getLine() {
        return root.findViewById(R.id.line);
    }


}
