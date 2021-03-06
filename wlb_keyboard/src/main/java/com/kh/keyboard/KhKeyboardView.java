package com.kh.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;


public class KhKeyboardView {
    private final Dialog mPopuWindow;
    private Activity mContext;
    private View parentView;
    private KeyboardView mLetterView;   //字母键盘view
    private KeyboardView mNumberView;   //数字键盘View
    private Keyboard mNumberKeyboard;   // 数字键盘
    private Keyboard mLetterKeyboard;   // 字母键盘
    private Keyboard mSymbolKeyboard;   // 符号键盘

    private boolean isNumber = true;    // 是否数字键盘
    public static   boolean isUpper = false;    // 是否大写
    private boolean isSymbol = false;   // 是否是符号
    private EditText mEditText;
    //    private View headerView;
    private Handler mHander;

    public void setEditText(EditText text) {
        mEditText = text;
    }

    public KhKeyboardView(Activity context, View view,Dialog popupWindow) {
        mContext = context;
        parentView = view;
        mPopuWindow=popupWindow;
        mNumberKeyboard = new Keyboard(mContext, R.xml.keyboard_numbers);
        mLetterKeyboard = new Keyboard(mContext, R.xml.keyboard_word);
        mSymbolKeyboard = new Keyboard(mContext, R.xml.keyboard_symbol);
        mNumberView = (KeyboardView) parentView.findViewById(R.id.keyboard_view);
        mLetterView = (KeyboardView) parentView.findViewById(R.id.keyboard_view_2);
        mNumberView.setKeyboard(mNumberKeyboard);
        mNumberView.setEnabled(true);
        mNumberView.setPreviewEnabled(false);
        mNumberView.setOnKeyboardActionListener(listener);
        mLetterView.setKeyboard(mLetterKeyboard);
        mLetterView.setEnabled(true);
        mLetterView.setPreviewEnabled(true);
        mLetterView.setOnKeyboardActionListener(listener);
//        headerView = parentView.findViewById(R.id.keyboard_header);

    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void onPress(int primaryCode) {
            Log.d("primaryCode","onPress--"+primaryCode);
            if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                List<Keyboard.Key> keyList = mLetterKeyboard.getKeys();
                mLetterView.setPreviewEnabled(false);
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {
                mLetterView.setPreviewEnabled(false);
            } else if (primaryCode == 32) {
                mLetterView.setPreviewEnabled(false);
            }else if (primaryCode==90001){
                mLetterView.setPreviewEnabled(false);
            }
            else {
                mLetterView.setPreviewEnabled(false);
            }

        }

        @Override
        public void onRelease(int primaryCode) {
            Log.d("primaryCode","onRelease--"+primaryCode);

//            if(primaryCode== -1){
//                if(isUpper){
//                    isUpper=false;
//                }else {
//                    isUpper=true;
//                }
//            }
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Log.d("primaryCode","onKey--"+primaryCode);
            try {
                if (mEditText == null)
                    return;
                Editable editable = mEditText.getText();
                int start = mEditText.getSelectionStart();
                if (primaryCode == Keyboard.KEYCODE_CANCEL) {
                    // 隐藏键盘
                    hideKeyboard();
                } else if (primaryCode == Keyboard.KEYCODE_DELETE || primaryCode == -35) {

                    // 回退键,删除字符
                    if (editable != null && editable.length() > 0) {
                        if (start > 0) {
                            editable.delete(start - 1, start);
                        }
                    }
                } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {
                    // 大小写切换
                    changeKeyboart();
                    mLetterView.setKeyboard(mLetterKeyboard);

                } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
                    // 数字与字母键盘互换
                    if (isNumber) {
                        showLetterView();
//                        showLetterView2();
                    } else {
//                        showNumberView();
                    }

                } else if (primaryCode == 90001) {
                    hideKeyboard();
                } else {
                    // 输入键盘值
                    Vibrator vibrator=(Vibrator)mContext.getSystemService(Service.VIBRATOR_SERVICE);
                    vibrator.vibrate(new long[]{0,50}, -1);
                    editable.insert(start, Character.toString((char) primaryCode));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    //  字母-符号,显示字母
//    private void showLetterView2() {
//        if (mLetterView != null) {
//            isSymbol = false;
//            mLetterView.setKeyboard(mLetterKeyboard);
//        }
//    }

    //  字母-符号,显示符号
    private void showSymbolView() {
        try {
            if (mLetterKeyboard != null) {
                isSymbol = true;
                mLetterView.setKeyboard(mSymbolKeyboard);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    //  数字-字母,显示字母键盘
    private void showLetterView() {
        List<Keyboard.Key> keys = mLetterKeyboard.getKeys();
        List<Keyboard.Key> newkeyList = new ArrayList<Keyboard.Key>();
        for (Keyboard.Key key : keys) {
            if (key.label!=null
                    && isInteger(key.label.toString()
            )){
                newkeyList.add(key);
            }
        }

        int count = newkeyList.size();
        List<KeyModel> resultList = new ArrayList<KeyModel>();
        LinkedList<KeyModel> temp=new LinkedList<KeyModel>();
        for (int i = 0; i < count; i++) {
            temp.add(new KeyModel(48 + i, i + ""));
        }

//        Random random = new Random();
//        for (int i = 0; i < count; i++) {
//            int index = random.nextInt(count - i);
//            KeyModel keyModel = temp.get(index);
//            newkeyList.get(i).label=keyModel.getLable();
//            newkeyList.get(i).codes[0]=keyModel.getCode();
//            temp.remove(index);
//        }
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            int num = rand.nextInt(count - i);
            resultList.add(new KeyModel(temp.get(num).getCode(), temp.get(num)
                    .getLable()));
            temp.remove(num);
        }
        for (int i = 0; i < newkeyList.size(); i++) {
            newkeyList.get(i).label = resultList.get(i).getLable();
            newkeyList.get(i).codes[0] = resultList.get(i).getCode();
        }

        try {
            if (mLetterView != null && mNumberView != null) {
                isNumber = false;
                mLetterView.setKeyboard(mLetterKeyboard);
                mLetterView.setVisibility(View.VISIBLE);
                mNumberView.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 数字-字母, 显示数字键盘
//    private void showNumberView() {
//        try {
//            if (mLetterView != null && mNumberView != null) {
//                isNumber = true;
//                mLetterView.setVisibility(View.INVISIBLE);
//                mNumberView.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 切换大小写
     */
    public void changeKeyboart() {
        List<Keyboard.Key> keyList = mLetterKeyboard.getKeys();
        if (isUpper) {
            // 大写切换小写
            isUpper = false;
            for (Keyboard.Key key : keyList) {
                Drawable icon = key.icon;

                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toLowerCase();
                    key.codes[0] = key.codes[0] + 32;
                }
            }
        } else {
            // 小写切换成大写
            isUpper = true;
            for (Keyboard.Key key : keyList) {
                if (key.label != null && isLetter(key.label.toString())) {
                    key.label = key.label.toString().toUpperCase();
                    key.codes[0] = key.codes[0] - 32;
                }
            }
        }
    }

    /**
     * 判断是否是字母
     */
    private boolean isLetter(String str) {
        String wordStr = "abcdefghijklmnopqrstuvwxyz";
        return wordStr.contains(str.toLowerCase());
    }

    public void hideKeyboard() {
        try {
            int visibility = mLetterView.getVisibility();
            if (visibility == View.VISIBLE) {
                mLetterView.setVisibility(View.GONE);
            }
            visibility = mNumberView.getVisibility();
            if (visibility == View.VISIBLE) {
                mNumberView.setVisibility(View.GONE);
            }
//            if (isUpper){
//                isUpper=false;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        mPopuWindow.dismiss();
    }

    /**
     * 显示键盘
     *
     * @param editText
     */
    public void showKeyboard(EditText editText) {
        try {
            this.mEditText = editText;
            int visibility = 0;
            int inputText = mEditText.getInputType();
//            headerView.setVisibility(View.VISIBLE);
            if (isUpper){
                isUpper=false;
                changeKeyboart();
            }else {
            }
            showLetterView();
//            switch (inputText) {
//                case InputType.TYPE_CLASS_NUMBER:
//                    showNumberView();
//                    break;
//                case InputType.TYPE_CLASS_PHONE:
//                    showNumberView();
//                    break;
//                case InputType.TYPE_NUMBER_FLAG_DECIMAL:
//                    showNumberView();
//                    break;
//                default:
//                    showLetterView();
//                    break;
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
