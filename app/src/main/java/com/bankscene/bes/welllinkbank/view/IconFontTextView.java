package com.bankscene.bes.welllinkbank.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.bankscene.bes.welllinkbank.R;


/**
 * Created by tianwei on 2017/4/10.
 */

public class IconFontTextView extends AppCompatTextView {

    private String value = "";
    private String fontFile = "";
    private String TAG = "IconFontTextView";

    public IconFontTextView(Context context) {
        super(context);
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconFontTextView);
        final int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.IconFontTextView_value:
                    value = a.getString(attr);
                    setText(value);
                    Log.d(TAG, "value : " + value);
                    break;
                case R.styleable.IconFontTextView_fontFile:
                    fontFile = a.getString(attr);
                    Log.d(TAG, "fontFile : " + fontFile);
                    try {
                        Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontFile);
                        setTypeface(typeface);
                    } catch (Throwable e) {

                    }
                    break;
            }
        }
        a.recycle();
    }
}
