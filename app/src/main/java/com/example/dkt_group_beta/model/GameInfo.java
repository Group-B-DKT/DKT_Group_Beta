package com.example.dkt_group_beta.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class GameInfo {
    private int id;
    private String name;
    private List<Player> connectedPlayers;
    boolean isStarted;

    public GameInfo(int id, String name, List<Player> connectedPlayers) {
        this.id = id;
        this.name = name;
        this.connectedPlayers = connectedPlayers;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public boolean isStarted() {
        return isStarted;
    }
}
