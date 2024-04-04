package com.example.dkt_group_beta.communication.controller;

import android.util.Log;

import com.example.dkt_group_beta.networking.WebSocketClient;
import com.example.dkt_group_beta.networking.WebSocketMessageHandler;

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

    public static void addMessageHandler(WebSocketMessageHandler<String> messageHandler){
        networkHandler.addMessageHandler(messageHandler);
    }
}
