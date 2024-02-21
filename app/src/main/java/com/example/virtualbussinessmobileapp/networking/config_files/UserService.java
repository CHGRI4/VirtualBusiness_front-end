package com.example.virtualbussinessmobileapp.networking.config_files;

import com.example.virtualbussinessmobileapp.networking.data.LoginRequest;
import com.example.virtualbussinessmobileapp.networking.data.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("authenticate/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);
}
