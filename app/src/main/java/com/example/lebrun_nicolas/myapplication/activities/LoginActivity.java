package com.example.lebrun_nicolas.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;

import com.example.lebrun_nicolas.myapplication.R;
import com.example.lebrun_nicolas.myapplication.helpers.CozyHelper;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity{

    private EditText urlInputField;
    private EditText pwdInputField;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if( PreferenceManager.getDefaultSharedPreferences(this).getString("token", null) == null) {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            this.urlInputField = (EditText) findViewById(R.id.urlInputField);
            this.pwdInputField = (EditText) findViewById(R.id.pwdInputField);
            this.connectButton = (Button) findViewById(R.id.connectButton);

            this.connectButton.setEnabled(false);

            urlInputField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(urlInputField.getText().toString())){
                        urlInputField.setError("Ce champ ne doit pas être vide.");
                    } else {
                        if(!URLUtil.isValidUrl(urlInputField.getText().toString())) {
                            urlInputField.setError("Veuillez saisir une URL valide.");
                        }
                    }

                    disableConnectButtonIfNecessary();
                }
            });

            pwdInputField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(TextUtils.isEmpty(pwdInputField.getText().toString())) {
                        pwdInputField.setError("Ce champ ne doit pas être vide.");
                    }

                    disableConnectButtonIfNecessary();
                }
            });
        } else {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) throws IOException {

        CozyHelper connection = CozyHelper.getInstance(this.getApplicationContext());
        connection.connectToCozy(this.urlInputField.getText().toString(), this.pwdInputField.getText().toString());
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);

    }

    public void disableConnectButtonIfNecessary() {
        this.connectButton.setEnabled(!this.urlInputField.getText().toString().equals("")
                && !this.pwdInputField.getText().toString().equals("")
                && URLUtil.isValidUrl(this.urlInputField.getText().toString()));
    }

}
