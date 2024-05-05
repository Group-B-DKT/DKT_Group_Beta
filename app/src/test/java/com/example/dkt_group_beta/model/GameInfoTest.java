package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameInfoTest {

    private List<Player> players;
    private GameInfo gameInfo;

    @BeforeEach
    public void setUp() {
        players = new ArrayList<>();
        players.add(new Player("Player1", "1"));
        players.add(new Player("Player2", "2"));

        gameInfo = new GameInfo(1, "Test Game", players);
    }

    @Test
    public void testGetId() {
        assertEquals(1, gameInfo.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Game", gameInfo.getName());
    }

    @Test
    public void testGetConnectedPlayers() {
        assertEquals(2, gameInfo.getConnectedPlayers().size());
    }

    @Test
    public void testIsStarted() {
        assertEquals(false, gameInfo.isStarted());
    }
}
