package com.example.dkt_group_beta.model;

import android.util.Log;

import com.example.dkt_group_beta.activities.GameBoard;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.networking.WebSocketClient;

import java.security.SecureRandom;
import java.util.List;

public class Game {
    public static final int MIN_PLAYER = 2;
    public static String PATH_TO_FIELDLIST = "./assets/fields.csv";
    private SecureRandom random;

    private List<Player> players;
    private List<Field> fields;
    private Player player = WebsocketClientController.getPlayer();

    public Game(List<Player> players, List<Field> fields) {
        this.players = players;
        this.fields = fields;
        this.random = new SecureRandom();
    }

    public int getRandomNumber(int min, int max) {
        if(min > max){
            return -1;
        }
        return random.nextInt(max - min + 1) + min;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field buyField(int index) {

        if (index < 0 || index >= fields.size()) {
            return null;
        }

        Field field = fields.get(index);
        if (field.getOwnable() && player.getMoney() >= field.getPrice()) {
            field.setOwner(player);
            field.setOwnable(false);
            player.setMoney(player.getMoney() - field.getPrice());
            return field;
        } else {
            return null;
        }
    }
    public void updateField(Field field) {
        Field savedField = this.fields.stream()
                .filter(f -> f.getId() == field.getId())
                .findAny().orElse(null);
        if (savedField == null){
            this.fields.add(field);
        }else{
            int index = this.fields.indexOf(savedField);
            this.fields.set(index, field);
        }
    }
    public void updatePlayer(Player player) {
        Player savedPlayer = this.players.stream()
                .filter(f -> f.getId().equals(player.getId()))
                .findAny().orElse(null);
        if (player == null){
            this.players.add(player);
        }else{
            int index = this.players.indexOf(savedPlayer);
            this.players.set(index, player);
        }
    }
}
