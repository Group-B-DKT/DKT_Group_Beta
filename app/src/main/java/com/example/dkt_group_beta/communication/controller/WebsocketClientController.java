package com.example.dkt_group_beta.communication.controller;

import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.networking.WebSocketClient;
import com.example.dkt_group_beta.networking.WebSocketMessageHandler;

public class WebsocketClientController {
    private static WebSocketClient networkHandler;

    private WebsocketClientController() {}
    public static void connectToServer(String uri, String id, String username){
        networkHandler = new WebSocketClient(uri, id, username);
        networkHandler.connectToServer();
    }

    public static void sendToServer(String msg){
        if (networkHandler == null) return;
        networkHandler.sendMessageToServer(msg);
    }

    public static void addMessageHandler(WebSocketMessageHandler<Object> messageHandler){
        networkHandler.addMessageHandler(messageHandler);
    }

    public static void notifyMessageHandler(Object jsonObject){
        networkHandler.notifyMessageHandler(jsonObject);
    }

    public static void setPlayerConnected(int gameId){
        networkHandler.connectToGame(gameId);
    }

    public static int getConnectedGameId(){
        return networkHandler.getConnectedGameId();
    }

    public static boolean isConnected(){
        return networkHandler.isConnected();
    }

    public static Player getPlayer(){return networkHandler.getPlayer();}
}
