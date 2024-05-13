package com.example.dkt_group_beta.activities;

import android.content.res.ColorStateList;
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
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import com.example.dkt_group_beta.activities.adapter.PlayerItemAdapter;
import com.example.dkt_group_beta.model.Field;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoard extends AppCompatActivity implements SensorEventListener, GameBoardAction {
    private static final int NUMBER_OF_FIELDS = 32;
    private static final int NUMBER_OF_FIGURES = 6;
    private List<ImageView> imageViews;
    private boolean isPopupWindowOpen = false;
    SensorManager sensorManager;
    private static final float SHAKE_THRESHOLD = 15.0f; // Sensitivity -> how much the device moves
    private Button rollButton;
    private Button testButton;
    private Button btn_endTurn;

    private ViewGroup.LayoutParams endTurn_layout;

    private Player player;
    private boolean diceRolling = true;

    private ImageView diceImageView1;
    private ImageView diceImageView2;
    private int rollCounter = 0;
    private GameBoardViewModel gameBoardViewModel;
    private RecyclerView rvPlayerStats;

    private Game game;
    private int[] diceResults;

    private List<ImageView> figures;
    ImageView character;

    int currentplace = 0;

    List<Player> players;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ASD", "HEREEEE");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_board);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gameBoard), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        character = findViewById(R.id.character);
        btn_endTurn = findViewById(R.id.btn_endTurn);
        rvPlayerStats = findViewById(R.id.rv_playerStats);

        player = WebsocketClientController.getPlayer();

        diceResults = new int[2];
        endTurn_layout = new ViewGroup.LayoutParams(btn_endTurn.getLayoutParams());

        players = (List<Player>) getIntent().getSerializableExtra("players");
        players.removeIf(p -> p.getId().equals(player.getId()));
        players.add(player);
        List<Field> fields = (List<Field>) getIntent().getSerializableExtra("fields");

        players.sort(Comparator.comparing(Player::getId));


        initializeFieldsImageViews();

        ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);
        for (int i = 0; i < players.size(); i++) {

            final ImageView x;
            if(i == 0){
               x = character;
            }else{
                x = new ImageView(this);

                constraintLayout.addView(x);

                x.setLayoutParams(character.getLayoutParams());
            }
            ImageViewCompat.setImageTintList(x, ColorStateList.valueOf(players.get(i).getColor()));


            int resourceId = this.getResources()
                    .getIdentifier("character" + (i+1), "drawable", this.getPackageName());


            if (resourceId != 0) {
                x.setImageBitmap(
                        decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            }

            x.setImageBitmap(decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            players.get(i).setCharacterView(x);
        }

        constraintLayout.addOnLayoutChangeListener((View v, int left, int top, int right, int bottom,
                                                    int oldLeft, int oldTop, int oldRight, int oldBottom)-> {
            for (Player p: players) {
                setPosition(p.getCurrentPosition(), p);
            }
        });

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
        createPlayerItems(game.getPlayers());

        testButton = findViewById(R.id.popUpCards);
        Log.d("DEBUG", "IST: " + player.getUsername());
        if (!player.isOnTurn()) {
            disableView(testButton);
            disableView(btn_endTurn);
        }
        for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
            int resourceId = this.getResources().getIdentifier("field" + i, "id", this.getPackageName());
            ImageView imageView = findViewById(resourceId);
            if (imageView != null) {
                imageViews.add(imageView);
                String resourceName = getResources().getResourceEntryName(imageView.getId());
                // enable clicking on an image view and opening pop-up
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("ImageView", "ImageView ID: " + imageView.getId());
                        showCard(v, resourceName);
                    }
                });
            }
        }
        testButton.setOnClickListener(v -> dicePopUp());
        btn_endTurn.setOnClickListener(v -> {
            if (player.isOnTurn()){
                gameBoardViewModel.endTurn();
                disableView(testButton);
                diceRolling = false;
            }
        });
    }

    private void initializeFieldsImageViews() {
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
    }

    private void createPlayerItems(List<Player> players) {
        PlayerItemAdapter adapter = new PlayerItemAdapter(this, players);
        rvPlayerStats.setLayoutManager(new LinearLayoutManager(this));
        rvPlayerStats.setAdapter(adapter);
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
    public void animation(Player player, int repetition) {

        ImageView characterImageView = player.getCharacterView();

        if (repetition == 0) {
            return;
        }

        Animation animation = getAnimation(player);
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
                player.setCurrentPosition(player.getCurrentPosition()+1);
                if (player.getCurrentPosition() >= NUMBER_OF_FIELDS - 2)
                    player.setCurrentPosition(0);
                setPosition(player.getCurrentPosition(), player);
                animation(player, repetition - 1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { // method not used
            }
        });
        player.getCharacterView().startAnimation(animation);
    }

    @Override
    public void disableEndTurnButton() {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = btn_endTurn.getLayoutParams();
            params.height = 1;
            params.width = 1;
            btn_endTurn.setLayoutParams(params);
        });
    }

    @Override
    public void enableEndTurnButton() {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = btn_endTurn.getLayoutParams();
            params.height = endTurn_layout.height;
            params.width = endTurn_layout.width;
            btn_endTurn.setLayoutParams(params);
//            btn_endTurn.setLayoutParams(endTurn_layout);
        });
    }

    @Override
    public void updatePlayerStats(String playerId) {
        runOnUiThread(() -> {
            rvPlayerStats.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void enableDiceButton() {
        diceRolling = true;
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = testButton.getLayoutParams();
            params.height = endTurn_layout.height;
            params.width = endTurn_layout.width;
            testButton.setLayoutParams(params);
        });
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


            popupWindow.setOnDismissListener(() -> isPopupWindowOpen = false);
                // set isPopupWindowOpen to false if it is closed);
            isPopupWindowOpen = true;
            popupWindow.showAtLocation(imageViews.get(0), Gravity.CENTER, 0, 0);
            // initialising listener for the acceleration sensor
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);

            rollButton = popupView.findViewById(R.id.rollButton);
            rollButton.setOnClickListener(v -> {
                if (diceRolling){
                    diceRolling = false;
                    // Roll the dice and update the images
                    rollDiceAnimation(diceImageView1);
                    rollDiceAnimation(diceImageView2);
                    rollButton.setText("OK");
                } else {
                    popupWindow.dismiss();
                    gameBoardViewModel.movePlayer(diceResults[0] + diceResults[1]);
                    disableView(testButton);
                }

            });
            Log.d("DEBUG", "IsOnTurn: "+player.isOnTurn());

            if (!player.isOnTurn()) {
                disableView(rollButton);
            }
        });
    }

    private void disableView(View view){
        ViewGroup.LayoutParams param = view.getLayoutParams();
        param.height = 1;
        param.width = 1;
        view.setLayoutParams(param);
    }

    private void rollDiceAnimation(ImageView imageView){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(500);
        rotateAnimation.setInterpolator(new LinearInterpolator());

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { // not used
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                rollCounter++;
                rollDice(imageView);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { // not used

            }
        });
        imageView.startAnimation(rotateAnimation);
    }
    private void rollDice(ImageView imageView) {
        int diceResult = gameBoardViewModel.getRandomNumber(1,6);

        diceResults[rollCounter-1] = diceResult;
        Log.d("Debug",rollCounter + ", " +diceResult);
        showDice(diceResult, imageView);

        if(rollCounter==2){
            gameBoardViewModel.rollDice(diceResults);
            rollCounter = 0;
        }
    }

    private void showDice(int diceResult, ImageView imageView){
        int drawableResource = getResources().getIdentifier("dice" + diceResult, "drawable", getPackageName());
        imageView.setImageResource(drawableResource);
    }

    public void showBothDice(int[] diceResult){
        runOnUiThread(()->{
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
        if (isPopupWindowOpen && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) { // not used
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

    public void my_button_onClick_working(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_pop_up, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private void showCard(View view, String viewID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_cards_layout, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView popupImageView = popupView.findViewById(R.id.popUpCards); // load specific image into pop-up that belongs to the field
        int imageResourceId = getResources().getIdentifier("card" + viewID, "drawable", getPackageName());
        popupImageView.setImageResource(imageResourceId);
    }

    public void setPosition(int start, Player player) {

        int index = players.indexOf(player);
        int x = getPositionFromView(imageViews.get(start))[0];
        int y = getPositionFromView(imageViews.get(start))[1];
        ImageView character = player.getCharacterView();

        if(player.getCurrentPosition() <= 10 || (player.getCurrentPosition() >= 15 && player.getCurrentPosition() <= 25)){
            x += (index%2 == 0) ? 0 : character.getWidth();
            y += (index/2) * character.getHeight();
        } else{
            x += (index/2) * character.getWidth();
            y += (index%2 == 0) ? 0 : character.getHeight();
        }

        character.setX(x);
        character.setY(y);

    }



    private Animation getAnimation(Player player) {
        float xDelta = 110;
        float yDelta = 70;

        Animation animation;
        if (player.getCurrentPosition() <= 10) {
            animation = new TranslateAnimation(0, xDelta * -1, 0, 0);
        }
        else if (player.getCurrentPosition() < 15) {
            animation = new TranslateAnimation(0, 0, 0, yDelta * -1);
        }
        else if (player.getCurrentPosition() < 25){
            animation = new TranslateAnimation(0, xDelta, 0, 0);
        }
        else {
            animation = new TranslateAnimation(0, 0, 0, yDelta);
        }
        return animation;
    }
}