package com.example.dkt_group_beta.model;

import lombok.Getter;

public class House extends Building{
    @Getter
    private static final int HOUSE_PRICE = 200;
    @Getter
    private static final int MAX_AMOUNT = 4;

    public House(int price, Player owner, int position, Field field) {
        super(price, owner, position, field);
    }

    public static int getHousePrice(){
        return HOUSE_PRICE;
    }

    public int getMaxAmount(){
        return MAX_AMOUNT;
    }
}
