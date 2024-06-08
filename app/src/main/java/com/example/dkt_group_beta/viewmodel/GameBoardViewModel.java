package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.House;
import com.example.dkt_group_beta.model.Player;
import com.google.gson.Gson;

import java.util.Arrays;
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
//        gameBoardAction.markBoughtField(index);
        actionController.buyField(field);

    }
    public void buyBuilding(Player player, House house, Field field){
        boolean building = game.buyHouse(player, house, field);
        if (building) {
            actionController.buyBuilding(field);
        }
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
        if (action == Action.BUY_BUILDING) {
            game.updateField(fields.get(0));
            game.updatePlayer(fromPlayer);
            gameBoardAction.updatePlayerStats();
            gameBoardAction.placeBuilding(fields.get(0).getId()-1, fields.get(0).getBuildings().get(0), fields.get(0).getBuildings().size());
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
}
