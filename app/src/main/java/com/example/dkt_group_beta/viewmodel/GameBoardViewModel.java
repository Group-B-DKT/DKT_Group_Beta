package com.example.dkt_group_beta.viewmodel;

import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;
import com.google.gson.Gson;

public class GameBoardViewModel {
    private ActionController actionController;
    private GameBoardAction gameBoardAction;
    private Game game;
    private Player player;


    public GameBoardViewModel(GameBoardAction gameBoardAction, Game game) {
        this.actionController = new ActionController(this::handleAction);
        this.game = game;
        player = WebsocketClientController.getPlayer();
    }
    void handleAction(Action action, String param, Player fromPlayer){
        if(action == Action.ROLL_DICE) {
            // array zurücksetzten, popup öffnen, showbothdice
            if (fromPlayer.getId().equals(player.getId()) && !player.isHost()) {
                return;
            }
            Gson gson = new Gson();
            int[] diceResult = gson.fromJson(param,int[].class);
            gameBoardAction.dicePopUp();
        }
    }

    public int getRandomNumber(int min, int max){
        return game.getRandomNumber(min,max);
    }

    public void rollDice(int[] diceResults){
        actionController.diceRolled(diceResults);
    }

}
