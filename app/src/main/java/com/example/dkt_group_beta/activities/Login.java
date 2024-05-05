package com.example.dkt_group_beta.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.viewmodel.LoginViewModel;

public class Login extends AppCompatActivity {

    private Button buttonLogin;
    private Button buttonBoard;
    private EditText etUsername;
    private LoginViewModel loginViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loginViewModel = new LoginViewModel(this.getApplicationContext(), this::switchToGameView);
        loginViewModel.checkSavedUsername();
        etUsername = findViewById(R.id.BenutzerEingabe);
        buttonLogin = findViewById(R.id.LoginButton2);
        buttonLogin.setOnClickListener(v -> loginViewModel.onLogin(etUsername.getText().toString()));
        Log.d("debug", "Gespeicherte Namen" + loginViewModel.getSavedUsername());

        buttonBoard = findViewById(R.id.buttonBoard);
        buttonBoard.setOnClickListener(v -> {
                Intent intent = new Intent(Login.this, GameBoard.class);
                startActivity(intent);
        });
    }

    public void switchToGameView (String username) {
        Log.d("debug", "Gespeicherte Namen :" + username);
        Intent intent = new Intent(Login.this, GameSearch.class);
        intent.putExtra("username", username);
        startActivity(intent);


    }
}