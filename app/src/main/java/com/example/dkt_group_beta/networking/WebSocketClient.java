package com.example.dkt_group_beta.networking;

import android.util.Log;

import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.communication.utilities.ConnectJsonObject;
import com.example.dkt_group_beta.communication.utilities.Wrapper;
import com.example.dkt_group_beta.model.Player;
import com.example.dkt_group_beta.parser.JsonInputParser;
import com.example.dkt_group_beta.parser.interfaces.InputParser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {
    private Gson gson;
    private String WEBSOCKET_URI;
    private InputParser inputParser;
    private WebSocket webSocket;

    private Player player;

    private List<WebSocketListener> messageHandler;



    public WebSocketClient(String websocketUri, String id, String username){
        WEBSOCKET_URI = websocketUri;
        this.inputParser = new JsonInputParser();
        this.messageHandler = new ArrayList<>();
        this.gson = new Gson();
        this.player = new Player(username, id);
    }


    public void connectToServer() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WEBSOCKET_URI)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                ConnectJsonObject connectJsonObject = new ConnectJsonObject(ConnectType.NEW_CONNECT, player.getId(), player.getUsername());
                Wrapper wrapper = new Wrapper(ConnectJsonObject.class.getSimpleName(), -1, com.example.dkt_group_beta.communication.enums.Request.CONNECT, connectJsonObject);
                String msg = gson.toJson(wrapper);
                webSocket.send(msg);
                Log.d("Network", "connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("Network", text);
                inputParser.parseInput(text);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("Network", "Connection closed!");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                messageHandler.onMessageReceived("Connection lost...");
                Log.d("Network", "connection failure" + t.getLocalizedMessage());
            }
        });
    }

    private void addMessageHandler(WebSocketListener messageHandler){
        this.messageHandler.add(messageHandler);
    }

    public void sendMessageToServer(String msg) {
        webSocket.send(msg);
    }
}
