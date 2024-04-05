package com.example.dkt_group_beta.viewmodel;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.MasterKey;
import androidx.test.core.app.ApplicationProvider;

import com.example.dkt_group_beta.activities.interfaces.GameSearchAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {
    private Context context;
    private GameSearchAction gameSearchAction;
    private LoginViewModel loginViewModel;


    @Before
    public void setUp() {

        context = ApplicationProvider.getApplicationContext();
        loginViewModel = new LoginViewModel(context, gameSearchAction);
    }

    @Test
    public void testgetSharedPreference() {
        try {
            MasterKey masterKey = new MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();
            LoginViewModel loginViewModel = new LoginViewModel(context, gameSearchAction);
            SharedPreferences sharedPreferences = loginViewModel.getSharedPreference();
            assertEquals(true, sharedPreferences != null);
        } catch (GeneralSecurityException | IOException e) {
            return;
        }
    }
}
