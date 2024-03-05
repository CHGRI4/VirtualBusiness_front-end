package com.example.virtualbussinessmobileapp.global_data;

import android.app.Application;

public class LoggedUserInfo extends Application {
    public static String username, email, first_name, surname, nonce, acc_level;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        LoggedUserInfo.username = username;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        LoggedUserInfo.email = email;
    }

    public static String getFirst_name() {
        return first_name;
    }

    public static void setFirst_name(String first_name) {
        LoggedUserInfo.first_name = first_name;
    }

    public static String getSurname() {
        return surname;
    }

    public static void setSurname(String surname) {
        LoggedUserInfo.surname = surname;
    }

    public static String getNonce() {
        return nonce;
    }

    public static void setNonce(String nonce) {
        LoggedUserInfo.nonce = nonce;
    }

    public static String getAcc_level() {
        return acc_level;
    }

    public static void setAcc_level(String acc_level) {
        LoggedUserInfo.acc_level = acc_level;
    }
}