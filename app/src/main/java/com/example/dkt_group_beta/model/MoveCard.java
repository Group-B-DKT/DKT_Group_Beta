package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;

public class MoveCard extends Card {
    private int fieldID;

    public MoveCard(int id, int amount, CardType type, int fieldID, String imageResource) {
        super(id, amount, type, imageResource);
        this.fieldID = fieldID;
    }
    @Override
    public void doActionOfCard(GameBoardViewModel gameBoardViewModel) {
        gameBoardViewModel.moveForCard(this.fieldID, this.getAmount());
    }

    public int getFieldID() {
        return fieldID;
    }

    public void setFieldID(int fieldID) {
        this.fieldID = fieldID;
    }
}