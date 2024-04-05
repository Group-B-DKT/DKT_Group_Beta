package com.example.dkt_group_beta.activities;

import android.icu.text.ListFormatter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.activities.interfaces.GameSearchAction2;
import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameSearch extends AppCompatActivity implements GameSearchAction2 {
    private LinearLayout scrollviewLayout;
    private GameSearchViewModel gameSearchViewModel;
    private List<TextView> gameFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gamesearch);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String device_unique_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        this.gameSearchViewModel = new GameSearchViewModel(getString(R.string.ip_address), getIntent().getStringExtra("username"), device_unique_id, this);

        this.scrollviewLayout = (LinearLayout) findViewById(R.id.scrollview_layout);
        this.gameFields = new ArrayList<>();
    }

    @Override
    public void addGameToScrollView(int gameId, int amountOfPLayer){
        Log.d("DEBUG", "GameSearch::addGameToScrollView/ " + gameId + ", " + amountOfPLayer);
        runOnUiThread(() -> {
            TextView textView = new TextView(this);
            textView.setText(String.format(Locale.GERMAN,"Spiel %d \t %d/6", gameId, amountOfPLayer));
            textView.setId(gameId);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    100));
            textView.setTextSize(20);
            scrollviewLayout.addView(textView);
            this.gameFields.add(textView);
        });


    }
    @Override
    public void onConnectionEstablished(){
        Log.d("DEBUG", "GameSearch::onConnectionEstablished/ ");
        this.gameSearchViewModel.receiveGames();
    }


}