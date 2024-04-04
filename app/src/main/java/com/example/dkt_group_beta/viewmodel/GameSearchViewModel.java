package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

public class GameSearchViewModel extends ViewModel {
    private final InfoController infoController;

    public GameSearchViewModel(){
        infoController = new InfoController();
        WebsocketClientController.addMessageHandler(this::messageReceivedFromServer);
    }

    public void receiveGames (){
        infoController.getGameListFromServer();
    }

    private void messageReceivedFromServer(String message) {
        Log.d("DEBUG", "RECEIVED" + message);
    }
}
