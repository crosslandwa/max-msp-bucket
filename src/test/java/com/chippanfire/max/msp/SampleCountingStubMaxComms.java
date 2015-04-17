package com.chippanfire.max.msp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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

    public void updateSampleIndex(){
        if (count == null) {
            count = 0;
        } else {
            count += 1;
        }
    }

    public int latest() {
        List<Map.Entry<Integer,Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(values.entrySet());
        Map.Entry<Integer, Integer> lastEntry = entryList.get(entryList.size() - 1);
        return lastEntry.getValue();
    }

    public Map<Integer, Integer> values() {
        return values;
    }

    public int messageCount() {
        return values.size();
    }

    public void reset() {
        values.clear();
        count = null;
    }
}
