package com.example.dkt_group_beta.networking;

import android.content.Context;
import android.util.Log;

import com.example.dkt_group_beta.R;
import com.example.dkt_group_beta.parser.JsonInputParser;
import com.example.dkt_group_beta.parser.interfaces.InputParser;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {
    private String WEBSOCKET_URI;
    private InputParser inputParser;
    private WebSocket webSocket;

    private List<WebSocketListener> messageHandler;



    public WebSocketClient(String websocketUri){
        WEBSOCKET_URI = websocketUri;
        this.inputParser = new JsonInputParser();
        this.messageHandler = new ArrayList<>();
    }


    public void connectToServer(WebSocketMessageHandler<String> messageHandler) {
        if (messageHandler == null)
            throw new IllegalArgumentException("messageHandler is required");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(WEBSOCKET_URI)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("Network", "connected");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("Network", text);
                inputParser.parseInput(text, messageHandler);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("Network", "Connection closed!");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                messageHandler.onMessageReceived("Connection lost...");
                Log.d("Network", "connection failure");
            }
        });
    }

    private void addMessageHandler(WebSocketListener messageHandler){
        this.messageHandler.add(messageHandler);
    }
}
