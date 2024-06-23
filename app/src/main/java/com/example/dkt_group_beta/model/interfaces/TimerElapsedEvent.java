package com.example.dkt_group_beta.model.interfaces;

public interface TimerElapsedEvent {
    void onTimerElapsed();
    void onSecondElapsed(int secondsRemaining);
}
