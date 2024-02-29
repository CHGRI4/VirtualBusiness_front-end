package com.example.virtualbussinessmobileapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.virtualbussinessmobileapp.R;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        Button registerButton = findViewById(R.id.RegisterButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"WrongConstant", "ShowToast"})
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Register in development (data not submitted)", 2).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
