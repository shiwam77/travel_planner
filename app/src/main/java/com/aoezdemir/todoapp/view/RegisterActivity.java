package com.aoezdemir.todoapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.crud.database.TodoDBHelper;
import com.aoezdemir.todoapp.model.Todo;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private EditText etConPassword;
    private Button bLogin;
    private TodoDBHelper db;
    private TextView tvErrorInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etEmail = findViewById(R.id.etLoginEmail);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorInfo.setVisibility(View.INVISIBLE);
                if (isValidEmailAddress()) {
                    etEmail.setError(null);
                    etEmail.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidPassword()) {
                        enableLoginButton();
                    }
                } else {
                    etEmail.setError("Please provide a valid email address");
                    etEmail.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });
        etPassword = findViewById(R.id.etLoginPassword);
        etConPassword = findViewById(R.id.etConLoginPassword);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorInfo.setVisibility(View.INVISIBLE);
                String password = etPassword.getText().toString().trim();
                String conPassword = etConPassword.getText().toString().trim();
                if (isValidPassword()) {
                    etPassword.setError(null);
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidEmailAddress() && password.equals(conPassword)) {
                        enableLoginButton();
                    }
                } else {
                    etPassword.setError("The password must be equals  or greater than 8 characters");
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });

        etConPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvErrorInfo.setVisibility(View.INVISIBLE);
                String password = etPassword.getText().toString().trim();
                if (password.equals(etConPassword.getText().toString().trim()) && isValidEmailAddress()) {
                    etConPassword.setError(null);
                    etConPassword.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidEmailAddress()) {
                        enableLoginButton();
                    }
                } else {
                    etConPassword.setError("Confirm password does not matched");
                    etConPassword.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });
        tvErrorInfo = findViewById(R.id.tvErrorInfo);
        tvErrorInfo.setVisibility(View.INVISIBLE);


        bLogin = findViewById(R.id.bLogin);
        disableLoginButton();
        bLogin.setOnClickListener((View v) -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            Intent intent = new Intent(v.getContext(), OverviewActivity.class);
            intent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, true);
            startActivity(intent);

        });
    }

    private boolean isValidEmailAddress() {
        String email = etEmail.getText().toString();
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        return !password.isEmpty() && password.length() >= 8;
    }

    private void enableLoginButton() {
        bLogin.setEnabled(true);
        bLogin.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
    }

    private void disableLoginButton() {
        bLogin.setEnabled(false);
        bLogin.setBackgroundColor(getResources().getColor(R.color.colorTodoTitleDone, null));
    }
}