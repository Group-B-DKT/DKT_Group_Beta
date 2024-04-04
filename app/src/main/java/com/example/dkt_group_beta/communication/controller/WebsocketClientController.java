package com.example.dkt_group_beta.communication.controller;

import com.example.dkt_group_beta.networking.WebSocketClient;

public class WebsocketClientController {
    private static WebSocketClient networkHandler;
    public static void connectToServer(String uri, String id, String username){
        networkHandler = new WebSocketClient(uri, id, username);
        networkHandler.connectToServer();
    }

    public static void sendToServer(String msg){
        if (networkHandler == null) return;
        networkHandler.sendMessageToServer(msg);
    }
}
