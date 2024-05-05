package com.example.dkt_group_beta.model;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("testUser", "123");
    }

    @Test
    public void testGetUsername() {
        assertEquals("testUser", player.getUsername());
    }

    @Test
    public void testGetId() {
        assertEquals("123", player.getId());
    }

    @Test
    public void testIsConnected() {
        assertFalse(player.isConnected());
    }

    @Test
    public void testGetGameId() {
        assertEquals(-1, player.getGameId());
    }

    @Test
    public void testSetConnected() {
        player.setConnected(true);
        assertTrue(player.isConnected());
    }

    @Test
    public void testSetGameId() {
        player.setGameId(1);
        assertEquals(1, player.getGameId());
    }

    @Test
    public void testIsReady() {
        assertFalse(player.isReady());
    }

    @Test
    public void testSetReady() {
        player.setReady(true);
        assertTrue(player.isReady());
    }

    @Test
    public void testIsHost() {
        assertFalse(player.isHost());
    }

    @Test
    public void testSetHost() {
        player.setHost(true);
        assertTrue(player.isHost());
    }

    @Test
    public void testIsOnTurn() {
        assertFalse(player.isOnTurn());
    }

    @Test
    public void testSetOnTurn() {
        player.setOnTurn(true);
        assertTrue(player.isOnTurn());
    }

    @Test
    public void testGetCurrentField() {
        assertNull(player.getCurrentField());
    }

    @Test
    public void testSetCurrentField() {
        Field field = new Field(1, "Field1", false);
        player.setCurrentField(field);
        assertEquals(field, player.getCurrentField());
    }

    @Test
    public void testGetMoney() {
        assertEquals(1200, player.getMoney());
    }

    @Test
    public void testSetMoney() {
        player.setMoney(1500);
        assertEquals(1500, player.getMoney());
    }

    @Test
    public void testEquals() {
        Player samePlayer = new Player("testUser", "123");
        assertEquals(player, samePlayer);
    }

    @Test
    public void testNotEquals() {
        Player differentPlayer = new Player("differentUser", "456");
        assertNotEquals(player, differentPlayer);
    }

    @Test
    public void testHashCode() {
        Player samePlayer = new Player("testUser", "123");
        assertEquals(player.hashCode(), samePlayer.hashCode());
    }
}
