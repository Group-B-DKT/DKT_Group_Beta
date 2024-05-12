package com.example.dkt_group_beta.activities.interfaces;

import com.example.dkt_group_beta.model.Player;

public interface GameBoardAction {
    void dicePopUp();
    void showBothDice(int[] diceResult);
    void markBoughtField(int index);
    void animation(Player player, int repetition);

}