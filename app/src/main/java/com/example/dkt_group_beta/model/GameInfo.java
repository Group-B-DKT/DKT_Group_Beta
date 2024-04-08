package com.example.dkt_group_beta.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class GameInfo {
    private int id;
    private String name;
    private List<String> connectedPlayerNames;

    public GameInfo(int id, String name, List<String> connectedPlayerNames) {
        this.id = id;
        this.name = name;
        this.connectedPlayerNames = connectedPlayerNames;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getConnectedPlayerNames() {
        return connectedPlayerNames;
    }
}
