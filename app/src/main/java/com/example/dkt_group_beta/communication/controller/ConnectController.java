package com.example.dkt_group_beta.communication.controller;


import android.util.Log;

import com.example.dkt_group_beta.communication.ConnectJsonObject;
import com.example.dkt_group_beta.communication.InfoJsonObject;
import com.example.dkt_group_beta.viewmodel.interfaces.InputHandleConnect;
import com.google.gson.Gson;

public class ConnectController {
    private Gson gson;
    private final InputHandleConnect handleConnect;

    public ConnectController(InputHandleConnect handleConnect){
        this.handleConnect = handleConnect;
        WebsocketClientController.addMessageHandler(this::onMessageReceived);
    }

    private void onMessageReceived(Object connectObject) {
        if (!(connectObject instanceof ConnectJsonObject))
            return;

        handleConnect.onConnectionEstablished();
    }


}
