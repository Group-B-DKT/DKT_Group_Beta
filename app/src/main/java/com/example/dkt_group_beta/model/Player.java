package com.example.dkt_group_beta.model;

import java.io.Serializable;
import java.util.Objects;


public class Player implements Serializable {
    private static final int START_MONEY = 1200;

    private String username;

    private String id;

    private boolean isConnected;

    private int gameId;

    private boolean isReady;

    private boolean isHost;

    private int money;
    private Field currentField;
    private boolean isInGame;
    private boolean isOnTurn;



    public Player(String username, String id) {
        this.money = 1500;
        this.username = username;
        this.id = id;
        this.gameId = -1;
        this.setConnected(false);
        this.isInGame = false;
        this.isConnected = false;
        this.money = START_MONEY;
    }


    public String getUsername() {
        return username;
    }

    public String getId() {
        return id;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public int getGameId() {
        return gameId;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean isOnTurn() {
        return isOnTurn;
    }

    public void setOnTurn(boolean onTurn) {
        isOnTurn = onTurn;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int newMoney) {
        money = newMoney;
    }
    public Field getCurrentField() { return currentField; }

    public void setCurrentField(Field field) { this.currentField = field; }
    public boolean isInGame() {
        return isInGame;
    }

    public void setInGame(boolean inGame) {
        isInGame = inGame;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Player player = (Player) object;
        return Objects.equals(username, player.username) && Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, id, isConnected, gameId, isReady, isHost);
    }
}
