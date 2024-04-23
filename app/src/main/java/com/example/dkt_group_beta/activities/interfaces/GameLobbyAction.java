package com.example.dkt_group_beta.activities.interfaces;

import com.example.dkt_group_beta.model.Player;

public interface GameLobbyAction {
    void addPlayerToView(Player player);

    void readyStateChanged(String username, boolean isReady);

    public void changeReadyBtnText(boolean isReady);
}
