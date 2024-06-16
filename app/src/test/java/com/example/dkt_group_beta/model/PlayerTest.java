package com.example.dkt_group_beta.model;

import static org.junit.Assert.*;

import android.widget.ImageView;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

class PlayerTest {
    Game game;
    Player player;
    House house;
    Hotel hotel;
    Field field;
    private static MockedStatic<WebsocketClientController> websocketClientController;


    @BeforeAll
    static void setUpStatic() {

        websocketClientController = Mockito.mockStatic(WebsocketClientController.class);

    }

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", "123");
        field = new Field(200, "TestField", 200, true);
        field.setOwner(player);
        house = new House(0, 10);
        hotel = new Hotel(200, 10);
        List<Player> players = new ArrayList<>();
        players.add(player);
        List<Field> fields = new ArrayList<>();
        fields.add(field);
        game = new Game(players, fields);
    }

    @Test
    void buyHouse() {
        assertTrue(game.buyHouse(player, house, field));
        assertEquals(1300, player.getMoney());
    }
    @Test
    void buyHouseNotEnoughMoney() {
        player.setMoney(50);
        assertFalse(game.buyHouse(player, house, field));
    }
    @Test
    void buyHouseFieldAlreadyHasHotel() {
        field.setHotel(hotel);
        assertFalse(game.buyHouse(player, house, field));
    }
    @Test
    void testBuyHouseFieldHasMaxHouses() {
        field.setOwner(player);
        for (int i = 0; i < house.getMaxAmount(); i++) {
            field.addHouse(house);
        }
        boolean result = game.buyHouse(player, house, field);
        assertFalse(result);
        assertNotNull(field.getHotel());
        assertEquals(0, field.getHouses().size());
    }

    @Test
    void testBuyHotel() {
        assertTrue(game.buyHotel(player, hotel, field));
        assertEquals(1300, player.getMoney());
    }

    @Test
    void testBuyHotelNotEnoughMoney() {
        player.setMoney(50);
        assertFalse(game.buyHotel(player, hotel, field));

    }

    @Test
    void testBuyHotelFieldAlreadyHasHotel() {
        field.setHotel(hotel);
        assertFalse(game.buyHotel(player, new Hotel(200, 10), field));
    }

    @Test
    void testGetUsername() {
        assertEquals("TestPlayer", player.getUsername());
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
        assertEquals(Player.START_MONEY, player.getMoney());
    }

    @Test
    void testSetMoney() {
        player.setMoney(1200);
        assertEquals(1200, player.getMoney());
    }

    @Test
    void testGetColor() {
        player.setColor(0xFF808080);
        assertEquals(0xFF808080, player.getColor());
    }

    @Test
    void testEquals() {
        Player samePlayer = new Player("TestPlayer", "123");
        assertEquals(player, samePlayer);
    }

    @Test
    void testNotEquals() {
        Player differentPlayer = new Player("differentUser", "456");
        assertNotEquals(player, differentPlayer);
    }

    @Test
    void testHashCode() {
        Player samePlayer = new Player("TestPlayer", "123");
        assertEquals(player.hashCode(), samePlayer.hashCode());
    }



    @Test
    void testGetCharacterView() {
        ImageView character = new ImageView(null);
        Player player = new Player("username", "id");
        player.setCharacterView(character);
        assertEquals(character, player.getCharacterView());
    }

    @Test
    void testGetCurrentPosition() {
        Player player = new Player("username", "id");
        player.setCurrentPosition(5);
        assertEquals(5, player.getCurrentPosition());
    }


}
