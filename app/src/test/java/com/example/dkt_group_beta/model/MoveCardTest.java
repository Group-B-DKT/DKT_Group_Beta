package com.example.dkt_group_beta.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class MoveCardTest {
    private MoveCard card;
    private GameBoardViewModel gameBoardViewModel;

    @BeforeEach
    public void setUp() {
        gameBoardViewModel = Mockito.mock(GameBoardViewModel.class);
        card = new MoveCard(1, 200, CardType.MOVE, 25,"risikoCardMove.png");
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
        assertEquals(200, card.getAmount());
    }

    @Test
    public void testSetAmount() {
        card.setAmount(0);
        assertEquals(0, card.getAmount());
    }

    @Test
    public void testGetImageResource() {
        assertEquals("risikoCardMove.png", card.getImageResource());
    }

    @Test
    public void testSetImageResource() {
        card.setImageResource("newRisikoCard.png");
        assertEquals("newRisikoCard.png", card.getImageResource());
    }

    @Test
    public void testGetType() {
        assertEquals(CardType.MOVE, card.getType());
    }

    @Test
    public void testSetType() {
        card.setType(CardType.PAY);
        assertEquals(CardType.PAY, card.getType());
    }

    @Test
    public void tesGetFieldID(){
        assertEquals(25, card.getFieldID());
    }
    @Test
    public void testSetFieldID(){
        card.setFieldID(7);
        assertEquals(7, card.getFieldID());
    }
    @Test
    public void testDoActionOfCard() {
        card.doActionOfCard(gameBoardViewModel);
        Mockito.verify(gameBoardViewModel, Mockito.times(1)).moveForCard(25, 200);
    }
}
