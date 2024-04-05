package com.example.dkt_group_beta.viewmodel;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.dkt_group_beta.communication.controller.ConnectController;
import com.example.dkt_group_beta.communication.controller.InfoController;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.enums.Info;

import java.util.Map;

public class GameSearchViewModel extends ViewModel {
    //private final InfoController infoController;
    String username;
    String id;
    String uri;


    public GameSearchViewModel(String uri, String username, String id){
        /*infoController = new InfoController(this::handleInfo);
        this.username = username;
        this.id = id;
        this.uri = uri;*/
        WebsocketClientController.connectToServer(uri, id, username);

    }

    public void receiveGames (){
        //infoController.getGameListFromServer();
    }

    void handleInfo(Info info, Map<Integer, Integer> gameInfo){
        Log.d("DEBUG", "handleInfo: ");

    }

}
