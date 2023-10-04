package com.aoezdemir.todoapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aoezdemir.todoapp.R;
import com.aoezdemir.todoapp.components.AlertDialogMaker;

public class SettingActivity extends AppCompatActivity {
    private Button bLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        bLogout = findViewById(R.id.logoutButton);

        bLogout.setOnClickListener((View v) -> {
            // Create an intent to open the LoginActivity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            // Clear the back stack and start LoginActivity as a new task
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            // Start LoginActivity
            startActivity(intent);
        });

    }
}