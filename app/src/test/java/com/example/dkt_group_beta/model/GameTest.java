package com.example.dkt_group_beta.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.model.enums.FieldType;

class GameTest {

    private List<Player> players;
    private List<Field> fields;
    private Game game;

    private static MockedStatic<WebsocketClientController> websocketClientController;


    @BeforeAll
    static void setUpStatic() {

        websocketClientController = Mockito.mockStatic(WebsocketClientController.class);

    }

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
    void testSetMoney(){
        Player player = new Player("player", "100");
        websocketClientController.when(WebsocketClientController::getPlayer).thenReturn(player);
        Game mygame = new Game(null, null);
        mygame.setMoney(200);
        assertEquals(Player.START_MONEY + 200, player.getMoney());
    }

    @Test
    void testGameInitialization() {
        assertEquals(2, game.getPlayers().size());
        assertEquals(2, game.getFields().size());
    }

    @Test
    void testBuyField() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(0, "100", 500, true));
        fields.add(new Field(1, "300", 200,true));
        fields.add(new Field(2, "500", 300, true));
        List<Player> players = new ArrayList<>();
        Player player = new Player("TestPlayer", "2");
        players.add(player);
        websocketClientController.when(WebsocketClientController::getPlayer).thenReturn(player);
        game = new Game(players,fields);


        Field boughtField = game.buyField(1);

        assertNotNull(boughtField);
        assertEquals(player, boughtField.getOwner());
    }

    @Test
    void testifMoneyIsReduced() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(0, "100", 500, true));
        List<Player> players = new ArrayList<>();
        Player player = new Player("TestPlayer", "2");
        players.add(player);
        websocketClientController.when(WebsocketClientController::getPlayer).thenReturn(player);
        game = new Game(players,fields);

        Field boughtField = game.buyField(0);

        assertNotNull(boughtField);
        assertEquals(Player.START_MONEY-500, player.getMoney());
    }
    @Test
    void testifMoneyIsToLess() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(0, "100", 3000, true));
        List<Player> players = new ArrayList<>();
        Player player = new Player("TestPlayer", "2");
        players.add(player);
        websocketClientController.when(WebsocketClientController::getPlayer).thenReturn(player);
        game = new Game(players,fields);

        Field boughtField = game.buyField(0);

        assertNull(game.buyField(0));

    }
    @Test
    void testBuyFieldSecond() {
        fields.get(0).setOwnable(false);
        assertNull(game.buyField(0));
    }
    @Test
    void testBuyFieldIndexLessThanZero() {
        assertNull(game.buyField(-1));
    }
    @Test
    void testBuyFieldIndexGreaterThanSize() {
        assertNull(game.buyField(fields.size()));
        assertNull(game.buyField(fields.size()+1));
    }


    @Test
    void testUpdateField() {
        Field updateField = new Field(1, "Updated Field", false);

        game.updateField(updateField);
        assertEquals("Updated Field", game.getFields().get(0).getName());
    }

    @Test
    void testUpdateFieldNoField() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(1, "Field1", true));
        fields.add(new Field(2, "Field2", true));
        fields.add(new Field(3, "Field3", true));

        Game game = new Game(players, new ArrayList<>(fields));

        Field updateField = new Field(100, "Updated Field", false);
        game.updateField(updateField);

        assertEquals(4, game.getFields().size());
    }

    @Test
    void testUpdatePlayer() {
        Player updatedPlayer = new Player("New Player","1");
        game.updatePlayer(updatedPlayer);
        assertEquals("New Player", game.getPlayers().get(0).getUsername());

    }
    @Test
    void updatePlayerNoPlayer(){
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player1", "1"));
        players.add(new Player("Player2", "2"));

        Game game = new Game(new ArrayList<>(players), fields);

        Player updatedPlayer = new Player("New Player","10000");
        game.updatePlayer(updatedPlayer);

        assertEquals(3, game.getPlayers().size());
    }

    @Test
    void testUpdateFieldSecond() {
        Field updateField = fields.get(1);
        game.updateField(updateField);
        assertEquals("Field1", game.getFields().get(0).getName());
    }
    @Test
    void testUpdatePlayerSecond() {
        Player updatedPlayer = players.get(1);
        game.updatePlayer(updatedPlayer);
        assertEquals("Player1", game.getPlayers().get(0).getUsername());

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
    void testGetOwnedFieldsEmpty(){
        assertTrue(game.getOwnedFields(null).isEmpty());
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
    @Test
    void testPayTaxes() {
        Player currentPlayer = new Player("CurrentPlayer", "3");
        currentPlayer.setMoney(1000);
        Player fieldOwner = new Player("FieldOwner", "4");
        fieldOwner.setMoney(500);
        Field field = new Field(1, "Field1", 200, true, FieldType.NORMAL, 30);
        field.setOwner(fieldOwner);
        boolean result = game.payTaxes(currentPlayer, field);
        assertTrue(result);
        assertEquals(currentPlayer.getMoney(), 970);
        assertEquals(fieldOwner.getMoney(), 530);

    }

    @Test
    void testPayTaxesNotEnoughMoney() {

        Player currentPlayer = new Player("CurrentPlayer", "3");
        currentPlayer.setMoney(100);
        Player fieldOwner = new Player("FieldOwner", "4");
        fieldOwner.setMoney(500);
        Field field = new Field(1, "Field1", 200, true, FieldType.NORMAL, 150);
        field.setOwner(fieldOwner);
        boolean result = game.payTaxes(currentPlayer, field);
        assertFalse(result);

    }
    @Test
    void testPayTaxesOwnerIsNull() {

        Player currentPlayer = new Player("CurrentPlayer", "3");
        currentPlayer.setMoney(700);
        Player fieldOwner = new Player("FieldOwner", "4");
        fieldOwner.setMoney(500);
        Field field = new Field(1, "Field1", 200, true, FieldType.NORMAL, 150);
        boolean result = game.payTaxes(currentPlayer, field);
        assertFalse(result);

    }
    @Test
    void testPayTaxesIdIsIdentically() {

        Player currentPlayer = new Player("CurrentPlayer", "4");
        currentPlayer.setMoney(700);
        Player fieldOwner = new Player("FieldOwner", "4");
        fieldOwner.setMoney(500);
        Field field = new Field(1, "Field1", 200, true, FieldType.NORMAL, 150);
        field.setOwner(fieldOwner);
        boolean result = game.payTaxes(currentPlayer, field);
        assertFalse(result);

    }
 }
