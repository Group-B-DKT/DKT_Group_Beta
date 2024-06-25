package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class PayCardTest {

    private PayCard card;
    private GameBoardViewModel gameBoardViewModel;

    @BeforeEach
    public void setUp() {
        gameBoardViewModel = Mockito.mock(GameBoardViewModel.class);
        card = new PayCard(1, 100, CardType.PAY, "risikoCard.png");
    }

    @Test
    public void testGetId() {
        assertEquals(1, card.getId());
    }

    @Test
    public void testSetId() {
        card.setId(2);
        assertEquals(2, card.getId());
    }

    @Test
    public void testGetAmount() {
        assertEquals(100, card.getAmount());
    }

    @Test
    public void testSetAmount() {
        card.setAmount(200);
        assertEquals(200, card.getAmount());
    }

    @Test
    public void testGetImageResource() {
        assertEquals("risikoCard.png", card.getImageResource());
    }

    @Test
    public void testSetImageResource() {
        card.setImageResource("new_image.png");
        assertEquals("new_image.png", card.getImageResource());
    }

    @Test
    public void testGetType() {
        assertEquals(CardType.PAY, card.getType());
    }

    @Test
    public void testSetType() {
        card.setType(CardType.PAY);
        assertEquals(CardType.PAY, card.getType());
    }

    @Test
    public void testDoActionOfCard() {
        card.doActionOfCard(gameBoardViewModel);
        Mockito.verify(gameBoardViewModel, Mockito.times(1)).payForCard(100);
    }
}
