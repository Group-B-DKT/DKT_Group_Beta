package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testGetRandomNumber(){
        int min = 2;
        int max = 15;
        int result = game.getRandomNumber(min,max);
        assertTrue(min <= result && result <= max);
    }
    @Test
    void testGetRandomNumberMinBiggerMax(){
        int min = 20;
        int max = 15;
        int result = game.getRandomNumber(min,max);
        assertFalse(min <= result && result <= max);
    }
    @Test
    void testGetRandomNumberEqual(){
        int min = 2;
        int max = 2;
        int result = game.getRandomNumber(min,max);
        assertTrue(min <= result && result <= max);
    }

    @Test
    void testGetOwnedFields(){
        Player player = new Player("User1", "ID1");
        List<Field> fields1 = new ArrayList<>();
        Field field = new Field(1, "Field1", true);
        field.setOwner(player);
        fields1.add(field);
        fields1.add(new Field(2, "Field2", true));
        Game game1 = new Game(Collections.singletonList(player), fields1);
        assertEquals(1, game1.getOwnedFields(player).size());
    }

    @Test
    void testGetPlayerById(){

        Player player = new Player("User1", "101");
        players.add(player);
        assertEquals(player, game.getPlayerById("101"));
    }

    @Test
    void testGetPlayerByIdNull(){
        assertEquals(null, game.getPlayerById("10000"));
    }

    @Test
    void testSetPlayerTurn(){
        players.get(0).setOnTurn(false);
        players.get(1).setOnTurn(true);
        game.setPlayerTurn(players.get(0).getId());
        assertTrue(players.get(0).isOnTurn());
        assertFalse(players.get(1).isOnTurn());
    }


 }
