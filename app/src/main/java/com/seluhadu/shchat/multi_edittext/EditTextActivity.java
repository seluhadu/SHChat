package com.seluhadu.shchat.multi_edittext;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class EditTextActivity extends AppCompatActivity {
    private EditText fname;
    private EditText lname;
    private EditText email;
    private Button action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditText[] editTexts = {
                fname, lname, email
        };
        MultiWatcher multiWatcher = new MultiWatcher(editTexts, action);
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(multiWatcher);
        }
    }
}
