package com.example.dkt_group_beta.model;


import java.io.Serializable;

public class Building implements Serializable {
    private int price;
    private int position;


    protected Building(int price, int position){
        this.price = price;
        this.position = position;
    }

    public int getPrice(){
        return price;
    }

    public int getPosition(){
        return  position;
    }

}
