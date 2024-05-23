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

    public void passStart(boolean passedStart){

        if(player.getCurrentPosition() == 0){
            game.setMoney(player, 400);
            Log.d("MONEY", "Player at position 0" + player.getMoney());
        }else if(passedStart == true){
            game.setMoney(player, 200);
            Log.d("MONEY", "Player passed start" + player.getMoney());

        }

    }
}
