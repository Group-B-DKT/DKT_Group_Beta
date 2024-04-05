package com.example.dkt_group_beta.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            LinearLayout linearLayout = getLinearLayout(gameId);

            TextView textViewGameId = getTextView(String.format(Locale.GERMAN, "Spiel %d", gameId), View.TEXT_ALIGNMENT_TEXT_START);

            String status = String.format("%s", amountOfPLayer == 6 ? "Starting..." : "Waiting...");
            TextView textViewPlayerStatus = getTextView(status, View.TEXT_ALIGNMENT_CENTER);

            TextView textViewPlayerCount = getTextView(String.format(Locale.GERMAN, "%d/6", amountOfPLayer), View.TEXT_ALIGNMENT_TEXT_END);

            linearLayout.addView(textViewGameId);
            linearLayout.addView(textViewPlayerStatus);
            linearLayout.addView(textViewPlayerCount);

            scrollviewLayout.addView(linearLayout);
            this.gameFields.add(textViewGameId);
        });


    }

    private TextView getTextView(String text, int textAlignment){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                130, 1.0f));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(textAlignment);
        return textView;
    }

    private LinearLayout getLinearLayout(int gameId) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30,0,30,0);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setId(gameId);
        linearLayout.setOnClickListener(v -> gameSearchViewModel.connectToGame(v.getId()));

        if (gameId % 2 == 0)
            linearLayout.setBackgroundColor(Color.LTGRAY);
        return linearLayout;
    }

    @Override
    public void onConnectionEstablished(){
        Log.d("DEBUG", "GameSearch::onConnectionEstablished/ ");
        this.gameSearchViewModel.receiveGames();
    }


}