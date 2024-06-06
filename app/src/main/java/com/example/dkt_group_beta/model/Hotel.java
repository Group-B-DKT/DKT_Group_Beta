package com.example.dkt_group_beta.model;



public class Hotel extends Building{
    public static final int HOTEL_PRICE = 400;

    public Hotel(int price, int position) {
        super(price, position);
    }
    public static int getHotelPrice(){
        return HOTEL_PRICE;
    }
}
