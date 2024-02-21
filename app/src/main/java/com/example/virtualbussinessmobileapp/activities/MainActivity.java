package com.example.virtualbussinessmobileapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.virtualbussinessmobileapp.R;
import com.example.virtualbussinessmobileapp.networking.config_files.ApiClient;
import com.example.virtualbussinessmobileapp.global_data.LoggedUserInfo;
import com.example.virtualbussinessmobileapp.networking.data.LoginRequest;
import com.example.virtualbussinessmobileapp.networking.data.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText username_input_field, password_input_field;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        LoggedUserInfo.email="Hello";
        username_input_field = findViewById(R.id.editTextLoginUsername);
        password_input_field = findViewById(R.id.editTextLoginPassword);
        login_button = findViewById(R.id.LoginButton);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked login_button");
                if(TextUtils.isEmpty(username_input_field.getText().toString()) || TextUtils.isEmpty(password_input_field.getText().toString())){
                    System.out.println("Empty username or password field");
                    Toast.makeText(MainActivity.this, "Username/Password required", Toast.LENGTH_LONG).show();
                }else {
                    login();
                }
            }
        });
    }

    public void login(){
        LoginRequest loginRequest = new LoginRequest();
        String subUsername = username_input_field.getText().toString();
        String subPassword = password_input_field.getText().toString();
        loginRequest.setUsername(subUsername);
        loginRequest.setPassword(subPassword);

        Call<LoginResponse> LoginResponseCall = ApiClient.getUserService().userLogin(loginRequest);
        LoginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    LoginResponse loginResponse = response.body();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //go to next page
                            assert loginResponse != null;
                            LoggedUserInfo.username = subUsername;
                            LoggedUserInfo.first_name = loginResponse.getName();
                            LoggedUserInfo.surname = loginResponse.getSurname();
                            LoggedUserInfo.email = loginResponse.getEmail();

                            startActivity(new Intent(MainActivity.this, LoggedIn_activity.class));
                        }
                    },700);
                }else{
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}