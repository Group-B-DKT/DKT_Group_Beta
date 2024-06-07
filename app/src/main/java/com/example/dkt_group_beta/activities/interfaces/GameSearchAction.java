package com.example.dkt_group_beta.activities.interfaces;

import com.example.dkt_group_beta.model.Field;
import com.example.dkt_group_beta.model.GameInfo;
import com.example.dkt_group_beta.model.Player;

import java.util.List;

public interface GameSearchAction {
    void addGameToScrollView(int gameId, String gameName,  int amountOfPLayer, boolean isStarted);
    void onConnectionEstablished();

    void refreshGameListItems();

    void switchToGameLobby(String username);

    void reconnectToGame(GameInfo gameInfo);

    void switchToGameBoard(List<Player> connectedPlayers, List<Field> fields);

}
