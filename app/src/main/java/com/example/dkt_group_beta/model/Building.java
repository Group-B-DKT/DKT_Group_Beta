package com.example.dkt_group_beta.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Building {
    @Getter
    private int price;
    @Setter
    @Getter
    private Player owner;
    @Getter
    private int position;

    public void placeBuilding(){
        //TODO: places the building on the gameboard
    }

    public int getPrice(){
        return price;
    }

    public int getPosition(){
        return  position;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public Player getOwner(){
        return owner;
    }
}
