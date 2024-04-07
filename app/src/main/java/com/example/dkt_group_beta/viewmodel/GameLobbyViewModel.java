package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.communication.controller.ActionController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.enums.Action;
import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyViewModel {
    private List<String> usernames;
    private InfoController infoController;
    private ActionController actionController;
    private GameLobbyAction gameLobbyAction;

    public GameLobbyViewModel(GameLobbyAction gameLobbyAction){
        this.infoController = new InfoController(this::handleInfo);
        this.actionController = new ActionController(this::handleAction);
        this.gameLobbyAction = gameLobbyAction;
        this.usernames = new ArrayList<>();
    }

    public void getConnectedPlayerNames(){
        infoController.getConnectedPlayers();
    }

    public void handleInfo(Info info, List<GameInfo> gameInfos){
        Log.d("DEBUG", "GameLobbyViewModel::handleInfo/ " + gameInfos);
        GameInfo gameInfo = gameInfos.get(0);

        if (gameInfo == null) return;

        gameInfo.getConnectedPlayerNames()
                .forEach(g -> {
                    if (!this.usernames.contains(g)){
                        this.usernames.add(g);
                        gameLobbyAction.addPlayerToView(g);
                    }
                });
    }

    public void handleAction(Action action, String param, String fromPlayername){
        Log.d("DEBUG", "GameLobbyViewModel::handleAction/ " + action);
        this.getConnectedPlayerNames();
    }


}
