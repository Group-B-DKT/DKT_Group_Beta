package com.example.dkt_group_beta.communication.controller;



import com.example.dkt_group_beta.communication.ConnectJsonObject;
import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.viewmodel.interfaces.InputHandleConnect;

public class ConnectController {
    private final InputHandleConnect handleConnect;

    public ConnectController(InputHandleConnect handleConnect){
        this.handleConnect = handleConnect;
        WebsocketClientController.addMessageHandler(this::onMessageReceived);

    }

    public void removeMessageHandler() {
        WebsocketClientController.removeMessageHandler(this::onMessageReceived);
    }

    private void onMessageReceived(Object connectObject) {
        if (!(connectObject instanceof ConnectJsonObject))
            return;

        ConnectJsonObject connectJsonObject = (ConnectJsonObject) connectObject;
        handleConnect.handleConnect(connectJsonObject.getConnectType(), connectJsonObject.getGameInfo());
    }


}
