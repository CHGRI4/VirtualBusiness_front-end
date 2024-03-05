package com.example.virtualbussinessmobileapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.virtualbussinessmobileapp.R;
import com.example.virtualbussinessmobileapp.networking.config_files.ApiClient;
import com.example.virtualbussinessmobileapp.global_data.LoggedUserInfo;
import com.example.virtualbussinessmobileapp.networking.data.LoginRequest;
import com.example.virtualbussinessmobileapp.networking.data.LoginResponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username_input_field, password_input_field;
    TextView prompt_register_but;
    Button login_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        username_input_field = findViewById(R.id.editTextLoginUsername);
        password_input_field = findViewById(R.id.editTextLoginPassword);
        login_button = findViewById(R.id.LoginButton);

        SpannableString ss = new SpannableString("Don't have an account? Click here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 23, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        TextView textView = (TextView) findViewById(R.id.textViewPromptRegister);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setHighlightColor(Color.TRANSPARENT);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked login_button");
                if(TextUtils.isEmpty(username_input_field.getText().toString()) || TextUtils.isEmpty(password_input_field.getText().toString())){
                    System.out.println("Empty username or password field");
                    Toast.makeText(LoginActivity.this, "Username/Password required", Toast.LENGTH_LONG).show();
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
                if(response.isSuccessful() && Objects.equals(Objects.requireNonNull(response.body()).getStatus(), "ok")) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                    LoginResponse loginResponse = response.body();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //go to next page
                            assert loginResponse != null;
                            LoggedUserInfo.setUsername(subUsername);
                            LoggedUserInfo.setFirst_name(loginResponse.getName());
                            LoggedUserInfo.setSurname(loginResponse.getSurname());
                            LoggedUserInfo.setEmail(loginResponse.getEmail());
                            LoggedUserInfo.setNonce(loginResponse.getNonce());
                            startActivity(new Intent(LoginActivity.this, MainMapActivity.class));
                        }
                    },400);
                }else{
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}