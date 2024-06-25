package com.example.dkt_group_beta.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class JokerCardTest {
    private JokerCard card;
    private GameBoardViewModel gameBoardViewModel;

    @BeforeEach
    public void setUp() {
        gameBoardViewModel = Mockito.mock(GameBoardViewModel.class);
        card = new JokerCard(2, 0, CardType.JOKER,"risikoCardJoker.png");
    }

    @Test
    public void testGetId() {
        assertEquals(2, card.getId());
    }

    @Test
    public void testSetId() {
        card.setId(14);
        assertEquals(14, card.getId());
    }

    @Test
    public void testGetAmount() {
        assertEquals(0, card.getAmount());
    }

    @Test
    public void testSetAmount() {
        card.setAmount(200);
        assertEquals(200, card.getAmount());
    }

    @Test
    public void testGetImageResource() {
        assertEquals("risikoCardJoker.png", card.getImageResource());
    }

    @Test
    public void testSetImageResource() {
        card.setImageResource("newRisikoCard.png");
        assertEquals("newRisikoCard.png", card.getImageResource());
    }

    @Test
    public void testGetType() {
        assertEquals(CardType.JOKER, card.getType());
    }

    @Test
    public void testSetType() {
        card.setType(CardType.PAY);
        assertEquals(CardType.PAY, card.getType());
    }

    @Test
    public void testDoActionOfCard() {
        card.doActionOfCard(gameBoardViewModel);
        Mockito.verify(gameBoardViewModel, Mockito.times(1)).addJokerCard(card);
    }
}
