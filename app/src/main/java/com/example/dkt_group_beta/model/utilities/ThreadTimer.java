package com.example.dkt_group_beta.model.utilities;

import com.example.dkt_group_beta.model.interfaces.TimerElapsedEvent;

public class ThreadTimer extends Thread {
    private TimerElapsedEvent timerElapsedEvent;
    private int timer;
    private long oldMillis;

    public ThreadTimer(int timer, TimerElapsedEvent timerElapsedEvent) {
        this.timerElapsedEvent = timerElapsedEvent;
        this.timer = timer;
    }

    public ThreadTimer(TimerElapsedEvent timerElapsedEvent) {
        this(10000, timerElapsedEvent);
    }

    @Override
    public synchronized void start() {
        super.start();

        oldMillis = System.currentTimeMillis();
    }

    @Override
    public void run() {
        super.run();

        while (System.currentTimeMillis() - oldMillis < timer);

        timerElapsedEvent.onTimerElapsed();
    }
}
