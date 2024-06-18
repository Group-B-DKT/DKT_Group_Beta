package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;

public class PayCard extends Card{

    public PayCard(int id, int amount, CardType type, String imageResource) {
        super(id, amount, type, imageResource);
    }

    @Override
    public void doActionOfCard(GameBoardViewModel gameBoardViewModel) {
        gameBoardViewModel.payForCard(getAmount());
    }
}