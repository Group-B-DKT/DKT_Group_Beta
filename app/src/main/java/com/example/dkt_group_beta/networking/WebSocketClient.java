package com.example.dkt_group_beta.networking;

import android.util.Log;

import com.example.dkt_group_beta.parser.JsonInputParser;
import com.example.dkt_group_beta.parser.interfaces.InputParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketClient {
    private final String WEBSOCKET_URI = System.getenv("ws://192.168.0.171:8080/websocket-example-handler");
    private InputParser inputParser;
    private WebSocket webSocket;



    public WebSocketClient(){
        this.inputParser = new JsonInputParser();
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
}
