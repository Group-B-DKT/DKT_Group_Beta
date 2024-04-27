package com.example.dkt_group_beta.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
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

public class GameBoard extends AppCompatActivity {
    private static final int NUMBER_OF_FIELDS = 32;
    private List<ImageView> imageViews;

    private float startPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setStartPosition();

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


        Log.d("FIELD1", String.valueOf(findViewById(R.id.field10).getX()));






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


    public void setStartPosition(){


        ImageView field1 = findViewById(R.id.field1);
        field1.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float x = field1.getX();
                float y = field1.getY();
                Log.d("Position", "X: " + x + ", Y: " + y);
                // Entfernen des Listeners, um Memory-Leaks zu vermeiden
                field1.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                ImageView characterImageView = findViewById(R.id.character);

                characterImageView.setX(x);
                characterImageView.setY(y);


                //change to the dice number later
                int repetition = 2;
                animation(characterImageView, repetition);
            }
        });






    }

    public void animation(ImageView characterImageView, int repetition){



        Animation animation = AnimationUtils.loadAnimation(characterImageView.getContext(), R.anim.animator);
        animation.setRepeatCount(repetition);
        characterImageView.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                float endPosition = characterImageView.getX() + 100*repetition; // Hier musst du die Endposition berechnen
                characterImageView.setX(endPosition);
                characterImageView.setX(endPosition);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }












}