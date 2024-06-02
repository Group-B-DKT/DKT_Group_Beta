package com.example.dkt_group_beta.communication;

import com.example.dkt_group_beta.communication.enums.ConnectType;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ConnectJsonObject {
    private ConnectType connectType;

    private Player player;

    private GameInfo gameInfo;

    public ConnectJsonObject(ConnectType connectType) {
        this.connectType = connectType;
    }

    public ConnectJsonObject(ConnectType connectType, Player player) {
        this.connectType = connectType;
        this.player = player;
    }

    public ConnectJsonObject(ConnectType connectType, Player player, GameInfo gameInfo) {
        this.connectType = connectType;
        this.player = player;
        this.gameInfo = gameInfo;
    }

    public ConnectType getConnectType() {
        return connectType;
    }

    public Player getPlayer() {
        return player;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }
}
