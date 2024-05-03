package com.example.dkt_group_beta.model;

import lombok.Getter;

public class Game {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private int maxPlayers;
    @Getter
    private int minPlayers;
    @Getter
    private int currentPlayerCount;
    @Getter
    private int currentPlayerTurn;
    private boolean isStarted;
    private boolean isFinished;


    public Game(int id, String name, int maxPlayers, int minPlayers) {
        this.id = id;
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.currentPlayerCount = 0;
        this.currentPlayerTurn = 0;
        this.isStarted = false;
        this.isFinished = false;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setCurrentPlayerCount(int currentPlayerCount) {
        this.currentPlayerCount = currentPlayerCount;
    }

    public void setCurrentPlayerTurn(int currentPlayerTurn) {
        this.currentPlayerTurn = currentPlayerTurn;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
