package com.example.dkt_group_beta.viewmodel;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import com.example.dkt_group_beta.activities.interfaces.LoginAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {
    private Context context;
    private LoginAction loginAction;
    private LoginViewModel loginViewModel;


    @Before
    public void setUp() {

        context = ApplicationProvider.getApplicationContext();
        loginViewModel = new LoginViewModel(context, loginAction);
    }

    @Test
    public void testgetSharedPreference() {
        try {

            LoginViewModel loginViewModel = new LoginViewModel(context, loginAction);
            SharedPreferences sharedPreferences = loginViewModel.getSharedPreference();
            assertEquals(true, sharedPreferences != null);
        } catch (GeneralSecurityException | IOException e) {
            return;
        }
    }
}