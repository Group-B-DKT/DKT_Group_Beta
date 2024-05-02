package com.example.dkt_group_beta.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.example.dkt_group_beta.activities.interfaces.GameLobbyAction;
import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.Player;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class GameModelTest {

    private GameModel game;



    @Test
    public void testBuyField() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(0, "100", 500, true));
        fields.add(new Field(1, "300", 200,true));
        fields.add(new Field(2, "500", 300, true));
        Player player = new Player("TestPlayer", "2");
        game = new GameModel(fields,player);

        Field boughtField = game.buyField(1);
        System.out.println(boughtField);

        assertNotNull(boughtField);
        assertEquals(player, boughtField.getOwner());
    }

    @Test
    public void testifMoneyIsReduced() {
        List<Field> fields = new ArrayList<>();
        fields.add(new Field(0, "100", 500, true));
        fields.add(new Field(1, "300", 200,true));
        fields.add(new Field(2, "500", 300, true));
        Player player = new Player("TestPlayer", "2");
        game = new GameModel(fields,player);

        Field boughtField = game.buyField(1);
        System.out.println(boughtField);

        assertNotNull(boughtField);
        assertEquals(player.getMoney(), boughtField.getOwner().getMoney());
    }


}
