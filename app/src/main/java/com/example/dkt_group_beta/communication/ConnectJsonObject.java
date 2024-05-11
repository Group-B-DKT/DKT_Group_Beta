package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.model.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ConnectJsonObject {
    @Getter
    private ConnectType connectType;

    private Player player;

    public ConnectJsonObject(ConnectType connectType) {
        this.connectType = connectType;
    }

    public ConnectJsonObject(ConnectType connectType, Player player) {
        this.connectType = connectType;
        this.player = player;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    public Player getPlayer() {
        return player;
    }
}
