package com.example.dkt_group_beta.viewmodel;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dkt_group_beta.activities.interfaces.LoginAction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RunWith(RobolectricTestRunner.class)
public class LoginViewModelTest {
    private Context context;
    private LoginAction loginAction;
    private LoginViewModel loginViewModel;
    @BeforeEach
    public void setUp() {

        context = RuntimeEnvironment.getApplication();
        loginViewModel = new LoginViewModel(context, loginAction);
    }
    @Test
    public void testgetSharedPreference() {
        try {

            LoginViewModel loginViewModel = new LoginViewModel(context, loginAction);
            SharedPreferences sharedPreferences = loginViewModel.getSharedPreference();
            assertNotNull(sharedPreferences);
        } catch (GeneralSecurityException | IOException e) {
            return;
        }
    }
}
