package com.example.dkt_group_beta.viewmodel;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.util.Log;

import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.io.CardCSVReader;
import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.JokerCard;
import com.example.dkt_group_beta.model.Player;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameBoardViewModel {
    private ActionController actionController;
    private GameBoardAction gameBoardAction;
    private Game game;
    private Player player;

    public GameBoardViewModel(GameBoardAction gameBoardAction, Game game) {
        this.actionController = new ActionController(this::handleAction);
        this.gameBoardAction = gameBoardAction;
        this.game = game;
        player = WebsocketClientController.getPlayer();
    }

    public void buyField(int index) {
        Field field = game.buyField(index);
//        gameBoardAction.markBoughtField(index);
        actionController.buyField(field);

    }

    void handleAction(Action action, String param, Player fromPlayer, List<Field> fields){
        Log.d("game-card","handleAction: " + action.toString());
        if(action == Action.ROLL_DICE) {
            Log.d("DEBUG", fromPlayer.getUsername());
            // array zurücksetzten, popup öffnen, showbothdice
            if (fromPlayer.getId().equals(player.getId())) {
                return;
            }
            Log.d("game",""+action);
            Gson gson = new Gson();
            int[] diceResult = gson.fromJson(param,int[].class);
            Log.d("game",""+ Arrays.toString(diceResult));
            gameBoardAction.dicePopUp();
            gameBoardAction.showBothDice(diceResult);
        }

        if(action == Action.MOVE_PLAYER){
            Player movePlayer = game.getPlayerById(fromPlayer.getId());
            int repetition = Integer.parseInt(param);
            gameBoardAction.animation(movePlayer, repetition);
        }

        if (action == Action.END_TURN){
            if (player.isOnTurn()){
                gameBoardAction.disableEndTurnButton();
                player.setOnTurn(false);
            }else if (player.getId().equals(fromPlayer.getId())){
                gameBoardAction.enableDiceButton();
                gameBoardAction.enableEndTurnButton();
            }
            game.setPlayerTurn(fromPlayer.getId());
            gameBoardAction.updatePlayerStats();
        }

        if (action == Action.BUY_FIELD) {
            game.updateField(fields.get(0));
            if (!fromPlayer.getId().equals(player.getId()))
                game.updatePlayer(fromPlayer);
            gameBoardAction.markBoughtField(fields.get(0).getId()-1, fromPlayer.getColor());
            gameBoardAction.updatePlayerStats();
        }
        if (action == Action.RISIKO_CARD_SHOW){
            Log.d("DEBUG", fromPlayer.getUsername());
            int cardIndex = Integer.parseInt(param);
            boolean showBtn = false;
            if (fromPlayer.getId().equals(player.getId())) {
                showBtn = true;
            }
            gameBoardAction.showCardRisiko(cardIndex, showBtn, fromPlayer);
        }
        if (action == Action.BANK_CARD_SHOW){
            Log.d("DEBUG", fromPlayer.getUsername());
            int cardIndex = Integer.parseInt(param);
            boolean showBtn = false;
            if (fromPlayer.getId().equals(player.getId())) {
                showBtn = true;
            }
            gameBoardAction.showCardBank(cardIndex, showBtn);
        }
        if(action == Action.UPDATE_MONEY){
            game.getPlayers().stream()
                    .filter(p -> p.getId().equals(fromPlayer.getId()))
                    .findAny()
                    .orElse(null)
                    .setMoney(fromPlayer.getMoney());
            gameBoardAction.updatePlayerStats();
        }
    }
    public void landOnRisikoCard(int cardAmount){
        int randomNumber = getRandomNumber(0,cardAmount-1);
        actionController.showRisikoCard(randomNumber);
    }
    public void landOnBankCard(int cardAmount){
        int randomNumber = getRandomNumber(0,cardAmount-1);
        actionController.showBankCard(randomNumber);
    }

    public int getRandomNumber(int min, int max){
        return game.getRandomNumber(min,max);
    }

    public void rollDice(int[] diceResults){
        actionController.diceRolled(diceResults);
    }

    public void movePlayer(int dice){

        actionController.movePlayer(dice);

    }
    public void endTurn() {
        actionController.endTurn();
    }

    public void passStartOrMoneyField(){

        if(player.getCurrentPosition() == 0){
            game.setMoney(400);
        }else if(player.getCurrentPosition() == 17){
            game.setMoney(100);
        }else{
            game.setMoney(200);
        }
        actionController.moneyUpdate();
    }

    public void payForCard(int amount){
        game.setMoney(amount);
        actionController.moneyUpdate();
    }
    public void moveForCard(int fieldID, int amount){
        int fieldAmount = game.getFieldListSize();
        int fieldPosition = game.getFieldPosition(fieldID);
        int currentPosition = player.getCurrentPosition();
        int moveAmount;
        if(currentPosition > fieldPosition){
            moveAmount = fieldAmount-(currentPosition-fieldPosition);
            game.setMoney(amount);
        }else{
            moveAmount = fieldPosition - currentPosition;
        }
        actionController.movePlayer(moveAmount);
    }

    public void addJokerCard(JokerCard joker){
        //player.addJokerCard(joker);
        gameBoardAction.updatePlayerStats();    // update the joker amount before endTurn
    }
    public void addJokerCard(JokerCard joker, Player fromPlayer){
        game.getPlayers().stream()
                .filter(p -> p.getId().equals(fromPlayer.getId()))
                .findAny()
                .orElse(null).addJokerCard(joker);
        gameBoardAction.updatePlayerStats();    // update the joker amount before endTurn
    }}
