package com.chippanfire.max.msp;

import java.util.LinkedHashMap;
import java.util.Map;

public class SampleCountingStubMaxComms implements MaxComms {
    private final Map<Integer, Integer> values = new LinkedHashMap<Integer, Integer>(); //LinkedHashMap to preserve insertion order
    private Integer count = null;

    public boolean outlet(int value) {
        if (count != null) {
            values.put(count, value);
        }
        return true;
    }

    public void post(String message) {
        // No implementation needed
    }

    public void updateSampleIndex(){
        if (count == null) {
            count = 0;
        } else {
            count += 1;
        }
    }

    public Map<Integer, Integer> values() {
        return values;
    }

    public void reset() {
        values.clear();
        count = null;
    }
}
