package com.example.dkt_group_beta.model;

import android.content.Context;

import com.example.dkt_group_beta.io.CSVReader;
import com.example.dkt_group_beta.model.enums.FieldType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Field implements Serializable {

    private int id;
    private String name;
    private int price = 0;
    private Player owner;
    private boolean ownable;
    private Hotel hotel;

    private List<House> houses;

    private FieldType fieldType;
    private int rent;

    public Field(int id, String name, int price, boolean ownable, FieldType fieldType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownable = ownable;
        this.fieldType = fieldType;
        this.houses = new ArrayList<>();
    }
    public Field(int id, String name, int price, boolean ownable, FieldType fieldType, int rent) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownable = ownable;
        this.fieldType = fieldType;
        this.rent = rent;
    }

    public Field(int id, String name, boolean ownable) {
        this(id, name, -1,ownable, FieldType.NORMAL);
    }
    public Field(int id, String name, int price, boolean ownable) {
        this(id, name, price, ownable, FieldType.NORMAL);
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
    public void setId(int newId) { id = newId;}

    public int getPrice() {
        return price;
    }
    public void setPrice(int newPrice) {
        price = newPrice;
    }

    public void setOwner(Player player) {
        owner = player;
    }

    public boolean getOwnable() {
        return ownable;
    }
    public void setOwnable(boolean newOwnable) {
        ownable = newOwnable;
    }

    public Player getOwner() {
        return owner;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public static List<Field> loadFields(Context context) {
        return CSVReader.readFields(context);
    }
    public boolean hasHotel(){
        return hotel != null;
    }
    public void setHotel(Hotel hotel){
        this.hotel = hotel;
    }
    public Hotel getHotel(){
        return hotel;
    }
    public List<House> getHouses(){
       return this.houses;
    }
    public void addHouse(House house){
        houses.add(house);
    }
    public void removeHouse(House house, int numberOfHousesToRemove){
        for (int i = 0; i < numberOfHousesToRemove; i++){
            houses.remove(house);
        }
    }
    public int getRent() {
        return rent;
    }
    public void setRent(int rent) {
        this.rent = rent;
    }

    public void copyFrom(Field field) {
        this.id = field.id;
        this.name = field.name;
        this.price = field.price;
        this.ownable = field.ownable;
        this.fieldType = field.fieldType;
        this.hotel = field.hotel;
        this.houses = field.houses;
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
