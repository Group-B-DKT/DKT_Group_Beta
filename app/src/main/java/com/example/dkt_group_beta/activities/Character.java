package com.example.dkt_group_beta.activities;

import static com.example.dkt_group_beta.activities.GameBoard.decodeSampledBitmapFromResource;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;

import java.util.ArrayList;
import java.util.List;

public class Character extends AppCompatActivity {
    public List<ImageView> characterViews;

    private static final int NUMBER_OF_CHARACTERS = 6;


    private float startPosition;
    private ImageView imageView;

    public Character() {
        // Leerer Konstruktor ohne Argumente
    }

    public Character(ImageView imageView) {

        this.imageView = imageView;

    }

   /* public void setStartPosition() {

        ImageView field1 = findViewById(R.id.field1);
        float field1Pos = field1.getX();
        if(imageView != null){
            this.startPosition = field1Pos;
            imageView.setX(field1Pos);
        }
    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_character);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        characterViews =new ArrayList<>();

        runOnUiThread(() ->

        {
            for (int i = 1; i <= NUMBER_OF_CHARACTERS; i++) {
                int resourceId = this.getResources()
                        .getIdentifier("character" + i, "id", this.getPackageName());
                characterViews.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier("character" + i, "drawable", this.getPackageName());


                ImageView imageView = characterViews.get(i - 1);
                if (imageView != null && resourceId != 0) {
                    imageView.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });



    }






}