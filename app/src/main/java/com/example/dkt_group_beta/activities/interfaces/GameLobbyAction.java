package com.example.dkt_group_beta.activities.interfaces;

public interface GameLobbyAction {
    void addPlayerToView(String username);
    void removePlayerFromView (String username);
    void switchToGameLobby(String username);

}
