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
    void handleAction(Action action, String param, Player fromPlayer, List<Field> fields){
        if(action == Action.ROLL_DICE) {
            Log.d("DEBUG2", fromPlayer.getUsername());
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

            Log.d("MOVE", param);
            Player movePlayer = game.getPlayerById(fromPlayer.getId());
            int repetition = Integer.parseInt(param);
            gameBoardAction.animation(movePlayer, repetition);

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

}
