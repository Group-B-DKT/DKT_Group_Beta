package com.example.dkt_group_beta.model;

import lombok.Getter;

public class Hotel extends Building{
    @Getter
    private static int HOTEL_PRICE;
    @Getter
    private int MAX_AMOUNT;
    public Hotel(int price, Player owner, int position) {
        super(price, owner, position);
    }
}
