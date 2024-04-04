package com.example.dkt_group_beta.parser;

import com.example.dkt_group_beta.networking.WebSocketMessageHandler;
import com.example.dkt_group_beta.parser.interfaces.InputParser;
import com.google.gson.Gson;


public class JsonInputParser implements InputParser {
    private static Gson gson = new Gson();

    public JsonInputParser(){

    }
    public void parseInput(String client_msg) {
    }
}
