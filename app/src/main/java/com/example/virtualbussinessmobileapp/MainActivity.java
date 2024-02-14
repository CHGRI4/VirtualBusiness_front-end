package com.example.virtualbussinessmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        @SuppressLint({"ResourceType", "MissingInflatedId", "LocalSuppress"}) ImageView background = findViewById(R.drawable.ancient_guardians_logo_tel_login);
    }

}