package com.example.dkt_group_beta.activities;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;
import com.example.dkt_group_beta.viewmodel.LoginViewModel;

public class MainActivityTest extends AppCompatActivity {
    private Button btn;
    private Button btn2;
    private GameSearchViewModel gameSearchViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.button);
        btn.setOnClickListener(this::onClick);
        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this::onClick2);

//        gameSearchViewModel = new GameSearchViewModel(getString(R.string.ip_address), "ASD", "ASD");
    }


    private void onClick(View view){

    }

    private void onClick2(View view){
        if (this.gameSearchViewModel == null)
            return;
        this.gameSearchViewModel.receiveGames();
    }

}