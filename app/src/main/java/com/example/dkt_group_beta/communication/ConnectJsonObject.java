package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.ConnectType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class ConnectJsonObject {
    @Getter
    private ConnectType connectType;
    @Getter
    private String playerId;
    @Getter
    private String username;

    public ConnectJsonObject(ConnectType connectType) {
        this.connectType = connectType;
    }

    public ConnectJsonObject(ConnectType connectType, String playerId, String username) {
        this.connectType = connectType;
        this.playerId = playerId;
        this.username = username;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getUsername() {
        return username;
    }
}
