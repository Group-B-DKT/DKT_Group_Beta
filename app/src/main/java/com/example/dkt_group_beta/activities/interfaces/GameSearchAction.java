package com.example.dkt_group_beta.activities.interfaces;

public interface GameSearchAction {
    void addGameToScrollView(int gameId, String gameName,  int amountOfPLayer, boolean isStarted);
    void onConnectionEstablished();

    void refreshGameListItems();

    void switchToGameLobby(String username);
}
