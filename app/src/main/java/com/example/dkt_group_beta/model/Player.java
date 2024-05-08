package com.example.dkt_group_beta.model;

import android.graphics.Color;

import com.example.dkt_group_beta.R;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;


public class Player implements Serializable {
    private static final int START_MONEY = 12000005;

    private String username;

    private String id;

    private boolean isConnected;

    private int gameId;

    private boolean isReady;

    private boolean isHost;

    private boolean isOnTurn;

    private Field currentField;

    private int money;

    private int color;


    public Player(String username, String id) {
        this.setColor(15);
        this.username = username;
        this.id = id;
        this.gameId = -1;
        this.isConnected = false;
        this.money = START_MONEY + 1000000;
        this.isOnTurn = false;

        // Temporary delete
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        this.color = Color.rgb(r,g,b);
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

    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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

    @Override
    public String toString() {
        return "Player{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", isConnected=" + isConnected +
                ", gameId=" + gameId +
                ", isReady=" + isReady +
                ", isHost=" + isHost +
                ", isOnTurn=" + isOnTurn +
                ", currentField=" + currentField +
                ", money=" + money +
                ", color=" + color +
                '}';
    }
}
