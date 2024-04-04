package com.example.dkt_group_beta.communication.controller;

import com.example.dkt_group_beta.networking.WebSocketClient;

public class WebsocketClientController {
    WebSocketClient networkHandler;
    public void connectToServer(){
        // Todo fetch id from device
        String id = "id";
        String username = "username";
        networkHandler = new WebSocketClient("ws://192.168.0.171:8080/websocket-example-handler", id, username);
        networkHandler.connectToServer();
    }

    public void sendToServer(String msg){
        if (networkHandler == null) return;
        networkHandler.sendMessageToServer(msg);
    }
}
