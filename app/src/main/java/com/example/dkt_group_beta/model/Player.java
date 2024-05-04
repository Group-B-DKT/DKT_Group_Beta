package com.example.dkt_group_beta.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Player {

    final static public int STARTINGMONEY = 1500;
    private String username;

    private String id;

    private boolean isConnected;

    private int gameId;

    private int playerMoney = STARTINGMONEY;

    private boolean isReady;

    private boolean isHost;
    private List<Field> ownedFields = new ArrayList<>();

    public Player(String username, String id) {
        this.username = username;
        this.id = id;
        this.gameId = -1;
        this.setConnected(false);
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

    public int getPlayerMoney() {
        return playerMoney;
    }
    public void setPlayerMoney(int playerMoney) {
        this.playerMoney = playerMoney;
    }
    public List<Field> getOwnedFields() {
        return ownedFields;
    }
    public boolean pay(int amount) {
        if (playerMoney > amount) {
            playerMoney -= amount;
            return true;
        }
        return false;
    }
    public boolean buyField(Field field){
        if(pay(field.getPrice())){
            field.setOwner(this);
            ownedFields.add(field);
            return true;
        }
        return false;
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
