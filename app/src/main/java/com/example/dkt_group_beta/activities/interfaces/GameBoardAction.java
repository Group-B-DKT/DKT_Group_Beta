package com.example.dkt_group_beta.activities.interfaces;

import android.view.View;
import android.widget.ImageView;

public interface GameBoardAction {
    void dicePopUp();
    void showBothDice(int[] diceResult);
    void animation(ImageView characterImageView, int repetition);

    }