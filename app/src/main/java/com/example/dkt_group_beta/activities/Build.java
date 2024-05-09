package com.example.dkt_group_beta.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.House;
import com.example.dkt_group_beta.model.Player;

import java.util.ArrayList;
import java.util.List;

public class Build extends AppCompatActivity {
    private Player currentPlayer;
    private Game game;
    private List<ImageView> imageViews;
    private TextView buildTextView;
    private Button buildButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        buildTextView = findViewById(R.id.build_text_view);
        buildButton = findViewById(R.id.build_button);
        imageViews = new ArrayList<>();
        for (int i = 1; i <= 32; i++) {
            int resourceId = getResources().getIdentifier("field" + i, "id", getPackageName());
            imageViews.add(findViewById(resourceId));
        }

        buildButton.setOnClickListener(v -> {
            buildTextView.setText("Um zu BAUEN klicken Sie auf die hervorgehobenen Felder");
            highlightOwnedFields();
        });
    }

    private void highlightOwnedFields() {
        List<Field> ownedFields = game.getOwnedFields(currentPlayer);
        for (ImageView imageView : imageViews) {
            Field field = ownedFields.get(imageViews.indexOf(imageView));
            if (ownedFields.contains(field)) {
                imageView.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    public void updateGameBoard(House house) {
        Field field = house.getField();
        int fieldId = field.getId();
        ImageView imageView = imageViews.get(fieldId - 1);
        imageView.setImageResource(R.drawable.house);
    }
}
