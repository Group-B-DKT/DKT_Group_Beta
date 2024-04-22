package com.example.dkt_group_beta.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class House extends Building{
    @Getter
    private static final int HOUSE_PRICE = 200;
    @Getter
    private static final int MAX_AMOUNT = 4;

    public House(int HOUSE_PRICE, Player owner, int position) {
        super(HOUSE_PRICE, owner, position);
    }

    public int getHousePrice(){
        return HOUSE_PRICE;
    }

    public int getMaxAmount(){
        return MAX_AMOUNT;
    }
}
