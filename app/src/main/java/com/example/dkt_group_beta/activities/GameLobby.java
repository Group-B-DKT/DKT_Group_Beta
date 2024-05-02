package com.example.dkt_group_beta.activities;

import android.content.Context;
import android.content.Intent;
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
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.viewmodel.GameLobbyViewModel;
import com.example.dkt_group_beta.viewmodel.GameSearchViewModel;

import org.w3c.dom.Text;

import java.io.Serializable;
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

        String username = getIntent().getStringExtra("username");

        isHost = WebsocketClientController.getPlayer().isHost();


        this.gameLobbyViewModel = new GameLobbyViewModel(this);
        this.playerFields = new ArrayList<>();

        this.scrollviewLayout = findViewById(R.id.scrollview_gameLobby_layout);
        this.btnLeave = findViewById(R.id.btn_leave);
        this.btnLeave.setOnClickListener(v -> {
            gameLobbyViewModel.leaveGame();

        });



        this.btnReady = findViewById(R.id.btn_setReady);
        this.btnReady.setOnClickListener((v) -> gameLobbyViewModel.setReady());

        this.layoutButtons = findViewById(R.id.layout_gameLobby_btn);
        if (isHost) addStartButton();

        gameLobbyViewModel.getConnectedPlayerNames();


    }


    public void addStartButton(){
//        int layout = androidx.constraintlayout.widget.R.attr.buttonBarButtonStyle;
        runOnUiThread(()->{
            btnStart = new Button(this);
            btnStart.setBackgroundTintList(this.btnReady.getBackgroundTintList());
            btnStart.setText(getString(R.string.btn_startGame));
            btnStart.setLayoutParams(this.btnReady.getLayoutParams());
            btnStart.setTextColor(Color.GREEN);
            btnStart.setOnClickListener((v) -> gameLobbyViewModel.startGame());
            ViewCompat.setBackgroundTintList(
                    layoutButtons,
                    ColorStateList.valueOf(Color.GREEN));
            layoutButtons.addView(btnStart);
        });

    }
    @Override
    public void removePlayerFromView(Player player) {
        runOnUiThread(() -> {
            for (int i = 0; i < playerFields.size(); i++) {
                LinearLayout playerField = playerFields.get(i);
                TextView textView = (TextView) playerField.getChildAt(0);
                String playerName = textView.getText().toString().trim();
                if (playerName.endsWith(" (HOST)")) {

                    playerName = playerName.substring(0, playerName.length() - 7).trim();
                }

                if (playerName.equals(player.getUsername().trim())) {
                    scrollviewLayout.removeView(playerField);
                    playerFields.remove(playerField);
                    break;
                }
            }
        });
    }

    @Override
    public void switchToGameLobby(Player player) {
        String currentUsername = getIntent().getStringExtra("username");
        Intent intent = new Intent(GameLobby.this, GameSearch.class);
        intent.putExtra("username", currentUsername);
        startActivity(intent);

    }


    private LinearLayout getLinearLayout(int id) {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setPadding(30,0,30,0);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setId(id);

        if (id % 2 == 0)
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
     public void assertInputDialog(String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage(text);

        builder.setPositiveButton(getString(R.string.create_game_btn_create), (dialog, which) -> {
            dialog.cancel();
        });

        builder.show();
    }


    @Override
    public void addPlayerToView(Player player) {
        runOnUiThread(() -> {
            LinearLayout linearLayout = getLinearLayout(id++);

            String name = player.getUsername();

            String isReady = player.isReady() ? getString(R.string.btn_is_ready) : getString(R.string.btn_is_not_ready);
            TextView textViewIsReady = getTextView(isReady, View.TEXT_ALIGNMENT_TEXT_END);

            if (player.isHost()) {
                name += " (HOST)";
                textViewIsReady.setText("");
            }
            TextView textViewGameId = getTextView(name, View.TEXT_ALIGNMENT_TEXT_START);
            linearLayout.addView(textViewGameId);
            linearLayout.addView(textViewIsReady);

            scrollviewLayout.addView(linearLayout);
            this.playerFields.add(linearLayout);
        });
    }

    @Override
    public void readyStateChanged(String username, boolean isReady) {
        Log.d("DEBUG", "GameLobby::readyStateChanged/ " + username + " " + isReady);
        playerFields.forEach(pf -> {
            TextView childAt = (TextView) pf.getChildAt(0);
            if (childAt.getText().toString().split(" ")[0].equals(username)){
                TextView childIsReady = (TextView) pf.getChildAt(1);
                String isReadyTxt = isReady ? getString(R.string.btn_is_ready) : getString(R.string.btn_is_not_ready);
                childIsReady.setText(isReadyTxt);
            }
        });
    }

    @Override
    public void switchToGameBoard(List<Player> connectedPlayers, List<Field> fields){
        Intent intent = new Intent(this, GameBoard.class);
        intent.putExtra("players", (Serializable) connectedPlayers);
        intent.putExtra("fields", (Serializable) fields);
        startActivity(intent);
    }


    @Override
    public Context getContext() {
        return this.getApplicationContext();
    }

    public void changeReadyBtnText(boolean isReady) {
        String isReadyTxt = isReady ? getString(R.string.btn_is_not_ready) : getString(R.string.btn_is_ready);
        this.btnReady.setText(isReadyTxt);
    }
}