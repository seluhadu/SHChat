package com.seluhadu.shchat.multi_edittext;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.seluhadu.shchat.R;

public class EditTextActivity extends AppCompatActivity {
    private EditText fname;
    private EditText lname;
    private EditText email;
    private Button action;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiedit_activity);
        fname = findViewById(R.id.editText1);
        lname = findViewById(R.id.editText2);
        email = findViewById(R.id.editText3);
        action= findViewById(R.id.button);
        EditText[] editTexts = {fname, lname, email};
        MultiWatcher multiWatcher = new MultiWatcher(editTexts, action);
        for (EditText editText : editTexts) {
            editText.addTextChangedListener(multiWatcher);
        }
    }
}
