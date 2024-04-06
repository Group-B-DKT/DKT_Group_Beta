package com.example.dkt_group_beta.activities.interfaces;

public interface GameSearchAction2 {
    void addGameToScrollView(int gameId, String gameName,  int amountOfPLayer);
    void onConnectionEstablished();

    void refreshGameListItems();
}
