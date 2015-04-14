package com.chippanfire.max.msp;

import java.util.LinkedList;
import java.util.List;

public class StubMaxComms implements MaxComms {
    private final List<Integer> values = new LinkedList<Integer>();

    public boolean outlet(int value) {
        values.add(value);
        return true;
    }

    /**
     * @param i Captured output (indexed from 0)
     * @return
     */
    public Integer capturedAt(int i) {
        return values.get(i);
    }

    public int messageCount() {
        return values.size();
    }

    public void reset() {
        values.clear();
    }
}
