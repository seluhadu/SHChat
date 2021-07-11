package com.seluhadu.shchat.multi_edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class MultiWatcher implements TextWatcher {

    private EditText[] editTexts;
    private View view;

    public MultiWatcher(EditText[] editTexts, View view) {
        this.editTexts = editTexts;
        this.view = view;
        view.setEnabled(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().length() <= 0) {
                view.setEnabled(false);
                break;
            } else{
                view.setEnabled(true);
            }
        }
    }
}
