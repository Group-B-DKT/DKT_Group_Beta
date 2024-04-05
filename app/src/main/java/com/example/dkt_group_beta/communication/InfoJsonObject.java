package com.example.dkt_group_beta.communication;


import com.example.dkt_group_beta.communication.enums.Info;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class InfoJsonObject {
    private Info info;
    private int gameId;
    private Map<Integer, Integer> gameInfo;

    public InfoJsonObject(Info info, int gameId, Map<Integer, Integer> gameInfo) {
        this.info = info;
        this.gameId = gameId;
        this.gameInfo = gameInfo;
    }

    public Info getInfo() {
        return info;
    }

    public int getGameId() {
        return gameId;
    }

    public Map<Integer, Integer> getGameInfo() {
        return gameInfo;
    }
}
