package com.example.dkt_group_beta.model;

import static org.junit.Assert.*;

import android.widget.ImageView;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.enums.CardType;

import org.junit.jupiter.api.AfterAll;
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
        websocketClientController.when(WebsocketClientController::getPlayer).thenReturn(new Player("PX", "IDX"));
    }

    @AfterAll
    public static void close() {
        websocketClientController.close();
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
        game.buyHouse(player, house, field);
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

    @Test
    void testCopyFrom() {
        Player player2 = new Player("P1", "ID1");
        Player playerCopy = new Player("P2", "ID2");
        playerCopy.setMoney(400);

        assertEquals(Player.START_MONEY, player2.getMoney());

        player2.copyFrom(playerCopy);

        assertEquals(playerCopy.getMoney(), playerCopy.getMoney());
    }

    @Test
    void testSetDefaultValues() {
        Player player2 = new Player("P1", "ID1");
        player2.setMoney(400);
        player2.setDefaulValues();
        assertEquals(Player.START_MONEY, player2.getMoney());
    }

    @Test
    void testHasCheated(){
        Player player = new Player("P1", "ID1");
        assertFalse(player.isHasCheated());
        player.setHasCheated(true);
        assertTrue(player.isHasCheated());
    }

    @Test
    void testGetJokerCardsInitiallyEmpty() {
        assertTrue(player.getJokerCards().isEmpty());
    }

    @Test
    void testSetJokerCards() {
        ArrayList<JokerCard> jokerCards = new ArrayList<>();
        jokerCards.add(new JokerCard(1, 0, CardType.JOKER, "joker1.png"));
        jokerCards.add(new JokerCard(2, 0, CardType.JOKER, "joker2.png"));

        player.setJokerCards(jokerCards);

        assertEquals(jokerCards, player.getJokerCards());
    }

    @Test
    void testHasJokerCardInitiallyFalse() {
        assertFalse(player.hasJokerCard());
    }

    @Test
    void testHasJokerCardAfterAdding() {
        player.addJokerCard(new JokerCard(1, 0, CardType.JOKER, "joker.png"));
        assertTrue(player.hasJokerCard());
    }

    @Test
    void testGetJokerAmount() {
        player.addJokerCard(new JokerCard(1, 0, CardType.JOKER, "joker.png"));
        player.addJokerCard(new JokerCard(2, 0, CardType.JOKER, "joker.png"));

        assertEquals(2, player.getJokerAmount());
    }

    @Test
    void testAddJokerCard() {
        JokerCard jokerCard = new JokerCard(1, 0, CardType.JOKER, "joker.png");
        player.addJokerCard(jokerCard);

        assertTrue(player.getJokerCards().contains(jokerCard));
    }

    @Test
    void testRemoveJokerCard() {
        JokerCard jokerCard = new JokerCard(1, 0, CardType.JOKER, "joker.png");
        player.addJokerCard(jokerCard);
        player.removeJokerCard();

        assertFalse(player.getJokerCards().contains(jokerCard));
    }
    @Test
    void testSetRoundsToSkip(){
        player.setRoundsToSkip(3);
        assertEquals(3, player.getRoundsToSkip());
    }

}
