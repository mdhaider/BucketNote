package com.ennjapps.bucketnote.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ennjapps.bucketnote.R;
import com.ennjapps.bucketnote.extras.MyPreferences;

/**
 * Created by haider on 07-05-2016.
 */
public class FirstTimeLogin extends Activity {


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            checkFieldsForEmptyValues();
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firsttimelogin);
        EditText usrName = (EditText) findViewById(R.id.editText);
        usrName.addTextChangedListener(textWatcher);
        MyPreferences pref = new MyPreferences(FirstTimeLogin.this);
        if (!pref.isFirstTime()) {
            Intent i = new Intent(getApplicationContext(), ActivityMain.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            finish();
        }
    }

    public void SaveUserName(View v) {
        EditText usrName = (EditText) findViewById(R.id.editText);
        MyPreferences pref = new MyPreferences(FirstTimeLogin.this);
        pref.setUsername(usrName.getText().toString().trim());
        Intent i = new Intent(getApplicationContext(), ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);
        finish();
    }

    private void checkFieldsForEmptyValues() {
        Button b = (Button) findViewById(R.id.button);
        EditText usrName = (EditText) findViewById(R.id.editText);
        String itemText = usrName.getText().toString();


        if (itemText.length() > 2) {
            b.setEnabled(true);
        } else {
            b.setEnabled(false);
        }
    }
}