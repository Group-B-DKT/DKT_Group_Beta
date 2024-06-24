package com.example.dkt_group_beta.model.utilities;

import com.example.dkt_group_beta.model.interfaces.TimerElapsedEvent;

public class ThreadTimer extends Thread {
    private TimerElapsedEvent timerElapsedEvent;
    private int timer;
    private long oldMillis;
    private long oldSecondsMillis;
    int secondsRemaining;

    public ThreadTimer(int timer, TimerElapsedEvent timerElapsedEvent) {
        this.timerElapsedEvent = timerElapsedEvent;
        this.timer = timer;
        this.secondsRemaining = timer / 1000;
    }

    public ThreadTimer(TimerElapsedEvent timerElapsedEvent) {
        this(10000, timerElapsedEvent);
    }

    @Override
    public synchronized void start() {
        super.start();

        oldMillis = System.currentTimeMillis();
        oldSecondsMillis = System.currentTimeMillis();
        timerElapsedEvent.onSecondElapsed(secondsRemaining);
    }

    @Override
    public void run() {
        super.run();

        while (System.currentTimeMillis() - oldMillis < timer){
            if (System.currentTimeMillis() - oldSecondsMillis > 1000){
                oldSecondsMillis = System.currentTimeMillis();
                timerElapsedEvent.onSecondElapsed(--secondsRemaining);
            }
        }

        timerElapsedEvent.onTimerElapsed();
    }
}
