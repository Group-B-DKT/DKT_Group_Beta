package com.example.dkt_group_beta.model;

import java.util.List;

public class Game {
    public static final int MIN_PLAYER = 2;
    private List<Player> players;
    private List<Field> fields;
    private int playerMoney;

    public Game(List<Player> players, List<Field> fields) {
        this.players = players;
        this.fields = fields;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Field> getFields() {
        return fields;
    }

    public boolean pay(int amount) {
        if (playerMoney > amount) {
            playerMoney -= amount;
            return true;
        }
        return false;
    }
    public boolean buyField(Player player, Field field){
        if(pay(field.getPrice()) && field.getOwner() == null){
            field.setOwner(player);
            return true;
        }
        return false;
    }
    public boolean buyHouse(Player player, House house) {
        Field field = house.getField();
        if (field.getOwner() == player && pay(house.getHousePrice())) {
            if (field.hasHotel()) {
                return false;
            } else if (field.getNumberOfHouses() == house.getMaxAmount()) {
                return buyHotel(player, new Hotel(Hotel.HOTEL_PRICE, player, house.getPosition(), field));
            } else {
                field.addBuilding(house);
                house.setOwner(player);
                house.setField(field);
                return true;
            }
        }
        return false;
    }

    public boolean buyHotel(Player player, Hotel hotel) {
        Field field = hotel.getField();
        if (field.getOwner() == player && pay(hotel.getPrice())) {
            if (field.hasHotel()) {
                return false;
            } else {
                field.addBuilding(hotel);
                hotel.setOwner(player);
                hotel.setField(field);
                return true;
            }
        }
        return false;
    }
}
