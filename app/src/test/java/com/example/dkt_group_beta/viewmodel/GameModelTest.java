package com.example.dkt_group_beta.viewmodel;

import static org.junit.Assert.assertEquals;

import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Player;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameModelTest {

    private Field field;
    private Player player;
    private GameModel gameModel;

    @BeforeEach
    public void setUp() {
        field = new Field(2, "Straße", 200, true);
        player = new Player("Max", "1");
        gameModel = new GameModel(player, field);
    }

    @Test
    public void testCalculatePayment() {
        field.setPrice(200);
        int payment = gameModel.calculatePayment(field);
        assertEquals(200, payment);
}

    @Test
    public void testProcessPayment() {
        player.setMoney(1000);
        gameModel.processPayment(player, player, 500);
        assertEquals(500, player.getMoney());
    }


}
