package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

 class GameTest {

    private List<Player> players;
    private List<Field> fields;
    private Game game;

    @BeforeEach
    void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Player1", "1"));
        players.add(new Player("Player2", "2"));

        fields = new ArrayList<>();
        fields.add(new Field(1, "Field1", true));
        fields.add(new Field(2, "Field2", true));

        game = new Game(players, fields);
    }

    @Test
    void testGameInitialization() {
        assertEquals(2, game.getPlayers().size());
        assertEquals(2, game.getFields().size());
    }

    @Test
    void testGetOwnedFields() {
        Player player = new Player("Player1", "1");
        player.setMoney(4000);
        game.buyField(player, new Field(1, "Field1", true));
        List<Field> ownedFields = game.getOwnedFields(player);
        assertEquals(1, ownedFields.size());
        assertEquals("Field1", ownedFields.get(0).getName());
    }
}
