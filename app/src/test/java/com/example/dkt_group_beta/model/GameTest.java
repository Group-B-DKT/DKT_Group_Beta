package com.example.dkt_group_beta.model;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

class GameTest {

    private List<Player> players;
    private List<Field> fields;
    private Game game;

    private static MockedStatic<WebsocketClientController> websocketClientController;

    @BeforeEach
    void setUp() {
        websocketClientController = Mockito.mockStatic(WebsocketClientController.class);
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
    public void testBuyField() {
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
    public void testifMoneyIsReduced() {
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

}
