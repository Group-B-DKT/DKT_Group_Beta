package com.example.dkt_group_beta.model;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static final int MIN_PLAYER = 2;

    private SecureRandom random;

    private List<Player> players;
    private List<Field> fields;

    public Game(List<Player> players, List<Field> fields) {
        this.players = players;
        this.fields = fields;
        this.random = new SecureRandom();
    }

    public int getRandomNumber(int min, int max) {
        if(min > max){
            return -1;
        }
        return random.nextInt(max - min + 1) + min;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Field> getFields() {
        return fields;
    }

    public boolean pay(Player player, int amount) {
        if (player.getMoney() > amount) {
            player.setMoney(player.getMoney() - amount);
            return true;
        }
        return false;
    }
    public boolean buyHouse(Player player, House house) {
        Field field = house.getField();
        if (field.getOwner() == player && pay(player, house.getHousePrice())) {
            if (field.hasHotel()) {
                return false;
            } else if (getNumberOfHouses() == house.getMaxAmount()) {
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
        if (field.getOwner() == player && pay(player, hotel.getPrice())) {
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
    public int getNumberOfHouses(){
        int count = 0;
        for (Field field : fields) {
            for (Building building : field.getBuildings()) {
                if (building instanceof House) {
                    count++;
                }
            }
        }
        return count;
    }

    public List<Field> getOwnedFields(Player player) {
        return this.fields.stream()
                          .filter(f -> f.getOwner() != null && f.getOwner().getId().equals(player.getId()))
                          .collect(Collectors.toList());
    }
}
