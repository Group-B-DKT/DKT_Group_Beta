package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Info;

import java.util.Map;

public class GameSearchViewModel extends ViewModel {
    private final InfoController infoController;

    public GameSearchViewModel(){
        infoController = new InfoController(this::handleInfo);
    }

    public void receiveGames (){
        infoController.getGameListFromServer();
    }

    void handleInfo(Info info, Map<Integer, Integer> gameInfo){

    }

}
