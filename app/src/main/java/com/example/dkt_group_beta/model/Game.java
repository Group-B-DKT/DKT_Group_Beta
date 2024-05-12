package com.example.dkt_group_beta.model;

import android.util.Log;

import com.example.dkt_group_beta.activities.GameBoard;
import com.example.dkt_group_beta.communication.controller.WebsocketClientController;
import com.example.dkt_group_beta.networking.WebSocketClient;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    public static final int MIN_PLAYER = 2;

    private SecureRandom random;

    private List<Player> players;

    private List<Field> fields;

    private Player player = WebsocketClientController.getPlayer();

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

    public Field buyField(int index) {

        if (index < 0 || index >= fields.size()) {
            return null;
        }

        Field field = fields.get(index);
        if (field.getOwnable() && player.getMoney() >= field.getPrice()) {
            field.setOwner(player);
            field.setOwnable(false);
            player.setMoney(player.getMoney() - field.getPrice());
            return field;
        } else {
            return null;
        }
    }
    public void updateField(Field field) {
        Field savedField = this.fields.stream()
                .filter(f -> f.getId() == field.getId())
                .findAny().orElse(null);
        if (savedField == null){
            this.fields.add(field);
        }else{
            int index = this.fields.indexOf(savedField);
            this.fields.set(index, field);
        }
    }
    public void updatePlayer(Player player) {
        Player savedPlayer = this.players.stream()
                .filter(f -> f.getId().equals(player.getId()))
                .findAny().orElse(null);
        if (player == null){
            this.players.add(player);
        }else{
            int index = this.players.indexOf(savedPlayer);
            this.players.set(index, player);
        }
    }


    public Player getPlayerById(String id){

        for (Player p: players) {

            if(p.getId().equals(id)){
                return p;
            }

        }
        return null;
    }
}
