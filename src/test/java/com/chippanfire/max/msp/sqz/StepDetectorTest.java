package com.chippanfire.max.msp.sqz;


import com.chippanfire.max.msp.SampleCountingStubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StepDetectorTest {

    private final SampleCountingStubMaxComms stubMaxComms = new SampleCountingStubMaxComms();
    private StepDetector stepDetector;

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepDetector = new StepDetector(stubMaxComms);
    }

    @Test
    public void detectsPositiveTransitionsOverThresholds() throws Exception {
        process(0.001f);
        assertEquals(1, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        process(0.1f);
        process(0.2f);
        assertEquals(1, stubMaxComms.messageCount());

        process(0.3f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        process(0.4f);
        assertEquals(2, stubMaxComms.messageCount());

        process(0.5f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(2, stubMaxComms.latest());

        process(0.6f);
        process(0.7f);
        assertEquals(3, stubMaxComms.messageCount());

        process(0.8f);
        assertEquals(4, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());

        process(0.9f);
        assertEquals(4, stubMaxComms.messageCount());

        process(0.1f);
        assertEquals(5, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());
    }

    @Test
    public void detectsNegativeTransitionsOverThresholds() throws Exception {
        process(0.001f);
        assertEquals(0, stubMaxComms.latest());
        assertEquals(1, stubMaxComms.messageCount());

        process(0.9f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());

        process(0.8f);
        process(0.7f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(2, stubMaxComms.latest());

        process(0.6f);
        process(0.5f);
        process(0.4f);
        assertEquals(4, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        process(0.3f);
        process(0.2f);
        assertEquals(5, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        process(0.1f);
        process(0f);
        process(0.9f);
        assertEquals(6, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());
    }

    @Test
    public void detectsPostiveThenNegativeTransitionsOverSameThreshold() throws Exception {
        process(0.2f);
        assertEquals(1, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        process(0.3f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        process(0.2f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());
    }

    private void process(float value) {
        stubMaxComms.updateSampleIndex();
        stepDetector.process(value);
    }
}