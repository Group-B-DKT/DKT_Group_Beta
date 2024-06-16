package com.example.dkt_group_beta.activities;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;

import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkt_group_beta.dialogues.CheatDialogFragment;
import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.model.enums.FieldType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import com.example.dkt_group_beta.activities.adapter.PlayerItemAdapter;
import com.example.dkt_group_beta.model.Field;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GameBoard extends AppCompatActivity implements SensorEventListener, GameBoardAction, CheatDialogFragment.OnInputListener{
    private static final String TAG = "DEBUG";
    private static final String DEF_TYPE = "drawable";
    private static final String FIELD_NAME = "field";
    private static final int NUMBER_OF_FIELDS = 30;
    private List<ImageView> imageViews;

    private GameBoardViewModel gameBoardViewModel;

    private boolean isPopupWindowOpen = false;

    SensorManager sensorManager;

    private static final float SHAKE_THRESHOLD = 15.0f; // Sensitivity -> how much the device moves

    private Button rollButton;

    private Button testButton;
    private Button btnEndTurn;

    private ViewGroup.LayoutParams endTurnLayout;

    private Player player;

    private boolean diceRolling = true;

    private ImageView diceImageView1;

    private ImageView diceImageView2;

    private int rollCounter = 0;

    private RecyclerView rvPlayerStats;

    private int[] diceResults;

    ImageView character;

    List<Player> players;
    List<Field> fields;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;

    private boolean passedStart;

    private PopupWindow popupReconnect;

    private boolean isCountdownThreadToCancel = false;


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


        initializeVariables();

        initializeFieldsImageViews();

        ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);
        initializeCharacterModels(constraintLayout);

        setPlayerAndFieldStatsOnLoaded(constraintLayout);

        Game game = new Game(players, fields);
        this.players = game.getPlayers();
        gameBoardViewModel = new GameBoardViewModel(this, game);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // initialising the Sensor
        createPlayerItems(game.getPlayers());

        testButton = findViewById(R.id.popUpCards);
        if (!player.isOnTurn()) {
            disableView(testButton);
            disableView(btnEndTurn);
        }
        initializeFieldImages();

        testButton.setOnClickListener(v -> dicePopUp());
        initializeEndTurnButton();
    }

    private void initializeFieldImages() {
        for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
            int resourceId = this.getResources().getIdentifier(FIELD_NAME + i, "id", this.getPackageName());
            ImageView imageView = findViewById(resourceId);
            if (imageView != null) {
                imageViews.add(imageView);
            }
        }
    }

    private void initializeEndTurnButton() {
        btnEndTurn.setOnClickListener(v -> {
            if (player.isOnTurn()){
                gameBoardViewModel.endTurn();
                disableView(testButton);
                diceRolling = false;
            }
        });
    }

    private void setPlayerAndFieldStatsOnLoaded(ConstraintLayout constraintLayout) {
        constraintLayout.addOnLayoutChangeListener((View v, int left, int top, int right, int bottom,
                                                    int oldLeft, int oldTop, int oldRight, int oldBottom)-> {
            for (Player p: players) {
                setPosition(p.getCurrentPosition(), p);

                ViewGroup.LayoutParams params = p.getCharacterView().getLayoutParams();
                int size = findViewById(R.id.field1).getWidth() / 2;
                params.width = size;
                params.height = size;
                p.getCharacterView().setLayoutParams(params);
            }

            for (Field f: fields) {
                if (f.getOwner() == null)
                    continue;

                markBoughtField(f.getId() - 1, f.getOwner().getColor());
            }
        });
    }

    private void initializeCharacterModels(ConstraintLayout constraintLayout) {
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
                    .getIdentifier("character" + (i+1), DEF_TYPE, this.getPackageName());

            if (resourceId != 0) {
                x.setImageBitmap(
                        decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            }
            x.setImageBitmap(decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
            players.get(i).setCharacterView(x);
        }
    }

    private void initializeVariables() {
        character = findViewById(R.id.character);
        btnEndTurn = findViewById(R.id.btn_endTurn);
        rvPlayerStats = findViewById(R.id.rv_playerStats);

        player = WebsocketClientController.getPlayer();

        diceResults = new int[2];
        endTurnLayout = new ViewGroup.LayoutParams(btnEndTurn.getLayoutParams());


        testButton.setOnClickListener(v -> dicePopUp());
        btnEndTurn.setOnClickListener(v -> {
            if (player.isOnTurn()){
                gameBoardViewModel.endTurn();
                disableView(testButton);
                diceRolling = false;
            }
        });
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (proximitySensor == null) {
            Toast.makeText(this, "Proximity sensor not available", Toast.LENGTH_SHORT).show();
        }

        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.values[0] < proximitySensor.getMaximumRange()) {
                    // Hand is near the display
                    Toast.makeText(getApplicationContext(), "Cheat Mode Activated", Toast.LENGTH_SHORT).show();
                    CheatDialogFragment dialog = new CheatDialogFragment();
                    dialog.inputListener = GameBoard.this;
                    dialog.show(getSupportFragmentManager(), "InputDialogFragment");
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // We can ignore this for now
            }
        };
        players = (List<Player>) getIntent().getSerializableExtra("players");
        players.removeIf(p -> p.getId().equals(player.getId()));
        players.add(player);
        fields = (List<Field>) getIntent().getSerializableExtra("fields");
        players.sort(Comparator.comparing(Player::getId));
    }

    private void initializeFieldsImageViews() {
        this.imageViews = new ArrayList<>();
        runOnUiThread(() -> {
            for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
                int resourceId = this.getResources()
                        .getIdentifier(FIELD_NAME + i, "id", this.getPackageName());
                imageViews.add(findViewById(resourceId));

                resourceId = this.getResources()
                        .getIdentifier(FIELD_NAME + i, DEF_TYPE, this.getPackageName());


                ImageView imageView = imageViews.get(i - 1);
                if (imageView != null && resourceId != 0) {
                    imageView.setImageBitmap(
                            decodeSampledBitmapFromResource(getResources(), resourceId, 200, 200));
                }
            }
        });
    }


    public void buyField(int index) {
            gameBoardViewModel.buyField(index);
    }

    public void markBoughtField(int index, int color){
        runOnUiThread(() -> {
            ImageView imageView = imageViews.get(index);
            if (imageView != null) {
                imageView.setPadding(10,10 ,10,10);
                imageView.setBackgroundColor(color);
            }
        });
    }

    public void markBoughtField(int index){
        markBoughtField(index, Color.rgb(255,70,0));
    }

    public void unMarkBoughtField(int index){
        runOnUiThread(() -> {
            ImageView imageView = imageViews.get(index);
            if (imageView != null) {
                imageView.setPadding(0,0 ,0,0);
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
    public void animation(Player movePlayer, int repetition) {


        ImageView characterImageView = movePlayer.getCharacterView();

        if (repetition == 0) {
            if (movePlayer.getId().equals(player.getId()))
                checkEndFieldPosition(passedStart);
            return;
        }

        Animation animation = getAnimation(movePlayer);
        animation.setDuration(500); // Dauer basierend auf Anzahl der Schritte
        animation.setRepeatCount(0); // Keine Wiederholung, da die Position manuell aktualisiert wird
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "Y3: " + characterImageView.getY());
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animation.cancel();
                movePlayer.setCurrentPosition(movePlayer.getCurrentPosition()+1);
                if (movePlayer.getCurrentPosition() >= NUMBER_OF_FIELDS) {
                    movePlayer.setCurrentPosition(0);
                    passedStart = true;
                }
                setPosition(movePlayer.getCurrentPosition(), movePlayer);
                animation(movePlayer, repetition - 1);

            }

            @Override
            public void onAnimationRepeat(Animation animation) { // method not used
            }
        });
        movePlayer.getCharacterView().startAnimation(animation);
    }

    private void checkEndFieldPosition(boolean passedStart) {
        Field field = fields.get(player.getCurrentPosition());
        if (field.getFieldType() != FieldType.ASSET &&
            field.getOwner() == null &&
            player.getMoney() >= field.getPrice()){

            showCard(findViewById(R.id.gameBoard), FIELD_NAME + (player.getCurrentPosition()+1));
        }
        if(passedStart) {
            gameBoardViewModel.passStartOrMoneyField();
        }

    }

    @Override
    public void disableEndTurnButton() {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = btnEndTurn.getLayoutParams();
            params.height = 1;
            params.width = 1;
            btnEndTurn.setLayoutParams(params);
        });
    }

    @Override
    public void enableEndTurnButton() {
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = btnEndTurn.getLayoutParams();
            params.height = endTurnLayout.height;
            params.width = endTurnLayout.width;
            btnEndTurn.setLayoutParams(params);
        });
    }

    @Override
    public void updatePlayerStats() {
        runOnUiThread(() -> rvPlayerStats.getAdapter().notifyDataSetChanged());
    }

    @Override
    public void enableDiceButton() {
        diceRolling = true;
        runOnUiThread(() -> {
            ViewGroup.LayoutParams params = testButton.getLayoutParams();
            params.height = endTurnLayout.height;
            params.width = endTurnLayout.width;
            testButton.setLayoutParams(params);
        });
    }

    public void dicePopUp() {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_popup_dice, null);

            diceImageView1 = popupView.findViewById(R.id.diceImageView1);
            diceImageView2 = popupView.findViewById(R.id.diceImageView2);

            int width = WRAP_CONTENT;
            int height = WRAP_CONTENT;
            boolean focusable = true;
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            popupWindow.setOnDismissListener(() -> isPopupWindowOpen = false);
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
            Log.d(TAG, "IsOnTurn: "+player.isOnTurn());

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
        int drawableResource = getResources().getIdentifier("dice" + diceResult, DEF_TYPE, getPackageName());
        imageView.setImageResource(drawableResource);
    }

    public void showBothDice(int[] diceResult){
        runOnUiThread(()->{
            int drawableResource = getResources().getIdentifier("dice" + diceResult[0], DEF_TYPE, getPackageName());
            diceImageView1.setImageResource(drawableResource);
            drawableResource = getResources().getIdentifier("dice" + diceResult[1], DEF_TYPE, getPackageName());
            diceImageView2.setImageResource(drawableResource);
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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

    private void showCard(View view, String viewID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_cards_layout, null);
        int width = WRAP_CONTENT;
        int height = WRAP_CONTENT;
        boolean focusable = true;

        PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        ImageView popupImageView = popupView.findViewById(R.id.popUpCards); // load specific image into pop-up that belongs to the field
        int imageResourceId = getResources().getIdentifier("card" + viewID, DEF_TYPE, getPackageName());
        popupImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageResourceId, 200, 200));

        Button btnBuy = popupView.findViewById(R.id.btn_buy);
        btnBuy.setOnClickListener(v -> {
            gameBoardViewModel.buyField(player.getCurrentPosition());
            popupWindow.dismiss();
        });
    }

    @Override
    public void showDisconnectPopUp(Player disconnectedPlayer, LocalTime serverTime){
        final int RECONNECT_DURATION = 1 * 60; // in seconds

        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_connection_lost, null);
            int width = MATCH_PARENT;
            int height = MATCH_PARENT;

            popupReconnect = new PopupWindow(popupView, width, height, true);
            popupReconnect.showAtLocation(findViewById(R.id.gameBoard), Gravity.CENTER, 0, 0);

            Button btnKickPlayer = popupView.findViewById(R.id.btn_kickPlayer);
            if (!this.player.isHost()) {
                disableView(btnKickPlayer);
            }
            btnKickPlayer.setOnClickListener(v ->
                this.players.forEach(p -> {
                    if (!p.isConnected()) {
                        gameBoardViewModel.removePlayer(player.getGameId(), p);
                    }
                }));

            TextView playerDisconnected = popupView.findViewById(R.id.txt_playerDisconnected);
            TextView remainingTime = popupView.findViewById(R.id.txt_remainingTime);

            playerDisconnected.setText(disconnectedPlayer.getUsername() + " " + getText(R.string.disconnected_player));
            remainingTime.setText(getText(R.string.remaining_time));

            int diff = LocalTime.now().toSecondOfDay() - serverTime.toSecondOfDay();
            countdown(RECONNECT_DURATION - diff, remainingTime);
        });

    }

    @Override
    public void removeReconnectPopUp() {
        runOnUiThread(() -> popupReconnect.dismiss());
        isCountdownThreadToCancel = true;
    }

    @Override
    public void removePlayerFromGame(Player fromPlayer) {
        runOnUiThread(() -> {
            ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);

            Player clientPlayer = this.players.stream().filter(p -> p.getId().equals(fromPlayer.getId())).findFirst().orElse(null);
            if (clientPlayer == null)
                return;

            constraintLayout.removeView(clientPlayer.getCharacterView());

            for (Field f: this.fields) {
                if (f.getOwner() == null || !f.getOwner().getId().equals(fromPlayer.getId())) {
                    continue;
                }
                f.setOwner(null);
                unMarkBoughtField(f.getId() - 1);
            }
            this.players.removeIf(p -> p.getId().equals(fromPlayer.getId()));
            this.rvPlayerStats.getAdapter().notifyDataSetChanged();
        });
    }

    @Override
    public void setPlayerDisconnected(Player disconnectedPlayer) {
        this.players.forEach(p -> {
            if (p.getId().equals(disconnectedPlayer.getId())){
                p.setConnected(false);
            }
        });
    }

    private void countdown(final int startTime, TextView remainingTime) {
        new Thread(() -> {
            long lastTime = System.currentTimeMillis();
            int time = startTime;
            while (time >= 0){
                if (System.currentTimeMillis() - lastTime >= 1000) {
                    int min = time / 60;
                    int sec = time % 60;
                    String timeStr = String.format("%02d:%02d", min, sec);
                    runOnUiThread(() -> remainingTime.setText(getText(R.string.remaining_time) + " " + timeStr));
                    time--;
                    lastTime = System.currentTimeMillis();
                }
            }
            if (!isCountdownThreadToCancel) {
                this.players.forEach(p -> {
                    if (!p.isConnected()) {
                        gameBoardViewModel.removePlayer(player.getGameId(), p);
                    }
                });
            }
            isCountdownThreadToCancel = false;
        }).start();
    }

    public void setPosition(int start, Player player) {

        int index = players.indexOf(player);
        int x = getPositionFromView(imageViews.get(start))[0];
        int y = getPositionFromView(imageViews.get(start))[1];
        ImageView mCharacter = player.getCharacterView();

        if(player.getCurrentPosition() <= 10 || (player.getCurrentPosition() >= 15 && player.getCurrentPosition() <= 25)){
            x += (index%2 == 0) ? 0 : mCharacter.getWidth();
            y += (index/2) * mCharacter.getHeight();
        } else{
            x += (index/2) * mCharacter.getWidth();
            y += (index%2 == 0) ? 0 : mCharacter.getHeight();
        }

        mCharacter.setX(x);
        mCharacter.setY(y);

    }



    private Animation getAnimation(Player movePlayer) {
        ImageView mCharacter = movePlayer.getCharacterView();
        int index = players.indexOf(movePlayer);
        int nextPosition = movePlayer.getCurrentPosition() + 1;
        if (nextPosition >= NUMBER_OF_FIELDS){
            nextPosition = 0;
        }

        float xDelta;
        float yDelta;
        int[] pos = getPositionFromView(imageViews.get(nextPosition));
        Animation animation = null;
        if(movePlayer.getCurrentPosition() <= 10 || (movePlayer.getCurrentPosition() >= 15 && movePlayer.getCurrentPosition() <= 25)){
            pos[0] += (index%2 == 0) ? 0 : mCharacter.getWidth();
            pos[1] += (index/2) * mCharacter.getHeight();
        } else{
            pos[0] += (index/2) * mCharacter.getWidth();
            pos[1] += (index%2 == 0) ? 0 : mCharacter.getHeight();
        }
        xDelta = pos[0] - mCharacter.getX();
        yDelta = pos[1] - mCharacter.getY();

        animation = new TranslateAnimation(0, xDelta, 0, yDelta);

        return animation;
    }
    @Override
    public void sendCheatValue(int input) {
        gameBoardViewModel.submitCheat(input);
    }

    public void gotToJail(Player playerInJail){

        int currentposition = playerInJail.getCurrentPosition();
        int jail = 24;

        if(currentposition < 24){
            animation(player, jail - currentposition);
        }else{ //current position > 24
            int jailFieldNumber = jail + (30 - currentposition); //goal: field 24, to reach this field he has to go till start + 24 fields
            animation(player, jailFieldNumber);
        }

        //call stay inJail

    }





}