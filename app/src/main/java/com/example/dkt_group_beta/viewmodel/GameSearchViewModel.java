package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dkt_group_beta.activities.interfaces.GameSearchAction2;
import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Info;

import java.util.Map;

public class GameSearchViewModel extends ViewModel {
    private final InfoController infoController;
    private final ConnectController connectController;
    private final GameSearchAction2 gameSearchAction;


    public GameSearchViewModel(String uri, String username, String id, GameSearchAction2 gameSearchAction){
        WebsocketClientController.connectToServer(uri, id, username);
        infoController = new InfoController(this::handleInfo);
        connectController = new ConnectController(this::onConnectionEstablished);
        this.gameSearchAction = gameSearchAction;
    }

    public void receiveGames (){
        infoController.getGameListFromServer();
    }

    public void connectToGame(int gameId){
        Log.d("DEBUG", "GameSearchViewModel::connectToGame/ " + gameId);
    }

    void handleInfo(Info info, Map<Integer, Integer> gameInfo){
        Log.d("DEBUG", "GameSearchViewModel::handleInfo/ " + gameInfo);
        if (gameInfo == null) return;

        gameSearchAction.refreshGameList();
        gameInfo.forEach(gameSearchAction::addGameToScrollView);

    }

    void onConnectionEstablished(){
        gameSearchAction.onConnectionEstablished();
    }


}
