package com.example.dkt_group_beta.activities;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import android.util.Log;
import android.view.View;

import android.view.animation.Animation;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameBoard extends AppCompatActivity implements SensorEventListener, GameBoardAction {
    private static final int NUMBER_OF_FIELDS = 32;

    private static final int NUMBER_OF_FIGURES = 6;
    private List<ImageView> imageViews;
    private boolean isPopupWindowOpen = false;
    SensorManager sensorManager;
    private static final float SHAKE_THRESHOLD = 15.0f; // Sensitivity -> how much the device moves
    private Button rollButton;
    private Button testButton;

    private Player player;
    private boolean diceRolling = true;

    private ImageView diceImageView1;
    private ImageView diceImageView2;
    private int rollCounter = 0;
    private GameBoardViewModel gameBoardViewModel;
    private Game game;
    private int[] diceResults;

    private List<ImageView> figures;
    ImageView character;

    int currentplace = 0;

    List<Player> players;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameBoard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        character = findViewById(R.id.character);

        player = WebsocketClientController.getPlayer();

        diceResults = new int[2];

        players = (List<Player>) getIntent().getSerializableExtra("players");
        List<Field> fields = (List<Field>) getIntent().getSerializableExtra("fields");

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




        for (int i = 0; i < players.size(); i++) {
            ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);
            final ImageView x;
            if(i == 0){
               x = character;
            }else{
                x = new ImageView(this);

                constraintLayout.addView(x);

                x.setLayoutParams(character.getLayoutParams());
            }




            int resourceId = this.getResources()
                    .getIdentifier("character" + (i+1), "drawable", this.getPackageName());


            if (resourceId != 0) {
                x.setImageBitmap(
                        decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            }

            x.setImageBitmap(decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            players.get(i).setCharacterView(x);

            constraintLayout.addOnLayoutChangeListener((View v, int left, int top, int right, int bottom,
                                                        int oldLeft, int oldTop, int oldRight, int oldBottom)-> {
                setPosition(0, x);
            });


        }

        game = new Game(players, fields);
        gameBoardViewModel = new GameBoardViewModel(this, game);



        this.figures = new ArrayList<>();
        runOnUiThread(() -> {

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
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // initialising the Sensor
    }

    @Override
    protected void onStart() {

        super.onStart();
        new Thread(() -> {
//            try {
//                Thread.sleep(5000);
//
//                for (Player p: players) {
//                    setPosition(0, p.getCharacterView());
//                }
//               animation(players.get(0).getCharacterView(), 100);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
        }).start();

        testButton = findViewById(R.id.popUpCards);
        Log.d("DEBUG", "IST: " + player.getUsername());
        if (!player.isOnTurn())
            disableView(testButton);

        testButton.setOnClickListener(v -> dicePopUp());
    }

    public int[] getPositionFromView(View view) {
        int[] res = new int[2];
        view.getLocationOnScreen(res);
//        res[0] -= character.getLayoutParams().width;
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


    @Override
    public void animation(ImageView characterImageView, int repetition) {
        if (repetition == 0) {
            return;
        }

        Animation animation = getAnimation();
        animation.setDuration(500); // Dauer basierend auf Anzahl der Schritte
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
                if (currentplace >= NUMBER_OF_FIELDS - 2)
                    currentplace = 0;
                characterImageView.setX(getPositionFromView(imageViews.get(currentplace))[0]);
                characterImageView.setY(getPositionFromView(imageViews.get(currentplace))[1]);
                animation(characterImageView, repetition - 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { // method not used
            }
        });
        characterImageView.startAnimation(animation);
    }


    public void dicePopUp() {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_popup_dice, null);

            diceImageView1 = popupView.findViewById(R.id.diceImageView1);
            diceImageView2 = popupView.findViewById(R.id.diceImageView2);

            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            popupWindow.setOnDismissListener(() -> {
                isPopupWindowOpen = false;
                // set isPopupWindowOpen to false if it is closed
            });
            isPopupWindowOpen = true;
            popupWindow.showAtLocation(imageViews.get(0), Gravity.CENTER, 0, 0);
            // initialising listener for the acceleration sensor
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);

            rollButton = popupView.findViewById(R.id.rollButton);
            rollButton.setOnClickListener(v -> {
                if (diceRolling) {
                    diceRolling = false;
                    // Roll the dice and update the images
                    rollDiceAnimation(diceImageView1);
                    rollDiceAnimation(diceImageView2);
                    rollButton.setText("OK");
                } else {
                    popupWindow.dismiss();
                    gameBoardViewModel.movePlayer(diceResults[0] + diceResults[1]);
                }

            });
            Log.d("DEBUG", "IsOnTurn: " + player.isOnTurn());

            if (!player.isOnTurn()) {
                disableView(rollButton);
            }
        });
    }

    private void disableView(View view) {
        ViewGroup.LayoutParams param = view.getLayoutParams();
        param.height = 1;
        param.width = 1;
        view.setLayoutParams(param);
    }

    private void rollDiceAnimation(ImageView imageView) {
        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                rollCounter++;
                rollDice(imageView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(rotateAnimation);
    }

    private void rollDice(ImageView imageView) {
        int diceResult = gameBoardViewModel.getRandomNumber(1, 6);

        diceResults[rollCounter - 1] = diceResult;
        Log.d("Debug", rollCounter + ", " + diceResult);
        showDice(diceResult, imageView);

        if (rollCounter == 2) {
            gameBoardViewModel.rollDice(diceResults);
            rollCounter = 0;
        }
    }

    private void showDice(int diceResult, ImageView imageView) {
        int drawableResource = getResources().getIdentifier("dice" + diceResult, "drawable", getPackageName());
        imageView.setImageResource(drawableResource);
    }

    public void showBothDice(int[] diceResult) {
        runOnUiThread(() -> {
            int drawableResource = getResources().getIdentifier("dice" + diceResult[0], "drawable", getPackageName());
            diceImageView1.setImageResource(drawableResource);
            drawableResource = getResources().getIdentifier("dice" + diceResult[1], "drawable", getPackageName());
            diceImageView2.setImageResource(drawableResource);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!player.isOnTurn())
            return;
        // only of the pop-up window is open, it is possible to shake the phone to roll the dice
        if (isPopupWindowOpen == true && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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



    public void setPosition(int start, ImageView characterImageView) {
        characterImageView.setX(getPositionFromView(imageViews.get(start))[0]);
        characterImageView.setY(getPositionFromView(imageViews.get(start))[1]);
    }



    private Animation getAnimation() {
        float xDelta = 110;
        float yDelta = 70;

        Animation animation = null;
        if (currentplace < 10) {
            animation = new TranslateAnimation(0, xDelta * -1, 0, 0);

        }
        else if (currentplace < 15) {
            animation = new TranslateAnimation(0, 0, 0, yDelta * -1);
        }
        else if (currentplace < 25){
            animation = new TranslateAnimation(0, xDelta, 0, 0);
        }
        else {
            animation = new TranslateAnimation(0, 0, 0, yDelta);
        }
        return animation;
    }

}











