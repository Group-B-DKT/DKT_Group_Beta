package com.example.dkt_group_beta.model.utilities;

import com.example.dkt_group_beta.model.interfaces.TimerElapsedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ThreadTimerTest {
    private TimerElapsedEvent timerElapsedEvent;
    private ThreadTimer threadTimer;

    @BeforeEach
    public void setUp() {
        timerElapsedEvent = mock(TimerElapsedEvent.class);
    }

    @Test
    public void testTimerInitialization() {
        int timerDuration = 5000; // 5 seconds
        threadTimer = new ThreadTimer(timerDuration, timerElapsedEvent);

        assertNotNull(threadTimer);
        assertEquals(timerDuration / 1000, threadTimer.secondsRemaining);
    }

    @Test
    public void testStartMethod() throws InterruptedException {
        threadTimer = new ThreadTimer(5000, timerElapsedEvent);

        threadTimer.start();

        Thread.sleep(100);

        verify(timerElapsedEvent, times(1)).onSecondElapsed(threadTimer.secondsRemaining);
    }

    @Test
    public void testRunMethod() throws InterruptedException {
        threadTimer = new ThreadTimer(3000, timerElapsedEvent);

        threadTimer.start();
        threadTimer.join();

        verify(timerElapsedEvent, atLeast(3)).onSecondElapsed(anyInt());
        verify(timerElapsedEvent, times(1)).onTimerElapsed();
    }

    @Test
    public void testDefaultConstructor() {
        threadTimer = new ThreadTimer(timerElapsedEvent);

        assertNotNull(threadTimer);
        assertEquals(10, threadTimer.secondsRemaining);
    }

    @Test
    public void testOnSecondElapsed() throws InterruptedException {
        threadTimer = new ThreadTimer(3000, timerElapsedEvent);

        threadTimer.start();
        threadTimer.join();

        verify(timerElapsedEvent, times(3)).onSecondElapsed(anyInt());
    }

    @Test
    public void testOnTimerElapsed() throws InterruptedException {
        threadTimer = new ThreadTimer(3000, timerElapsedEvent);

        threadTimer.start();
        threadTimer.join();

        verify(timerElapsedEvent, times(1)).onTimerElapsed();
    }
}
