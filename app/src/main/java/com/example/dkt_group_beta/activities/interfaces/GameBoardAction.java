package com.example.dkt_group_beta.activities.interfaces;


public interface GameBoardAction {
    void dicePopUp();
    void showBothDice(int[] diceResult);
    void markBoughtField(int index);
}