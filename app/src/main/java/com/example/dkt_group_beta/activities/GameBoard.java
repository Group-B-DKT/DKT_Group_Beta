package com.example.dkt_group_beta.activities;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard extends AppCompatActivity implements SensorEventListener {
    private static final int NUMBER_OF_FIELDS = 32;
    private List<ImageView> imageViews;
    private boolean isPopupWindowOpen = false;
    SensorManager sensorManager;
    private static final float SHAKE_THRESHOLD = 15.0f; // Sensitivity -> how much the device moves
    private Button rollButton;

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
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // initialising the Sensor
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

    public void dicePopUp(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup_dice, null);

        ImageView diceImageView1 = popupView.findViewById(R.id.diceImageView1);
        ImageView diceImageView2 = popupView.findViewById(R.id.diceImageView2);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        popupWindow.setOnDismissListener(() -> {
            isPopupWindowOpen = false;
            // set isPopupWindowOpen to false if it is closed
        });
        isPopupWindowOpen = true;
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        // initialising listener for the acceleration sensor
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        rollButton = popupView.findViewById(R.id.rollButton);
        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Roll the dice and update the images
                rollDiceAnimation(diceImageView1);
                rollDiceAnimation(diceImageView2);
            }
        });
    }
    private void rollDiceAnimation(ImageView imageView){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                rollDice(imageView);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(rotateAnimation);
    }
    private void rollDice(ImageView imageView) {
        Random random = new Random();
        int diceResult = random.nextInt(6) + 1; // Random number between 1 and 6
        int drawableResource = getResources().getIdentifier("dice" + diceResult, "drawable", getPackageName());
        imageView.setImageResource(drawableResource);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // only of the pop-up window is open, it is possible to shake the phone to roll the dice
        if (isPopupWindowOpen==true && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // acceleration along x-, y- and z-axis
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Calculate the overall acceleration if the phone is shaken
            double acceleration = Math.sqrt(x * x + y * y + z * z);

            // Check if acceleration exceeds the threshold -> means there is a shaking motion
            if (acceleration > SHAKE_THRESHOLD) {
                // if the shaking motion is detected -> the rollButton is clicked by itself
                rollButton.performClick();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}