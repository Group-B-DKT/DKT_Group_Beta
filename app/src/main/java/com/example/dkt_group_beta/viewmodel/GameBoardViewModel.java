package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;
import com.google.gson.Gson;

import java.util.List;

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
        actionController.buyField(field);

    }

    void handleAction(Action action, String param, Player fromPlayer, List<Field> fields){
        if(action == Action.ROLL_DICE) {
            handleRollDice(action, param, fromPlayer);
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

        if (action == Action.CONNECTION_LOST){
            handleConnectionLost(fromPlayer);
        }
    }

    private void handleConnectionLost(Player disconnectedPlayer) {
        gameBoardAction.showDisconnectPopUp(disconnectedPlayer);
    }

    private void handleBuyField(Player fromPlayer, List<Field> fields) {
        game.updateField(fields.get(0));
        if (!fromPlayer.getId().equals(player.getId()))
            game.updatePlayer(fromPlayer);
        gameBoardAction.markBoughtField(fields.get(0).getId()-1, fromPlayer.getColor());
        gameBoardAction.updatePlayerStats();
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

    private void handleRollDice(Action action, String param, Player fromPlayer) {
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
}
