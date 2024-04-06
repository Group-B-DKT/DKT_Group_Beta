package com.example.dkt_group_beta.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
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
    private static final int MAX_PLAYER = 6;
    private LinearLayout scrollviewLayout;
    private GameSearchViewModel gameSearchViewModel;
    private List<LinearLayout> gameFields;
    private Button btnRefresh;
    private Button btnConnect;
    private Button btnCreateNew;

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
        this.btnRefresh = findViewById(R.id.btn_refresh);
        this.btnRefresh.setOnClickListener(v -> gameSearchViewModel.receiveGames());
        this.gameFields = new ArrayList<>();

        this.btnCreateNew = findViewById(R.id.btn_createNew);
        this.btnCreateNew.setOnClickListener(this::assertInputDialog);
    }

    @Override
    public void refreshGameListItems() {
        runOnUiThread(() -> {
            this.gameFields.forEach(layout -> {
                Log.d("DEBUG", "GameSearch::refreshGameList/ " + layout.getId());
                this.scrollviewLayout.removeView(layout);
            });
            this.gameFields.clear();
        });
    }

    @Override
    public void addGameToScrollView(int gameId, String gameName, int amountOfPLayer){
        Log.d("DEBUG", "GameSearch::addGameToScrollView/ " + gameId + ", " + amountOfPLayer);
        runOnUiThread(() -> {
            LinearLayout linearLayout = getLinearLayout(gameId, amountOfPLayer);

            TextView textViewGameId = getTextView(gameName, View.TEXT_ALIGNMENT_TEXT_START, amountOfPLayer);

            String status = String.format("%s", amountOfPLayer == MAX_PLAYER ? "Starting..." : "Waiting...");
            TextView textViewPlayerStatus = getTextView(status, View.TEXT_ALIGNMENT_CENTER, amountOfPLayer);

            TextView textViewPlayerCount = getTextView(String.format(Locale.GERMAN, "%d/6", amountOfPLayer), View.TEXT_ALIGNMENT_TEXT_END, amountOfPLayer);

            linearLayout.addView(textViewGameId);
            linearLayout.addView(textViewPlayerStatus);
            linearLayout.addView(textViewPlayerCount);

            scrollviewLayout.addView(linearLayout);
            this.gameFields.add(linearLayout);
        });

    }

    private TextView getTextView(String text, int textAlignment,  int amountOfPLayer){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                130, 1.0f));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(textAlignment);
        if (amountOfPLayer == MAX_PLAYER){
            textView.setTextColor(Color.RED);
        }
        return textView;
    }

    private LinearLayout getLinearLayout(int gameId, int amountOfPLayer) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30,0,30,0);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setId(gameId);

        if (gameId % 2 == 0)
            linearLayout.setBackgroundColor(Color.LTGRAY);

        if (amountOfPLayer == MAX_PLAYER){
            linearLayout.setBackgroundColor(getColor(R.color.light_gray));
        }else {
            linearLayout.setOnClickListener(v -> gameSearchViewModel.connectToGame(v.getId()));
        }
        return linearLayout;
    }

    private void assertInputDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.create_game_input_dialog));

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.create_game_btn_create), (dialog, which) -> {
            String inputText = input.getText().toString();
            if (inputText.isEmpty())
                return;

            this.gameSearchViewModel.createGame(inputText);
        });
        builder.setNegativeButton(getString(R.string.create_game_btn_cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onConnectionEstablished(){
        Log.d("DEBUG", "GameSearch::onConnectionEstablished/ ");
        this.gameSearchViewModel.receiveGames();
    }


}