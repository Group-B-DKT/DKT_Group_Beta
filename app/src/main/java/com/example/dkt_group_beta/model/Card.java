package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.model.enums.CardType;
import com.example.dkt_group_beta.viewmodel.GameBoardViewModel;

import java.io.Serializable;

public abstract class Card implements Serializable {
    private int id;
    private int amount;
    private String imageResource;
    private CardType type;
    private GameBoardViewModel gameBoardViewModel;

    public Card(int id, int amount, CardType type,String imageResource) {
        this.id = id;
        this.amount = amount;
        this.imageResource = imageResource;
        this.type = type;
    }

    public void setGameBoardViewModel(GameBoardViewModel gameBoardViewModel) {
        this.gameBoardViewModel = gameBoardViewModel;
    }

    public GameBoardViewModel getGameBoardViewModel() {
        return gameBoardViewModel;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public String getImageResource() {
        return imageResource;
    }

     public abstract void doActionOfCard();
}
