package com.example.dkt_group_beta.model;

import java.util.List;

public class Game {
    List<Player> players;
    List<Field> fields;

    public Game(List<Player> players, List<Field> fields) {
        this.players = players;
        this.fields = fields;
    }
}
