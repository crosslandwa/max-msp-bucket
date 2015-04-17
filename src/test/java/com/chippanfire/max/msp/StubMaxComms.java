package com.chippanfire.max.msp;

import java.util.LinkedList;
import java.util.List;

public class StubMaxComms implements MaxComms {
    private final List<Integer> values = new LinkedList<Integer>();

    public boolean outlet(int value) {
        values.add(value);
        return true;
    }

    public int latest() {
        return values.get(values.size() - 1);
    }

    public int messageCount() {
        return values.size();
    }

    public void reset() {
        values.clear();
    }
}
