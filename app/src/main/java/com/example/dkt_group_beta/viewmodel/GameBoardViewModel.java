package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.GameBoard;
import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Game;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import java.util.ArrayList;
import java.util.List;

public class GameBoardViewModel {

    private GameBoardViewModel gameBoardViewModel;
    private List<Player> connectedPlayers;
    private InfoController infoController;
    private ActionController actionController;
    private ConnectController connectController;
    private GameLobbyAction gameLobbyAction;
    private GameBoard gameBoard;
    private Player player;
    private Field field;
    private List<Field> fields;
    private Game game;

    public GameBoardViewModel() {
        this.infoController = new InfoController(this::handleInfo);
        this.actionController = new ActionController(this::handleAction);
        this.connectedPlayers = new ArrayList<>();


    }

    public void buyField(int index) {
        Field field = game.buyField(index);
        gameBoard.markBoughtField(index);
        actionController.buyField(field);

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

    private void handleAction(Action action, String param, Player fromPlayer, List<Field> fields) {
        Log.d("DEBUG", "GameLobbyViewModel::handleAction/ " + action);
        if (action == Action.BUY_FIELD) {
            game.updateField(fields.get(0));
            gameBoard.markBoughtField(fields.get(0).getId()-1);

            }
        }

}
