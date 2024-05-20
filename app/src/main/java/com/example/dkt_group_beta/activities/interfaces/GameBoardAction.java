package com.example.dkt_group_beta.activities.interfaces;

import android.view.View;

import com.example.dkt_group_beta.model.Card;
import com.example.dkt_group_beta.model.Player;

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
    void showCard(Card card);
}