package com.example.dkt_group_beta.activities.interfaces;

import android.view.View;

import com.example.dkt_group_beta.model.Player;

import java.time.LocalTime;

public interface GameBoardAction {
    void dicePopUp();
    void showBothDice(int[] diceResult);
    void markBoughtField(int index);
    void markBoughtField(int index, int color);
    void animation(Player player, int repetition);
    void disableEndTurnButton();
    void enableEndTurnButton();
    void updatePlayerStats();
    void enableDiceButton();
    void showDisconnectPopUp(Player disconnectedPlayer, LocalTime serverTime);
    void removeReconnectPopUp();
    void removePlayerFromGame(Player fromPlayer);
    void setPlayerDisconnected(Player disconnectedPlayer);
    void showTaxes(Player payer, Player payee, int amount);
}