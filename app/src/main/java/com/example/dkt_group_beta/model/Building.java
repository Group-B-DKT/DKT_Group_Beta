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
    private Field field;

    public void placeBuilding(){
        //TODO: places the building on the gameboard
    }

    public int getPrice(){
        return price;
    }

    public int getPosition(){
        return  position;
    }
    public Field getField() {
        return field;
    }
    public void setField(Field field){
        this.field = field;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public Player getOwner(){
        return owner;
    }
}
