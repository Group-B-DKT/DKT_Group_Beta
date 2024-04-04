package com.example.dkt_group_beta.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import lombok.AllArgsConstructor;


public class LoginViewModel {
    private Context context;

    public LoginViewModel(Context applicationContext) {
        context = applicationContext;
    }


    public void onLogin (String username) {
        Log.d("debug", username);
         SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

         editor.putString("Username", username);
         editor.apply();


    }

    public String getSavedUsername() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Username", "");
    }




}
