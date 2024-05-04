package com.example.dkt_group_beta.model;

import android.content.Context;

import com.example.dkt_group_beta.io.CSVReader;

import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Field {

    @Getter
    private int id;
    private String name;
    private int price = 0;
    private Player owner;
    private final boolean ownable;
    private List<House> houses = new ArrayList<>();
    private Hotel hotel;

    public Field(int id, String name, boolean ownable) {
        this.id = id;
        this.name = name;
        this.ownable = ownable;
    }
    public Field(int id, String name, int price, boolean ownable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownable = ownable;
    }


    public String getName() {
        return name;
    }

    public String enterField(Player player) {
        return player.getId();
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public boolean getOwnable() {
        return ownable;
    }

    public Player getOwner() {
        return owner;
    }

    public static ArrayList<Field> loadFields(Context context) throws IOException {
        return CSVReader.readFields(context);
    }
    public boolean hasHotel(){
        return hotel != null;
    }
    public void setHotel(Hotel hotel){
        this.hotel = hotel;
    }
    public void addHouse(House house){
        houses.add(house);
    }
    public int getNumberOfHouses(){
        return houses.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return id == field.id && price == field.price && ownable == field.ownable && Objects.equals(name, field.name) && Objects.equals(owner, field.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, owner, ownable);
    }
}
