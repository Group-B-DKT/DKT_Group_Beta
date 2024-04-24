package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dkt_group_beta.activities.interfaces.GameSearchAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import java.util.List;

public class GameSearchViewModel extends ViewModel {
    private String username;
    private final InfoController infoController;
    private final ConnectController connectController;
    private final ActionController actionController;
    private final GameSearchAction gameSearchAction;


    public GameSearchViewModel(String uri, String username, String id, GameSearchAction gameSearchAction){
        WebsocketClientController.connectToServer(uri, id, username);
        infoController = new InfoController(this::handleInfo);
        connectController = new ConnectController(this::onConnectionEstablished);
        actionController = new ActionController(this::handleAction);
        this.gameSearchAction = gameSearchAction;
        this.username = username;
    }

    public void receiveGames (){
        infoController.getGameListFromServer();
    }

    public void connectToGame(int gameId){
        Log.d("DEBUG", "GameSearchViewModel::connectToGame/ " + gameId);
        actionController.joinGame(gameId);
    }

    public void createGame(String inputText) {
        actionController.createGame(inputText);
    }


    void handleInfo(Info info, List<GameInfo> gameInfos){
        Log.d("DEBUG", "GameSearchViewModel::handleInfo/ " + gameInfos);
        if (gameInfos == null) return;

        gameSearchAction.refreshGameListItems();
        gameInfos.forEach((gameInfo) -> gameSearchAction.addGameToScrollView(gameInfo.getId(),
                                                                             gameInfo.getName(),
                                                                             gameInfo.getConnectedPlayers() == null ? 0 : gameInfo.getConnectedPlayers().size()));

    }

    void onConnectionEstablished(){
        gameSearchAction.onConnectionEstablished();
    }

    void handleAction(Action action, String param, Player fromPlayer){
        if (fromPlayer == null || !fromPlayer.getUsername().equals(username)) {
            this.receiveGames();
            return;
        }

        if (action == Action.GAME_CREATED_SUCCESSFULLY) {
            WebsocketClientController.getPlayer().setHost(true);
            gameSearchAction.switchToGameLobby(username);
        }
        if (action == Action.GAME_JOINED_SUCCESSFULLY){
            gameSearchAction.switchToGameLobby(username);
        }
    }
}
