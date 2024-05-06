package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    House house;
    Hotel hotel;
    Field field;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", "1");
        field = new Field(200, "TestField", 200, true);
        field.setOwner(player);
        house = new House(0, null, 1, field);
        hotel = new Hotel(200, null, 1, field);
    }

    @Test
    void buyHouse() {
        assertTrue(player.buyHouse(house));
        assertEquals(1300, player.getPlayerMoney());
        assertEquals(player, house.getOwner());
        assertEquals(field, house.getField());

    }

    @Test
    void buyHouseNotEnoughMoney() {
        player.setPlayerMoney(50);
        assertFalse(player.buyHouse(house));
    }
    @Test
    void buyHouseFieldAlreadyHasHotel() {
        field.setHotel(hotel);
        assertFalse(player.buyHouse(house));
    }

    @Test
    void testBuyHotel() {
        assertTrue(player.buyHotel(hotel));
        assertEquals(1300, player.getPlayerMoney());
        assertEquals(player, hotel.getOwner());
    }

    @Test
    void testBuyHotelNotEnoughMoney() {
        player.setPlayerMoney(50);
        assertFalse(player.buyHotel(hotel));

    }

    @Test
    void testBuyHotelFieldAlreadyHasHotel() {
        field.setHotel(hotel);
        assertFalse(player.buyHotel(new Hotel(200, null, 1, new Field(200, "AnotherField", 500, true))));
    }

}