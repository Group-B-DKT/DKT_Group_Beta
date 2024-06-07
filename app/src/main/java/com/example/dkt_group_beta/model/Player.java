package com.example.dkt_group_beta.model;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Objects;


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

    private transient ImageView characterView;


    private int currentPosition;

    private int color;


    public Player(String username, String id) {
        this.username = username;
        this.id = id;
        this.gameId = -1;
        this.isConnected = false;
        this.money = START_MONEY;
        this.currentPosition = 0;
        this.isOnTurn = false;
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

    public ImageView getCharacterView() {
        return characterView;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setCharacterView(ImageView characterView) {
        this.characterView = characterView;
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

    public void copyFrom(Player serverPlayer) {
        this.username = serverPlayer.username;
        this.id = serverPlayer.id;
        this.gameId = serverPlayer.gameId;
        this.isConnected = serverPlayer.isConnected;
        this.money = serverPlayer.money;
        this.currentPosition = serverPlayer.currentPosition;
        this.isOnTurn = serverPlayer.isOnTurn;
        this.color = serverPlayer.color;
        this.isHost = serverPlayer.isHost;
    }
}
