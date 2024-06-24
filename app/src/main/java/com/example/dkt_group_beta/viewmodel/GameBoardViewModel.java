package com.example.dkt_group_beta.viewmodel;

import android.util.Log;
import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.JokerCard;
import com.example.dkt_group_beta.model.House;
import com.example.dkt_group_beta.model.Player;
import com.google.gson.Gson;

import java.time.LocalTime;
import java.util.List;

public class GameBoardViewModel {
    private ActionController actionController;
    private GameBoardAction gameBoardAction;
    private Game game;
    private Player player;
    private Player payerServer;


    public GameBoardViewModel(GameBoardAction gameBoardAction, Game game) {
        this.actionController = new ActionController(this::handleAction);
        this.gameBoardAction = gameBoardAction;
        this.game = game;
        player = WebsocketClientController.getPlayer();
    }

    public void buyField(int index) {
        Field field = game.buyField(index);
        actionController.buyField(field);

    }
    public void payTaxes(Player player, Field field) {
        boolean result = game.payTaxes(player,field);
        if(result) {
            actionController.payTaxes(player);
            actionController.payTaxes(field.getOwner());
        }
    }
    public void buyBuilding(Player player, House house, Field field){
        boolean building = game.buyHouse(player, house, field);
        if (building) {
            actionController.buyBuilding(field);
        }
    }


    void handleAction(Action action, String param, Player fromPlayer, List<Field> fields){
        if(action == Action.ROLL_DICE) {
            handleRollDice(param, fromPlayer);
        }

        if(action == Action.MOVE_PLAYER){
            Player movePlayer = game.getPlayerById(fromPlayer.getId());
            int repetition = Integer.parseInt(param);
            gameBoardAction.animation(movePlayer, repetition);
        }

        if (action == Action.END_TURN){
            handleEndTurn(fromPlayer);
        }

        if (action == Action.BUY_FIELD) {
            handleBuyField(fromPlayer, fields);
        }
        if (action == Action.BUY_BUILDING) {
            handleBuyBuilding(fields, fromPlayer);
        }

        if(action == Action.UPDATE_MONEY){
            game.updatePlayer(fromPlayer);
            gameBoardAction.updatePlayerStats();
        }
        if(action == Action.PAY_TAXES){
            if(payerServer == null) {
                payerServer = fromPlayer;
            }else {
                gameBoardAction.showTaxes(payerServer, fromPlayer, Integer.parseInt(param));
                payerServer = null;
            }
            game.updatePlayer(fromPlayer);
            gameBoardAction.updatePlayerStats();

        }

        if (action == Action.HOST_CHANGED){
            if (fromPlayer.getId().equals(player.getId()) && !player.isHost()) {
                player.setHost(fromPlayer.isHost());
            }
            game.updateHostStatus(fromPlayer.getId());
        }

        if (action == Action.CONNECTION_LOST){
            handleConnectionLost(fromPlayer, LocalTime.parse(param));
        }

        if (action == Action.RECONNECT_OK){
            gameBoardAction.removeReconnectPopUp();
        }

        if (action == Action.RECONNECT_DISCARD){
            gameBoardAction.removePlayerFromGame(fromPlayer);
            gameBoardAction.removeReconnectPopUp();
        }

        if (action == Action.REPORT_CHEAT){
            Player cheater = game.getPlayerById(param);
            gameBoardAction.showCheaterDetectedPopUp(cheater, fromPlayer);
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
    }

    private void handleConnectionLost(Player disconnectedPlayer, LocalTime serverTime) {
        gameBoardAction.setPlayerDisconnected(disconnectedPlayer);
        gameBoardAction.showDisconnectPopUp(disconnectedPlayer, serverTime);
    }

    private void handleBuyField(Player fromPlayer, List<Field> fields) {
        game.updateField(fields.get(0));
        if (!fromPlayer.getId().equals(player.getId()))
            game.updatePlayer(fromPlayer);
        gameBoardAction.markBoughtField(fields.get(0).getId()-1, fromPlayer.getColor());
        gameBoardAction.updatePlayerStats();
    }
    private void handleBuyBuilding(List<Field> fields, Player fromPlayer){
        game.updateField(fields.get(0));
        game.updatePlayer(fromPlayer);
        gameBoardAction.updatePlayerStats();
        if(fields.get(0).getHotel() != null){
            gameBoardAction.placeBuilding(fields.get(0).getId()-1, fields.get(0).getHotel(), 1);
        }else {
            gameBoardAction.placeBuilding(fields.get(0).getId() - 1, fields.get(0).getHouses().get(0), fields.get(0).getHouses().size());
        }
    }

    private void handleEndTurn(Player fromPlayer) {
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

    private void handleRollDice(String param, Player fromPlayer) {
        Log.d("DEBUG", fromPlayer.getUsername());
        // array zurücksetzten, popup öffnen, showbothdice
        if (fromPlayer.getId().equals(player.getId())) {
            return;
        }
        Gson gson = new Gson();
        int[] diceResult = gson.fromJson(param,int[].class);
        gameBoardAction.dicePopUp();
        gameBoardAction.showBothDice(diceResult);
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

    public void submitCheat(int money) {
        actionController.submitCheat(money);
    }
    public void passStartOrMoneyField() {

        if (player.getCurrentPosition() == 0) {
            game.setMoney(400);
        } else if (player.getCurrentPosition() == 17) {
            game.setMoney(100);
        } else {
            game.setMoney(200);
        }

        actionController.moneyUpdate(player);
    }

    public void payForCard(int amount){
        game.setMoney(amount);
        actionController.moneyUpdate(player);
    }

    public void moveForCard(int fieldID, int amount){
        int fieldAmount = game.getFieldListSize();
        int fieldPosition = game.getFieldPosition(fieldID);
        int currentPosition = player.getCurrentPosition();
        int moveAmount;
        if(currentPosition > fieldPosition) {
            moveAmount = fieldAmount - (currentPosition - fieldPosition);
            game.setMoney(amount);
        } else {
            moveAmount = fieldPosition - currentPosition;
        }
        actionController.movePlayer(moveAmount);
    }

    public void removePlayer(int gameId, Player player) {
        player.setDefaulValues();
        actionController.removePlayer(gameId, player);
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
    }

    public void reportCheat(Player player, Player fromPlayer) {
        actionController.reportCheat(player, fromPlayer);
    }
}
