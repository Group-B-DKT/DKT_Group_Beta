package com.example.dkt_group_beta.model;



import com.example.dkt_group_beta.communication.controller.WebsocketClientController;

import java.security.SecureRandom;
import java.util.ArrayList;
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
    public boolean payTaxes(Player currentPlayer, Field field) {
        Player owner = field.getOwner();
        if (owner != null && !owner.getId().equals(currentPlayer.getId())) {
            int taxAmount = field.getRent();
            if (currentPlayer.getMoney() > taxAmount) {
                owner.setMoney(owner.getMoney() + taxAmount);
                currentPlayer.setMoney(currentPlayer.getMoney()-taxAmount);
                return true;
            }
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

        Player savedPlayer = null;
        if (field.getOwner()!= null){
            savedPlayer =this.players.stream()
                    .filter(f -> f.getId().equals(field.getOwner().getId()))
                    .findAny().orElse(null);
        }

        if (savedField == null){
            this.fields.add(field);
            return;
        }
        savedField.copyFrom(field);
        savedField.setOwner(savedPlayer);
    }
    public void updatePlayer(Player player) {
        Player savedPlayer = this.players.stream()
                .filter(f -> f.getId().equals(player.getId()))
                .findAny().orElse(null);
        if (savedPlayer == null){
            this.players.add(player);
        }else{
            savedPlayer.copyFrom(player);

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



    public void setMoney(int money){
        player.setMoney(player.getMoney() + money);
    }


    public void updateHostStatus(String playerId) {
        this.players.forEach(p -> {
            p.setHost(false);
            if (p.getId().equals(playerId)){
                p.setHost(true);
            }
        });
    }
    public int getFieldListSize(){
        return fields.size();
    }
    public int getFieldPosition(int fieldID){
        for (int i = 0; i < fields.size(); i++) {
            if(fields.get(i).getId() == fieldID){
                return i;
            }
        }
        return -1;
    }
}
