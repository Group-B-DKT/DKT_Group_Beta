package com.example.dkt_group_beta.model;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("testUser", "123");
    }

    @Test
    void testGetUsername() {
        assertEquals("testUser", player.getUsername());
    }

    @Test
    void testGetId() {
        assertEquals("123", player.getId());
    }

    @Test
    void testIsConnected() {
        assertFalse(player.isConnected());
    }

    @Test
    void testGetGameId() {
        assertEquals(-1, player.getGameId());
    }

    @Test
    void testSetConnected() {
        player.setConnected(true);
        assertTrue(player.isConnected());
    }

    @Test
    void testSetGameId() {
        player.setGameId(1);
        assertEquals(1, player.getGameId());
    }

    @Test
    void testIsReady() {
        assertFalse(player.isReady());
    }

    @Test
    void testSetReady() {
        player.setReady(true);
        assertTrue(player.isReady());
    }

    @Test
    void testIsHost() {
        assertFalse(player.isHost());
    }

    @Test
    void testSetHost() {
        player.setHost(true);
        assertTrue(player.isHost());
    }

    @Test
    void testIsOnTurn() {
        assertFalse(player.isOnTurn());
    }

    @Test
    void testSetOnTurn() {
        player.setOnTurn(true);
        assertTrue(player.isOnTurn());
    }

    @Test
    void testGetCurrentField() {
        assertNull(player.getCurrentField());
    }

    @Test
    void testSetCurrentField() {
        Field field = new Field(1, "Field1", false);
        player.setCurrentField(field);
        assertEquals(field, player.getCurrentField());
    }

    @Test
    void testGetMoney() {
        assertEquals(1200, player.getPlayerMoney());
    }

    @Test
    void testSetMoney() {
        player.setPlayerMoney(1500);
        assertEquals(1500, player.getPlayerMoney());
    }

    @Test
    void testEquals() {
        Player samePlayer = new Player("testUser", "123");
        assertEquals(player, samePlayer);
    }

    @Test
    void testNotEquals() {
        Player differentPlayer = new Player("differentUser", "456");
        assertNotEquals(player, differentPlayer);
    }

    @Test
    void testHashCode() {
        Player samePlayer = new Player("testUser", "123");
        assertEquals(player.hashCode(), samePlayer.hashCode());
    }
}
