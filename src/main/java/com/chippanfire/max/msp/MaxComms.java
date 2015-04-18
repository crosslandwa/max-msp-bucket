package com.chippanfire.max.msp;

/**
 * Interface exposing interactions with Max environment
 */
public interface MaxComms {
    boolean outlet(int value);

    void post(String message);
}
