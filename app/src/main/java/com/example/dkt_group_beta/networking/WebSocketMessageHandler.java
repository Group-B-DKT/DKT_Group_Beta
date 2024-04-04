package com.example.dkt_group_beta.networking;

public interface WebSocketMessageHandler<T> {

    void onMessageReceived(T message);
    
}
