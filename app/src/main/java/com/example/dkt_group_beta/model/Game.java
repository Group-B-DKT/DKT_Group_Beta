package com.example.dkt_group_beta.model;

import java.util.List;

public class Game {
    public static int MIN_PLAYER = 2;
    private List<Player> players;
    private List<Field> fields;

    public Game(List<Player> players, List<Field> fields) {
        this.players = players;
        this.fields = fields;
    }
}
