package com.example.dkt_group_beta.viewmodel;

import com.example.dkt_group_beta.activities.interfaces.GameBoardAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.Player;

public class GameBoardViewModel {
    private ActionController actionController;
    private GameBoardAction gameBoardAction;
    private Game game;


    public GameBoardViewModel(GameBoardAction gameBoardAction, Game game) {
        this.actionController = actionController;
        this.game = game;
    }
    void handleAction(Action action, String param, Player fromPlayer){

    }

    public int getRandomNumber(int min, int max){
        return game.getRandomNumber(min,max);
    }

    public void rollDice(int[] diceResults){
        actionController.diceRolled(diceResults);
    }

}
