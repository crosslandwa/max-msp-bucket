package com.chippanfire.max.msp.sqz;


import com.chippanfire.max.msp.StubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StepDetectorTest {

    private final StubMaxComms stubMaxComms = new StubMaxComms();
    private StepDetector stepDetector;

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepDetector = new StepDetector(stubMaxComms);
    }

    @Test
    public void detectsPositiveTransitionsOverThresholds() throws Exception {
        stepDetector.process(0.001f);
        assertEquals(1, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        stepDetector.process(0.1f);
        stepDetector.process(0.2f);
        assertEquals(1, stubMaxComms.messageCount());

        stepDetector.process(0.3f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        stepDetector.process(0.4f);
        assertEquals(2, stubMaxComms.messageCount());

        stepDetector.process(0.5f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(2, stubMaxComms.latest());

        stepDetector.process(0.6f);
        stepDetector.process(0.7f);
        assertEquals(3, stubMaxComms.messageCount());

        stepDetector.process(0.8f);
        assertEquals(4, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());

        stepDetector.process(0.9f);
        assertEquals(4, stubMaxComms.messageCount());

        stepDetector.process(0.1f);
        assertEquals(5, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());
    }

    @Test
    public void detectsNegativeTransitionsOverThresholds() throws Exception {
        stepDetector.process(0.001f);
        assertEquals(1, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        stepDetector.process(0.9f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());

        stepDetector.process(0.8f);
        stepDetector.process(0.7f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(2, stubMaxComms.latest());

        stepDetector.process(0.6f);
        stepDetector.process(0.5f);
        stepDetector.process(0.4f);
        assertEquals(4, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        stepDetector.process(0.3f);
        stepDetector.process(0.2f);
        assertEquals(5, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        stepDetector.process(0.1f);
        stepDetector.process(0f);
        stepDetector.process(0.9f);
        assertEquals(6, stubMaxComms.messageCount());
        assertEquals(3, stubMaxComms.latest());
    }

    @Test
    public void detectsPostiveThenNegativeTransitionsOverSameThreshold() throws Exception {
        stepDetector.process(0.2f);
        assertEquals(1, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());

        stepDetector.process(0.3f);
        assertEquals(2, stubMaxComms.messageCount());
        assertEquals(1, stubMaxComms.latest());

        stepDetector.process(0.2f);
        assertEquals(3, stubMaxComms.messageCount());
        assertEquals(0, stubMaxComms.latest());
    }
}