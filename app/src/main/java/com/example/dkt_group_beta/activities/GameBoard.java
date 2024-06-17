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
import com.example.dkt_group_beta.activities.interfaces.PopupBtnAction;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.io.CardCSVReader;
import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.JokerCard;
import com.example.dkt_group_beta.model.MoveCard;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.model.enums.FieldType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import com.example.dkt_group_beta.activities.adapter.PlayerItemAdapter;
import com.example.dkt_group_beta.model.Field;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class GameBoard extends AppCompatActivity implements SensorEventListener, GameBoardAction {
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

    private boolean passedStart;

    List<Card> cards;
    List<Card> risikoCards;
    List<Card> bankCards;

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
        btnEndTurn = findViewById(R.id.btn_endTurn);
        rvPlayerStats = findViewById(R.id.rv_playerStats);


        player = WebsocketClientController.getPlayer();

        diceResults = new int[2];
        endTurnLayout = new ViewGroup.LayoutParams(btnEndTurn.getLayoutParams());


        players = (List<Player>) getIntent().getSerializableExtra("players");
        players.removeIf(p -> p.getId().equals(player.getId()));
        players.add(player);
        fields = (List<Field>) getIntent().getSerializableExtra("fields");
        players.sort(Comparator.comparing(Player::getId));
//        cards = CardCSVReader.readCards(getApplicationContext(),"risiko.csv");

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
                    .getIdentifier("character" + (i+1), DEF_TYPE, this.getPackageName());

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

                ViewGroup.LayoutParams params = p.getCharacterView().getLayoutParams();
                int size = findViewById(R.id.field1).getWidth() / 2;
                params.width = size;
                params.height = size;
                p.getCharacterView().setLayoutParams(params);
            }
        });

        Game game = new Game(players, fields);
        gameBoardViewModel = new GameBoardViewModel(this, game);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // initialising the Sensor
        createPlayerItems(game.getPlayers());

        testButton = findViewById(R.id.popUpCards);
        Log.d(TAG, "IST: " + player.getUsername());
        if (!player.isOnTurn()) {
            disableView(testButton);
            disableView(btnEndTurn);
        }
        for (int i = 1; i <= NUMBER_OF_FIELDS; i++) {
            int resourceId = this.getResources().getIdentifier(FIELD_NAME + i, "id", this.getPackageName());
            ImageView imageView = findViewById(resourceId);
            if (imageView != null) {
                imageViews.add(imageView);
            }
        }

        testButton.setOnClickListener(v -> dicePopUp());
        btnEndTurn.setOnClickListener(v -> {
            if (player.isOnTurn()){
                gameBoardViewModel.endTurn();
                disableView(testButton);
                diceRolling = false;
            }
        });

        risikoCards = CardCSVReader.readCards(getApplicationContext(), "risiko.csv");
        bankCards = CardCSVReader.readCards(getApplicationContext(), "bank.csv");
        Log.d("DEBUG", ""+risikoCards);
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
        Log.d(TAG, "variable:" + index);
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
        if ((field.getFieldType() == FieldType.NORMAL || field.getFieldType() == FieldType.SPECIAL) &&
            field.getOwner() == null &&
            player.getMoney() >= field.getPrice()){

            showCard(findViewById(R.id.gameBoard), "card" + FIELD_NAME + (player.getCurrentPosition() + 1), "Buy Field", true, () -> gameBoardViewModel.buyField(player.getCurrentPosition()));
        }else if (field.getFieldType() == FieldType.RISIKO){
            gameBoardViewModel.landOnRisikoCard(risikoCards.size());
        }else if (field.getFieldType() == FieldType.BANK){
            gameBoardViewModel.landOnBankCard(bankCards.size());
        }

        if(passedStart) {
            gameBoardViewModel.passStartOrMoneyField();
            this.passedStart = false;
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
//        if (!player.isOnTurn())
//            return;
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
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }

    public void showCard(View view, String viewID, String btnText, boolean showBtn, PopupBtnAction btnAction) {
        Log.d("game-card","ShowCard");
        runOnUiThread(()->{
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.popup_cards_layout, null);
                    int width = WRAP_CONTENT;
                    int height = WRAP_CONTENT;
                    boolean focusable = true;

                    PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    popupWindow.setTouchable(true);
                    popupWindow.setFocusable(false);
                    popupWindow.setOutsideTouchable(false);

                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                    ImageView popupImageView = popupView.findViewById(R.id.popUpCards); // load specific image into pop-up that belongs to the field
                    int imageResourceId = getResources().getIdentifier(viewID, DEF_TYPE, getPackageName());
                    popupImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), imageResourceId, 200, 200));

                    Button btnBuy = popupView.findViewById(R.id.btn_buy);
                    btnBuy.setText(btnText);
                    btnBuy.setOnClickListener(v -> {
                        btnAction.callAction();
                        popupWindow.dismiss();
                    });
                    if(!showBtn){
                        disableView(btnBuy);
                    }
        });
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
    public void showCardRisiko(int indexCard, boolean showBtn, Player fromPlayer) {
        Card currentCard = risikoCards.get(indexCard);
        if(currentCard instanceof JokerCard){
            JokerCard joker = (JokerCard)currentCard;
            gameBoardViewModel.addJokerCard(joker, fromPlayer);
        }
        showCard(findViewById(R.id.gameBoard), currentCard.getImageResource(),"Ok",showBtn,() -> {
            Log.d("DEBUG", "showCardRisiko" + (currentCard instanceof MoveCard ? "moveCard":"payCard"));
            currentCard.setGameBoardViewModel(this.gameBoardViewModel);
            currentCard.doActionOfCard();
        });
    }
    public void showCardBank(int indexCard, boolean showBtn) {
        Card currentCard = bankCards.get(indexCard);
        showCard(findViewById(R.id.gameBoard), currentCard.getImageResource(), "Ok",showBtn, () -> {
            Log.d("DEBUG", "showCardBank");
            currentCard.setGameBoardViewModel(this.gameBoardViewModel);
            currentCard.doActionOfCard();
        });
    }
}