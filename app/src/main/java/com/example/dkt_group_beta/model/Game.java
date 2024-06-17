package com.example.dkt_group_beta.model;

import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

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
        return 2;
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
    public boolean buyHouse(Player player, House house, Field field) {
        if (field.getOwner().getId().equals(player.getId())  && player.getMoney() >= House.getHousePrice()) {
            if (field.hasHotel()) {
                return false;
            } else if (getNumberOfHouses(field) == house.getMaxAmount()) {
                buyHotel(player, new Hotel(Hotel.HOTEL_PRICE, 10), field);
                field.removeHouse(house, 4);
                return true;
            } else {
                field.addHouse(house);
                pay(player, House.getHousePrice());
                return true;
            }
        }
        return false;
    }

    public boolean buyHotel(Player player, Hotel hotel, Field field) {
        if (field.getOwner().getId().equals(player.getId()) && player.getMoney() >= hotel.getPrice()){
            if (field.hasHotel()) {
                return false;
            } else {
                field.setHotel(hotel);
                pay(player, hotel.getPrice());
                return true;
            }
        }
        return false;
    }
    public int getNumberOfHouses(Field field){
        return field.getHouses().size();
    }
    
    /*public boolean sellBuilding(Player player, Building building) {
        Field field = building.getField();
        if (field.getOwner() == player) {
            if (building instanceof House) {
                player.setMoney(player.getMoney() + ((House) building).getHousePrice() / 2);
            } else if (building instanceof Hotel) {
                player.setMoney(player.getMoney() + ((Hotel) building).getPrice() / 2);
            }
            field.getBuildings().remove(building);
            return true;
        }
        return false;
    }*/

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
        if (savedPlayer == null){
            this.players.add(player);
        }else{
            int index = this.players.indexOf(savedPlayer);
            player.setCharacterView(savedPlayer.getCharacterView());
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

    public void setPlayerTurn(String id) {
        for (Player p: players) {
            if (p.isOnTurn()){
                p.setOnTurn(false);
            }
            if (p.getId().equals(id)) {
                p.setOnTurn(true);
            }
        }
    }
}
