package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.model.enums.CardType;

public class MoveCard extends Card {
    private  int fieldID;

    public MoveCard(int id, int amount, CardType type, int fieldID, String imageResource) {
        super(id, amount, type, imageResource);
        this.fieldID = fieldID;
    }
    @Override
    public void doActionOfCard() {
        getGameBoardViewModel().moveForCard(fieldID,getAmount());
    }
}
