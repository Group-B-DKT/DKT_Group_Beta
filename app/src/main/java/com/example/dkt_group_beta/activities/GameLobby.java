package com.example.dkt_group_beta.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
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
import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.activities.interfaces.GameSearchAction;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.viewmodel.GameLobbyViewModel;
import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameLobby extends AppCompatActivity implements GameLobbyAction {
    private GameLobbyViewModel gameLobbyViewModel;
    private LinearLayout scrollviewLayout;
    private LinearLayout layoutButtons;
    private List<LinearLayout> playerFields;
    private Button btnLeave;
    private Button btnReady;
    private Button btnStart;
    private boolean isHost;
    private static int id = 1;
    private boolean firstInList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_lobby);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.gameLobbyViewModel = new GameLobbyViewModel(this);
        this.playerFields = new ArrayList<>();

        this.scrollviewLayout = findViewById(R.id.scrollview_gameLobby_layout);
        this.btnLeave = findViewById(R.id.btn_leave);
        this.btnReady = findViewById(R.id.btn_setReady);

        this.layoutButtons = findViewById(R.id.layout_gameLobby_btn);

        isHost = getIntent().getBooleanExtra("isHost", false);
        if (isHost) addStartButton();

        gameLobbyViewModel.getConnectedPlayerNames();
    }

    private void addStartButton(){
//        int layout = androidx.constraintlayout.widget.R.attr.buttonBarButtonStyle;
        btnStart = new Button(this);
        btnStart.setBackgroundTintList(this.btnReady.getBackgroundTintList());
        btnStart.setText(getString(R.string.btn_startGame));
        btnStart.setLayoutParams(this.btnReady.getLayoutParams());
        btnStart.setTextColor(Color.GREEN);
        ViewCompat.setBackgroundTintList(
                layoutButtons,
                ColorStateList.valueOf(Color.GREEN));
        layoutButtons.addView(btnStart);
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

        if (gameId % 2 == 0)
            linearLayout.setBackgroundColor(Color.LTGRAY);

        return linearLayout;
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


    @Override
    public void addPlayerToView(Player player) {
        runOnUiThread(() -> {
            LinearLayout linearLayout = getLinearLayout(id++);

            String name = player.getUsername();
            if (firstInList){
                name += " (HOST)";
                firstInList = false;
            }
            TextView textViewGameId = getTextView(name, View.TEXT_ALIGNMENT_TEXT_START);

            linearLayout.addView(textViewGameId);
            String s = "ASd ".split(" ")[0];
            scrollviewLayout.addView(linearLayout);
            this.playerFields.add(linearLayout);
        });
    }
}