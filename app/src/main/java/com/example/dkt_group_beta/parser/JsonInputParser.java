package com.example.dkt_group_beta.parser;

import android.util.Log;

import com.example.dkt_group_beta.communication.Wrapper;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.communication.utilities.WrapperHelper;
import com.example.dkt_group_beta.parser.interfaces.InputParser;
import com.google.gson.Gson;

public class JsonInputParser implements InputParser {
    private static Gson gson = new Gson();

    public void parseInput(String clientMsg) {
        Wrapper wrapper = gson.fromJson(clientMsg, Wrapper.class);
        Log.d("DEBUG", "JsonInputParser::parseInput/ " + wrapper);
        if (wrapper.getGameId() != WebsocketClientController.getConnectedGameId()){
            WebsocketClientController.setPlayerConnected(wrapper.getGameId());
        }
        WebsocketClientController.notifyMessageHandler(WrapperHelper.getInstanceFromWrapper(wrapper));
    }
}
