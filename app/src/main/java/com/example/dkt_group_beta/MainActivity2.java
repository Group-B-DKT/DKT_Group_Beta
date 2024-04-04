package com.example.dkt_group_beta;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;

public class MainActivity2 extends AppCompatActivity {

    GameSearchViewModel gameSearchViewModel;
    TextView games;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.searchGames);
        games = findViewById(R.id.games);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameSearchViewModel = new GameSearchViewModel();
                games.setText("gameSearchViewModel.receiveGames()");
            }
        });
    }

}
