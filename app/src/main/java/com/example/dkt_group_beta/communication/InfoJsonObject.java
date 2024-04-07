package com.example.dkt_group_beta.communication;


import com.example.dkt_group_beta.communication.enums.Info;
import com.example.dkt_group_beta.model.GameInfo;

import java.util.List;

public class InfoJsonObject {
    private Info info;
    private List<GameInfo> gameInfoList;

    public InfoJsonObject(Info info, List<GameInfo> gameInfos) {
        this.info = info;
        this.gameInfoList = gameInfos;
    }

    public InfoJsonObject(Info info){
        this(info, null);
    }

    public Info getInfo() {
        return info;
    }

    public List<GameInfo> getGameInfoList() {
        return gameInfoList;
    }

    @Override
    public String toString() {
        return "InfoJsonObject{" +
                "info=" + info +
                ", gameInfos=" + gameInfoList +
                '}';
    }
}
