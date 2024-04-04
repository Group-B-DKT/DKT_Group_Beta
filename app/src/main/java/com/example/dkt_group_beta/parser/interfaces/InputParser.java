package com.example.dkt_group_beta.parser.interfaces;


import com.example.dkt_group_beta.networking.WebSocketMessageHandler;

public interface InputParser {
    void parseInput(String serverMsg, WebSocketMessageHandler<String> messageHandler);
}
