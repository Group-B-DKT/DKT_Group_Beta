package com.example.dkt_group_beta.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.dkt_group_beta.activities.interfaces.LoginAction;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class LoginViewModel {
    private final Context context;
    private final LoginAction loginAction;

    public LoginViewModel(Context applicationContext, LoginAction loginAction) {

        context = applicationContext;
        this.loginAction = loginAction;

    }

    public void onLogin(String username) {

        Log.d("debug", username);
        try {
            SharedPreferences sharedPreferences = getSharedPreference();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("Username", username);
            editor.apply();
        } catch (GeneralSecurityException | IOException e) {
            return;
        }
        loginAction.switchToGameView(username);

    }

    public String getSavedUsername() {
        try {
            SharedPreferences sharedPreferences = getSharedPreference();
            return sharedPreferences.getString("Username", null);
        } catch (GeneralSecurityException | IOException e) {
            return null;
        }
    }

    public void checkSavedUsername() {
        String username = getSavedUsername();
        if (username == null) {
            return;
        }
        loginAction.switchToGameView(username);
    }

    public SharedPreferences getSharedPreference() throws GeneralSecurityException, IOException {
        MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
        return EncryptedSharedPreferences.create(context, "encrypted_preferences",
                masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }


}
