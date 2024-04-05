package com.example.dkt_group_beta.communication.controller;

import android.util.Log;

import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.communication.enums.Request;
import com.example.dkt_group_beta.communication.InfoJsonObject;
import com.example.dkt_group_beta.communication.Wrapper;
import com.example.dkt_group_beta.viewmodel.interfaces.InputHandleInfo;
import com.google.gson.Gson;

public class InfoController {
    private Gson gson;
    private InputHandleInfo handleInfo;

    public InfoController(InputHandleInfo handleInfo){
        this.gson = new Gson();
        this.handleInfo = handleInfo;
        WebsocketClientController.addMessageHandler(this::onMessageReceived);
    }
    public void getGameListFromServer(){
        InfoJsonObject infoJsonObject = new InfoJsonObject(Info.GAME_LIST, -1, null);
        Wrapper wrapper = new Wrapper(infoJsonObject.getClass().getSimpleName(), -1, Request.INFO, infoJsonObject);
        String msg = gson.toJson(wrapper);
        Log.d("DEBUG", "InfoController::getGameListFromServer/ "+msg);
        WebsocketClientController.sendToServer(msg);
    }


    private void onMessageReceived(Object infoObject){
        if (!(infoObject instanceof InfoJsonObject))
            return;
        Log.d("DEBUG", "InfoController::onMessageReceived/ " + ((InfoJsonObject) infoObject).getInfo());
    }

}