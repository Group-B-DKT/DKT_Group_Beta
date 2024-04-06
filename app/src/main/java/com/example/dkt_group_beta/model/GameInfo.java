package com.example.dkt_group_beta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class GameInfo {
    private int id;
    private String name;
    private int connectedPlayer;

    public GameInfo(int id, String name, int connectedPlayer) {
        this.id = id;
        this.name = name;
        this.connectedPlayer = connectedPlayer;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getConnectedPlayer() {
        return connectedPlayer;
    }
}
