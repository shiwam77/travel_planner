package com.aoezdemir.todoapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText etEmail;
    private EditText etPassword;
    private Button bLogin;
    private Button bRegister;
    private TextView tvErrorInfo;
    private String globalEmail = "anitha77@gmail.com";
    private String globalPassword = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                if (isValidPassword()) {
                    etPassword.setError(null);
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextDefault, null));
                    if (isValidEmailAddress()) {
                        enableLoginButton();
                    }
                } else {
                    etPassword.setError("The password must provide exactly 6 numbers");
                    etPassword.setTextColor(getResources().getColor(R.color.colorTextError, null));
                    disableLoginButton();
                }
            }
        });
        tvErrorInfo = findViewById(R.id.tvErrorInfo);
        tvErrorInfo.setVisibility(View.INVISIBLE);

        bLogin = findViewById(R.id.bLogin);
        bRegister = findViewById(R.id.bRegister);
        disableLoginButton();
        bRegister.setOnClickListener((View v) ->{
            Intent intent = new Intent(v.getContext(), RegisterActivity.class);
            startActivity(intent);
        });
        bLogin.setOnClickListener((View v) -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if(email.trim().equals(globalEmail.trim()) && password.trim().equals(globalPassword.trim())
            || !email.trim().equals(globalEmail.trim())
            ){
                Intent intent = new Intent(v.getContext(), OverviewActivity.class);
                intent.putExtra(RouterEmptyActivity.INTENT_IS_WEB_API_ACCESSIBLE, true);
                startActivity(intent);
            }else{
                tvErrorInfo.setVisibility(View.VISIBLE);
                Toast.makeText(v.getContext(), "Local error: Failed to authenticate user (client error)", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isValidEmailAddress() {
        String email = etEmail.getText().toString();
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword() {
        String password = etPassword.getText().toString();
        return !password.isEmpty() && password.length() == 6;
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