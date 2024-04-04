package com.example.dkt_group_beta;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;

public class MainActivityTest extends AppCompatActivity {
    private Button btn;
    private Button btn2;

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

    }

    private void onClick(View view){
        String device_unique_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        WebsocketClientController.connectToServer(getString(R.string.ip_address), device_unique_id, "PlayerX");
    }

    private void onClick2(View view){
        new GameSearchViewModel().receiveGames();
    }

}