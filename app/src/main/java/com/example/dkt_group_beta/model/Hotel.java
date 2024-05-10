package com.example.dkt_group_beta.model;



public class Hotel extends Building{
    public static final int HOTEL_PRICE = 400;
    private int MAXAMOUNT;
    public Hotel(int price, Player owner, int position, Field field) {
        super(price, owner, position, field);
    }
}
