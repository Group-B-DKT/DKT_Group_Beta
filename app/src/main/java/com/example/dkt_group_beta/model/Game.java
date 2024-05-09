package com.example.dkt_group_beta.model;

import java.security.SecureRandom;
import java.util.List;

public class Game {
    public static final int MIN_PLAYER = 2;
    private SecureRandom random;
    private List<Player> players;
    private List<Field> fields;

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
}
