package com.example.dkt_group_beta.parser;

import android.util.Log;

import com.example.dkt_group_beta.communication.Wrapper;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.utilities.WrapperHelper;
import com.example.dkt_group_beta.networking.WebSocketClient;
import com.example.dkt_group_beta.networking.WebSocketMessageHandler;
import com.example.dkt_group_beta.parser.interfaces.InputParser;
import com.google.gson.Gson;


public class JsonInputParser implements InputParser {
    private static Gson gson = new Gson();

    public JsonInputParser(){

    }
    public void parseInput(String client_msg) {
        Wrapper wrapper = gson.fromJson(client_msg, Wrapper.class);
        Log.d("DEBUG", "JsonInputParser::parseInput/ " + wrapper);
        WebsocketClientController.notifyMessageHandler(WrapperHelper.getInstanceFromWrapper(wrapper));
    }
}
