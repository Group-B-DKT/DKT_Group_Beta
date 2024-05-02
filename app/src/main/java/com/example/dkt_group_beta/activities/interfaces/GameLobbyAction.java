package com.example.dkt_group_beta.activities.interfaces;

import android.content.Context;

import com.example.dkt_group_beta.model.Player;

public interface GameLobbyAction {

    void removePlayerFromView (Player username);
    void switchToGameLobby(Player username);


    void addPlayerToView(Player player);

    void readyStateChanged(String username, boolean isReady);

    void changeReadyBtnText(boolean isReady);
    void addStartButton();

    void assertInputDialog(String text);

    Context getContext();

}
