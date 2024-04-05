package com.example.dkt_group_beta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Player {
    @Getter
    private String username;
    @Getter
    private String id;
    @Getter
    private boolean isConnected;
    @Getter
    private int gameId;


    public Player(String username, String id) {
        this.username = username;
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }
}
