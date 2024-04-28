package com.example.dkt_group_beta.activities;

import static android.os.SystemClock.sleep;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameBoard extends AppCompatActivity {
    private static final int NUMBER_OF_FIELDS = 32;
    private List<ImageView> imageViews;

    HashMap<Integer, Float> XKoordinates = new HashMap();
    HashMap<Integer, Float> YKoordinates = new HashMap();

    ImageView character;

    int currentplace = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameboard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        character = findViewById(R.id.character);

        this.imageViews = new ArrayList<>();
        runOnUiThread(() -> {
            for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
                int resourceId = this.getResources()
                        .getIdentifier("field" + i, "id", this.getPackageName());
                imageViews.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier("field" + i, "drawable", this.getPackageName());


                ImageView imageView = imageViews.get(i-1);
                if (imageView != null && resourceId != 0) {
                    imageView.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });


        getPositions(); //Positionen der Felder speichern
        setStartPosition();





        int repetition = 1;
        animation(character, repetition);




        int repetition2 = 3;
        animation(character, repetition2);









    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }



    public void getPositions(){

        ConstraintLayout constraintLayout = findViewById(R.id.gameboard);
        constraintLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                // Hier wird der Code ausgeführt, wenn das Layout vollständig erstellt wurde


                for(int i = 1; i <= 30; i++){

                    String fieldName = "field" + i; // Erstelle den dynamischen ID-Namen
                    int resourceId = getResources().getIdentifier(fieldName, "id", getPackageName());

                    ImageView imageView = findViewById(resourceId);

                    XKoordinates.put(i, imageView.getX());
                    YKoordinates.put(i, imageView.getY());
                    Log.d("ABC", String.valueOf(imageView.getX()));

                }

                // Entfernen des Listeners, um Memory-Leaks zu vermeiden
                constraintLayout.removeOnLayoutChangeListener(this);
            }
        });




    }


    public void setStartPosition(){


        ImageView field1 = findViewById(R.id.field1);
        field1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                float x = XKoordinates.get(1);
                character.setX(XKoordinates.get(1));
                character.setY(YKoordinates.get(1));
                currentplace = 1;

                Log.d("STARTPOSTION", String.valueOf(x));
                // Entfernen des Listeners, um Memory-Leaks zu vermeiden
                field1.getViewTreeObserver().removeOnGlobalLayoutListener(this);


            }
        });








    }



    public void setPosition(ImageView characterView, int field){

        character.setX(XKoordinates.get(field));
        character.setY(YKoordinates.get(field));
    }

    public void animation(ImageView characterImageView, int repetition){


        final float[] endeX = {0.0F};
        final float[] endeY = {0.0F};


        Animation animation = AnimationUtils.loadAnimation(characterImageView.getContext(), R.anim.animator);
        animation.setRepeatCount(repetition);
        characterImageView.startAnimation(animation);


        animation.setAnimationListener(new Animation.AnimationListener() {
            int currentRepetition = 0;
            float startX, startY;

            @Override
            public void onAnimationStart(Animation animation) {
                // Animation startet
                startX = characterImageView.getX();
                startY = characterImageView.getY();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Aktuelle Position nach der Animation erhalten
               float endX = characterImageView.getX();
               float endY = characterImageView.getY();
                currentplace += repetition;

                setPosition(characterImageView, currentplace + repetition);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                currentRepetition++;
            }
        });





    }












}