package com.example.dkt_group_beta.model;

import android.content.Context;

import com.example.dkt_group_beta.io.CSVReader;
import com.example.dkt_group_beta.model.enums.FieldType;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Field implements Serializable {

    private int id;
    private String name;
    private int price = 0;
    private Player owner;
    private boolean ownable;

    private FieldType fieldType;

    public Field(int id, String name, int price, boolean ownable, FieldType fieldType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.ownable = ownable;
        this.fieldType = fieldType;
    }

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

    public static List<Field> loadFields(Context context) throws IOException {
        return CSVReader.readFields(context);
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
