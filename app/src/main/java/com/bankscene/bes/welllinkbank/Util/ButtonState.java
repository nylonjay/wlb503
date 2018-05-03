package com.bankscene.bes.welllinkbank.Util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianwei on 2017/5/16.
 */

public class ButtonState {

    private List<Boolean> inputState;
    private List<EditText> editTextList;
    private TextView button;

    public ButtonState(List<EditText> editTextList, TextView button) {
        this.editTextList = editTextList;
        this.button = button;
    }

    public void onWatch(){
        initInputState();
        initListener();
    }

    private void initInputState() {
        inputState = new ArrayList<>();
        for (int i = 0; i < editTextList.size(); i++) {
            if (editTextList.get(i).getText().toString().trim().length() == 0) {
                inputState.add(i, false);
            } else {
                inputState.add(i, true);
            }
        }
        changeButton();
    }

    private void initListener() {
        for (int i = 0; i < editTextList.size(); i++) {
            final int position = i;
            editTextList.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        inputState.set(position, true);
                    } else {
                        inputState.set(position, false);
                    }
                    changeButton();
                }
            });
        }
    }

    private void changeButton() {
        if (checkEmpty()) {
            button.setEnabled(true);
            button.getBackground().setAlpha(255);
        } else {
            button.setEnabled(false);
            button.getBackground().setAlpha(51);
        }
    }

    private boolean checkEmpty() {
        for (int i = 0; i < editTextList.size(); i++) {
            if (!inputState.get(i)) {
                return false;
            }
        }
        return true;
    }

    public void setInputState(Editable s, int position){
        if (s.length() != 0) {
            inputState.set(position, true);
        } else {
            inputState.set(position, false);
        }
        changeButton();
    }
}
