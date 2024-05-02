package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.GameBoard;
import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private GameModel gameModel;
    private List<Player> connectedPlayers;
    private InfoController infoController;
    private ActionController actionController;
    private ConnectController connectController;
    private GameLobbyAction gameLobbyAction;
    private GameBoard gameBoard;

    private Player player;
    private Field field;
    private List<Field> fields;

    public GameModel(List<Field> list, Player player) {
        //this.infoController = new InfoController(this::handleInfo);
        //this.actionController = new ActionController(this::handleAction);
        this.connectedPlayers = new ArrayList<>();
        //this.player = WebsocketClientController.getPlayer();
        this.fields = list;
        this.player = player;

    }

    public void playerLandedOnField(Player currentPlayer, Field field) {
        if(field.getOwner() != null) {
            if(!field.getOwner().equals(currentPlayer)) {
                int paymentAmount = calculatePayment(field);
                if(currentPlayer.getMoney() >= paymentAmount) {
                    processPayment(currentPlayer, field.getOwner(), paymentAmount);
                }
            }

        }


        }


    int calculatePayment(Field field) {
        return field.getPrice();
    }

    void processPayment(Player currentPlayer, Player owner, int paymentAmount) {
        currentPlayer.setMoney(currentPlayer.getMoney() - paymentAmount);
        owner.setMoney(owner.getMoney() + paymentAmount);
    }

    public Field buyField(int index) {

        if (index < 0 || index >= fields.size()) {
            return null;
        }

        Field field = fields.get(index);
        if (field.getOwnable() && player.getMoney() >= field.getPrice()) {
            field.setOwner(player);
            field.setOwnable(false);
            player.setMoney(player.getMoney() - field.getPrice());
            return field;
        } else {
            Log.d("debug", "Du kannst das nicht kaufen weil du arm bist Heheha");
            System.out.println("fehler");
            return null;
        }
    }


    private void handleInfo(Info info, List<GameInfo> gameInfos) {
        Log.d("DEBUG", "GameLobbyViewModel::handleInfo/ " + gameInfos.get(0).getConnectedPlayers());
        GameInfo gameInfo = gameInfos.get(0);

        if (gameInfo == null) return;

        gameInfo.getConnectedPlayers()
                .forEach(g -> {
                    if (!this.connectedPlayers.contains(g)) {
                        this.connectedPlayers.add(g);
                        gameLobbyAction.addPlayerToView(g);
                    }
                });
    }

    private void handleAction(Action action, String param, Player fromPlayer) {
        Log.d("DEBUG", "GameLobbyViewModel::handleAction/ " + action);
        if (action == Action.BUY_FIELD) {
            Log.d("debug", "success");

            }
        }

}
