package com.example.dkt_group_beta.activities;

import static java.lang.Thread.sleep;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameBoard extends AppCompatActivity {
    private static final int NUMBER_OF_FIELDS = 32;

    private static final int NUMBER_OF_FIGURES = 6;
    private List<ImageView> imageViews;

    private List<ImageView> figures;
    ImageView character;
    ImageView character2;

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
        character2 = findViewById(R.id.character2);

        this.imageViews = new ArrayList<>();
        runOnUiThread(() -> {
            for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
                int resourceId = this.getResources()
                        .getIdentifier("field" + i, "id", this.getPackageName());
                imageViews.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier("field" + i, "drawable", this.getPackageName());


                ImageView imageView = imageViews.get(i - 1);
                if (imageView != null && resourceId != 0) {
                    imageView.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });

        this.figures = new ArrayList<>();
        runOnUiThread(() ->{

            for (int i = 1; i <= NUMBER_OF_FIGURES; i++) {
                int resourceId = this.getResources()
                        .getIdentifier("character" + i, "id", this.getPackageName());
                figures.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier("character" + i, "drawable", this.getPackageName());


                ImageView imageView = figures.get(i - 1);
                if (imageView != null && resourceId != 0) {
                    imageView.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(() -> {
            try {
                Thread.sleep(50);
                setPosition(0, character);
                animation(character, 29);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

    }

    public int[] getPositionFromView(View view){
        int[] res = new int[2];
        view.getLocationOnScreen(res);
        res[0] -= character.getLayoutParams().width;
        return res;
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

    public void setPosition(int start, ImageView characterImageView) {
        characterImageView.setX(getPositionFromView(imageViews.get(start))[0]);
        characterImageView.setY(getPositionFromView(imageViews.get(start))[1]);
    }

    public void animation(ImageView characterImageView, int repetition) {
        if (repetition == 0){
            return;
        }

        Animation animation = null;
        if (currentplace < 10) {
            animation = new TranslateAnimation(0, -110, 0, 0);

        }
        else if (currentplace < 15) {
            animation = new TranslateAnimation(0, 0, 0, -50);
        }
        else if (currentplace < 25){
            animation = new TranslateAnimation(0, 110, 0, 0);
        }
        else {
            animation = new TranslateAnimation(0, 0, 0, 50);
        }
        animation.setDuration(800); // Dauer basierend auf Anzahl der Schritte
        animation.setRepeatCount(0); // Keine Wiederholung, da die Position manuell aktualisiert wird
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("DEBUG", "Y3: " + characterImageView.getY());
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                animation.cancel();
                currentplace++;
                characterImageView.setX(getPositionFromView(imageViews.get(currentplace))[0]);
                characterImageView.setY(getPositionFromView(imageViews.get(currentplace))[1]);
                animation(characterImageView, repetition -1 );
            }
            @Override
            public void onAnimationRepeat(Animation animation) { // method not used
            }
        });
        characterImageView.startAnimation(animation);
    }

}











