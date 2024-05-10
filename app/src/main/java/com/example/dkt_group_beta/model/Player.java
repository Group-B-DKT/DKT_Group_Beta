package com.example.dkt_group_beta.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;


public class Player implements Serializable {
    public static final int START_MONEY = 1500;

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
        this.username = username;
        this.id = id;
        this.gameId = -1;
        this.isConnected = false;
        this.money = START_MONEY;
        this.isOnTurn = false;

        // Todo Temporary
        this.color = getRandomColor();
    }

    // Todo Temporary delete
    public int getRandomColor() {
        Random random = new Random();
        int[] colors = {
                0xFFC0C0C0, // Silber
                0xFF800000, // Dunkelrot
                0xFF008000, // Dunkelgrün
                0xFF808000, // Oliv
                0xFF000080, // Dunkelblau
                0xFF800080, // Lila
                0xFF008080, // Cyan
                0xFF000000, // Schwarz
                0xFF808080, // Grau
                0xFFFFFFFF  // Weiß
        };
        return colors[random.nextInt(colors.length)];
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
        this.isOnTurn = onTurn;
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
}
