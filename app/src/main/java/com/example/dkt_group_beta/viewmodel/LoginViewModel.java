package com.example.dkt_group_beta.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;
import androidx.security.crypto.EncryptedSharedPreferences;

import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;




public class LoginViewModel {
    private final Context context;

    public LoginViewModel(Context applicationContext) {
        context = applicationContext;
    }


    public void onLogin (String username) {

        Log.d("debug", username);
        try {
            SharedPreferences sharedPreferences = getSharedPreference();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Username", username);
            editor.apply();
        }catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

    }

    public String getSavedUsername() {
        try {
            SharedPreferences sharedPreferences = getSharedPreference();
            return sharedPreferences.getString("Username", "");
        }catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return "";
        }

    }
    private SharedPreferences getSharedPreference () throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
        SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(context, "encrypted_preferences",
                masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
        return sharedPreferences;
    }




}
