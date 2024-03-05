package com.example.virtualbussinessmobileapp.networking.config_files;

import com.example.virtualbussinessmobileapp.networking.data.LoginRequest;
import com.example.virtualbussinessmobileapp.networking.data.LoginResponse;
import com.opencsv.CSVReader;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @POST("authenticate/")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);

    @GET("/data/data01.csv?")
    Call<CSVReader> getExcelFile();


}
