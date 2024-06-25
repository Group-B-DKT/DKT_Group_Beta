package com.example.dkt_group_beta.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Intent;
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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dkt_group_beta.dialogues.CheatDialogFragment;
import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.activities.adapter.PlayerItemAdapter;
import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.activities.interfaces.PopupBtnAction;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.io.CardCSVReader;
import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.dialogues.CheatDialogFragment;
import com.example.dkt_group_beta.dialogues.ReportCheaterDialog;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Building;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.JokerCard;
import com.example.dkt_group_beta.model.MoveCard;
import com.example.dkt_group_beta.model.Hotel;
import com.example.dkt_group_beta.model.House;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.model.enums.FieldType;
import com.example.dkt_group_beta.model.interfaces.TimerElapsedEvent;
import com.example.dkt_group_beta.model.utilities.ThreadTimer;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import com.example.dkt_group_beta.activities.adapter.PlayerItemAdapter;
import com.example.dkt_group_beta.model.Field;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    List<Card> cards;
    List<Card> risikoCards;
    List<Card> bankCards;
    private PopupWindow popupReconnect;

    private boolean isCountdownThreadToCancel = false;
    Button build;

    private boolean doAnimation = true;


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

        testButton = findViewById(R.id.popUpCards);
        build = findViewById(R.id.build_button);

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

        if (!player.isOnTurn()) {
            disableView(testButton);
            disableView(btnEndTurn);
        }
        initializeFieldImages();

        testButton.setOnClickListener(v -> dicePopUp());
        build.setOnClickListener(v -> {
            if (player.isOnTurn()) {
                buildPopUp(player);
                updatePlayerStats();
            }
        });
        initializeEndTurnButton();

        risikoCards = CardCSVReader.readCards(getApplicationContext(), "risiko.csv");
        bankCards = CardCSVReader.readCards(getApplicationContext(), "bank.csv");
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


        //testButton.setOnClickListener(v -> dicePopUp());
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
            ThreadTimer threadTimer = null;
            CheatDialogFragment dialog;
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (this.threadTimer != null){
                    this.threadTimer.discard();
                }
                if (event.values[0] == 0) {
                    threadTimer = new ThreadTimer(5000, new TimerElapsedEvent() {
                        @Override
                        public void onTimerElapsed() {
                            runOnUiThread(() -> {
                                dialog = new CheatDialogFragment();
                                dialog.inputListener = GameBoard.this;
                                if (dialog == null || !dialog.isVisible()) {
                                    Toast.makeText(GameBoard.this.getApplicationContext(), "Cheat Mode Activated", Toast.LENGTH_SHORT).show();
                                    dialog.show(GameBoard.this.getSupportFragmentManager(), "InputDialogFragment");
                                }
                            });
                        }

                        @Override
                        public void onSecondElapsed(int secondsRemaining) {

                        }
                    });
                    this.threadTimer.start();
//                    Toast.makeText(getApplicationContext(), "Cheat Mode Activated", Toast.LENGTH_SHORT).show();
//                    CheatDialogFragment dialog = new CheatDialogFragment();
//                    dialog.inputListener = GameBoard.this;
//                    dialog.show(getSupportFragmentManager(), "InputDialogFragment");

                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // We can ignore this for now
            }
        };
        sensorManager.registerListener(proximitySensorListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);

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
        if (!doAnimation){

            if (movePlayer.getCurrentPosition() + repetition >= NUMBER_OF_FIELDS) {
                movePlayer.setCurrentPosition((movePlayer.getCurrentPosition() + repetition) - NUMBER_OF_FIELDS);
                passedStart = true;
            }else{
                movePlayer.setCurrentPosition(movePlayer.getCurrentPosition()+ repetition);
            }
            setPosition(movePlayer.getCurrentPosition(), movePlayer);
            if (player.getId().equals(movePlayer.getId()))
                checkEndFieldPosition(passedStart);
        }
        else {
            ImageView characterImageView = movePlayer.getCharacterView();

            if (repetition == 0) {
                if (movePlayer.getId().equals(player.getId())) {
                    checkEndFieldPosition(passedStart);
                }
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
                    if (movePlayer.getCurrentPosition() + 1 >= NUMBER_OF_FIELDS) {
                        movePlayer.setCurrentPosition(0);
                        passedStart = true;
                    } else {
                        movePlayer.setCurrentPosition(movePlayer.getCurrentPosition() + 1);
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
    }

    private void checkEndFieldPosition(boolean passedStart) {
        Field field = fields.get(player.getCurrentPosition());
        if ((field.getFieldType() == FieldType.NORMAL || field.getFieldType() == FieldType.SPECIAL) &&
            field.getOwner() == null &&
            player.getMoney() >= field.getPrice()){

            showCard(findViewById(R.id.gameBoard), "card" + FIELD_NAME + (player.getCurrentPosition() + 1), "Buy Field", true, FieldType.NORMAL, () -> gameBoardViewModel.buyField(player.getCurrentPosition()));
        }else if (field.getFieldType() == FieldType.RISIKO){
            gameBoardViewModel.landOnRisikoCard(risikoCards.size());
        }else if (field.getFieldType() == FieldType.BANK){
            gameBoardViewModel.landOnBankCard(bankCards.size());
        }

        if(field.getFieldType() != FieldType.ASSET &&
                field.getOwner() != null && player.getMoney() >= field.getRent()) {
            gameBoardViewModel.payTaxes(player, field);
        }

        if(passedStart) {
            gameBoardViewModel.passStartOrMoneyField();
            this.passedStart = false;
       }
        if(player.getCurrentPosition() == 9){
            gotToJail(player);

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
    public void buildPopUp(Player player) {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_build, null);


            int width = WRAP_CONTENT;
            int height = WRAP_CONTENT;
            boolean focusable = true;
            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


            popupWindow.showAtLocation(findViewById(R.id.gameBoard), Gravity.CENTER, 0, 0);


            Button buildButton = popupView.findViewById(R.id.buildButton);
            buildButton.setOnClickListener(v -> {
                getOwnedFields(player);
                popupWindow.dismiss();
            });
        });
    }
    private void getOwnedFields(Player player) {
        for (Field field : fields) {
            int fieldIndex = fields.indexOf(field);
            if (field.getOwner() != null && field.getOwner().getId().equals(player.getId()) && field.getFieldType() == FieldType.NORMAL) {
                enableFieldClick(fieldIndex, player);
            }
        }
    }

    private void enableFieldClick(int index, Player player) {
        ImageView imageView = imageViews.get(index);
        if (imageView != null) {
            imageView.setOnClickListener(v -> buildHouse(player, index));
        }
    }
    private void buildHouse(Player player, int fieldIndex) {
        Field field = fields.get(fieldIndex);
        House house = new House(House.getHousePrice(), fieldIndex);
        gameBoardViewModel.buyBuilding(player, house, field);
    }

    private Map<Integer, List<ImageView>> fieldHousesMap = new HashMap<>();
    @Override
    public void placeBuilding(int fieldIndex, Building building, int numberOfBuildings) {
        runOnUiThread(() -> {
            ImageView buildingView = new ImageView(this);
            int resourceId = building instanceof House ? R.drawable.house : R.drawable.hotel;
            buildingView.setImageResource(resourceId);


            int houseWidth = 50;
            int houseHeight = 50;
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(houseWidth, houseHeight);
            buildingView.setLayoutParams(params);

            ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);
            constraintLayout.addView(buildingView);

            if(building instanceof Hotel){
                removeHousesFromField(fieldIndex, 4);
            } else if (building instanceof House) {
                addHouse(fieldIndex, buildingView);
            }

            int[] position = getPositionFromView(imageViews.get(fieldIndex));


            int xOffset = 10;
            int yOffset = 10;
            int xMult = 1;
            int yMult = 1;
            if (numberOfBuildings == 2){
                xMult = 5;
            }
            else if (numberOfBuildings == 3){
                yMult = 5;
            }
            else if(numberOfBuildings == 4){
                xMult = 5;
                yMult = 5;
            }
            buildingView.setX(position[0] + (float) (xOffset*xMult));
            buildingView.setY(position[1] + (float) (yOffset*yMult));
        });

    }

    private void addHouse (int fieldIndex, ImageView houseView){
        List<ImageView> houseViews = fieldHousesMap.get(fieldIndex);
        if (houseViews == null){
            houseViews = new ArrayList<>();
            fieldHousesMap.put(fieldIndex, houseViews);
        }
        houseViews.add(houseView);
    }
    private void removeHousesFromField(int fieldIndex, int numberOfHouses) {
        List<ImageView> houseViews = fieldHousesMap.get(fieldIndex);
        if (houseViews != null && houseViews.size() >= numberOfHouses) {
            ConstraintLayout constraintLayout = findViewById(R.id.gameBoard);
            for (int i = 0; i < numberOfHouses; i++) {
                ImageView houseView = houseViews.get(i);
                constraintLayout.removeView(houseView);
            }
            houseViews.subList(0, numberOfHouses).clear();
            if (houseViews.isEmpty()) {
                fieldHousesMap.remove(fieldIndex);
            }
        }
    }


    public void dicePopUp() {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.activity_popup_dice, null);

            diceImageView1 = popupView.findViewById(R.id.diceImageView1);
            diceImageView2 = popupView.findViewById(R.id.diceImageView2);

            int width = MATCH_PARENT;
            int height = MATCH_PARENT;
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
                rollButton.setText("CLOSE");
                rollButton.setOnClickListener(v -> popupWindow.dismiss());
                new ThreadTimer(4000, new TimerElapsedEvent() {
                    @Override
                    public void onTimerElapsed() {
                        runOnUiThread(popupWindow::dismiss);
                    }
                    @Override
                    public void onSecondElapsed(int secondsRemaining) {
                        runOnUiThread(() ->
                            rollButton.setText(String.format(getString(R.string.txt_general_close_msg), secondsRemaining))
                        );
                    }
                }).start();
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

        sensorManager.registerListener(proximitySensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void showCard(View view, String viewID, String btnText, boolean showBtn, FieldType fieldType, PopupBtnAction btnAction) {
        runOnUiThread(()->{
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_cards_layout, null);
            int width = MATCH_PARENT;
            int height = MATCH_PARENT;
            boolean focusable = true;

            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

            ImageView popupImageView = popupView.findViewById(R.id.popUpCards); // load specific image into pop-up that belongs to the field
            int imageResourceId = getResources().getIdentifier(viewID, DEF_TYPE, getPackageName());
            popupImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageResourceId, 200, 200));

            Button btnDiscard = popupView.findViewById(R.id.btn_cardDiscard);
            btnDiscard.setOnClickListener(v -> popupWindow.dismiss());

            Button btnBuy = popupView.findViewById(R.id.btn_buy);
            btnBuy.setText(btnText);
            btnBuy.setOnClickListener(v -> {
                btnAction.callAction();
                popupWindow.dismiss();
            });
            if(!showBtn){
                disableView(btnBuy);
                new ThreadTimer(7000, new TimerElapsedEvent() {
                    @Override
                    public void onTimerElapsed() {
                        runOnUiThread(popupWindow::dismiss);
                    }

                    @Override
                    public void onSecondElapsed(int secondsRemaining) {
                        runOnUiThread(() ->
                                btnDiscard.setText(String.format(getString(R.string.txt_general_close_msg), secondsRemaining))
                        );
                    }
                }).start();
            } else if (fieldType != FieldType.NORMAL) {
                disableView(btnDiscard);
                new ThreadTimer(7000, new TimerElapsedEvent() {
                    @Override
                    public void onTimerElapsed() {
                        runOnUiThread(() -> {
                            btnAction.callAction();
                            popupWindow.dismiss();
                        });
                    }

                    @Override
                    public void onSecondElapsed(int secondsRemaining) {
                        runOnUiThread(() ->
                                btnBuy.setText(String.format(getString(R.string.txt_general_ok_msg), secondsRemaining))
                        );
                    }
                }).start();
            }
        });

    }
    @Override
     public void showTaxes(Player payer, Player payee, int amount) {
        runOnUiThread(()->{
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_info_text, null);
            int width = MATCH_PARENT;
            int height = MATCH_PARENT;
            boolean focusable = true;

            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(findViewById(R.id.gameBoard), Gravity.CENTER, 0, 0);
            TextView playerTaxesTextView = popupView.findViewById(R.id.txt_playerTaxes);
            String taxesMessage = payer.getUsername() + " pay " + amount + " $ Taxes to " + payee.getUsername();
            playerTaxesTextView.setText(taxesMessage);

            Button btnClose = popupView.findViewById(R.id.btn_closeInfoPopup);
            btnClose.setOnClickListener(v -> popupWindow.dismiss());

            new ThreadTimer(5000, new TimerElapsedEvent() {
                @Override
                public void onTimerElapsed() {
                    runOnUiThread(popupWindow::dismiss);
                }

                @Override
                public void onSecondElapsed(int secondsRemaining) {
                    runOnUiThread(() ->
                        btnClose.setText(String.format(getString(R.string.txt_general_close_msg), secondsRemaining))
                    );
                }
            }).start();
        });


    }

    @Override
    public void showCheaterDetectedPopUp(Player cheater, Player detective) {
        runOnUiThread(() -> {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupView = inflater.inflate(R.layout.popup_info_text, null);
            int width = MATCH_PARENT;
            int height = MATCH_PARENT;
            boolean focusable = true;

            PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
            popupWindow.showAtLocation(findViewById(R.id.gameBoard), Gravity.CENTER, 0, 0);

            TextView playerTaxesTextView = popupView.findViewById(R.id.txt_playerTaxes);
            String taxesMessage = String.format(getString(R.string.txt_cheater_detected), cheater.getUsername(), detective.getUsername());
            playerTaxesTextView.setText(taxesMessage);

            Button btnClose = popupView.findViewById(R.id.btn_closeInfoPopup);
            btnClose.setOnClickListener(v -> popupWindow.dismiss());
            new ThreadTimer(5000, new TimerElapsedEvent() {
                @Override
                public void onTimerElapsed() {
                    runOnUiThread(popupWindow::dismiss);
                }

                @Override
                public void onSecondElapsed(int secondsRemaining) {
                    runOnUiThread(() ->
                        btnClose.setText(String.format(getString(R.string.txt_general_close_msg), secondsRemaining))
                    );
                }
            }).start();
        });
    }

    @Override
    public void switchToWinScreen(Player fromPlayer) {
        runOnUiThread(() -> {
            Intent intent = new Intent(this, WinScreen.class);
            intent.putExtra("playername", fromPlayer.getUsername());
            startActivity(intent);
            sensorManager.unregisterListener(this);
            finish();
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

    @Override
    public void disableDiceButton() {
        runOnUiThread(()-> disableView(testButton));
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
    public void reportCheat(Player player, Player fromPlayer) {
        gameBoardViewModel.reportCheat(player, fromPlayer);
    }

    public void reportCheater(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        List<Player> playersWithoutMe = this.players.stream()
                .filter(p -> !p.getId().equals(player.getId()))
                .collect(Collectors.toList());
        ReportCheaterDialog dialogFragment = new ReportCheaterDialog(playersWithoutMe, new ReportCheaterDialog.OnPlayerSelectedListener() {
            @Override
            public void onPlayerSelected(Player player) {
                // not used
            }

            @Override
            public void onPlayerConfirmed(Player player) {
                reportCheat(player, GameBoard.this.player);
            }
        });
        dialogFragment.show(fragmentManager, "player_selection");
    }
    public void showCardRisiko(int indexCard, boolean showBtn, Player fromPlayer) {
        Card currentCard = risikoCards.get(indexCard);
        if(currentCard instanceof JokerCard){
            JokerCard joker = (JokerCard)currentCard;
            gameBoardViewModel.addJokerCard(joker, fromPlayer);
        }
        showCard(findViewById(R.id.gameBoard), currentCard.getImageResource(),"Ok",showBtn, FieldType.RISIKO,() -> {
            Log.d(TAG, "showCardRisiko" + (currentCard instanceof MoveCard ? "moveCard":"payCard"));
            currentCard.doActionOfCard(this.gameBoardViewModel);
        });
    }
    public void showCardBank(int indexCard, boolean showBtn) {
        Card currentCard = bankCards.get(indexCard);
        showCard(findViewById(R.id.gameBoard), currentCard.getImageResource(), "Ok",showBtn, FieldType.BANK, () -> {
            Log.d(TAG, "showCardBank");
            currentCard.doActionOfCard(this.gameBoardViewModel);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.sensorManager.unregisterListener(proximitySensorListener, proximitySensor);
        this.sensorManager = null;

    }

    public void gotToJail(Player playerInJail){


        //player.setRoundsToSkip(3);
        int currentposition = playerInJail.getCurrentPosition();
        int jail = 24;
        int round = 0;
        if(currentposition < 24){
            round = jail-currentposition;
        }else{ //current position > 24
            round= jail + (30 - currentposition); //goal: field 24, to reach this field he has to go till start + 24 fields
        }

        gameBoardViewModel.setRoundsToSkip(round);
    }





}