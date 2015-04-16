package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SteppingTest extends MspStepperImplBaseTest {

    @Test
    public void outputsEachStep() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        stepper.numberOfSteps(3);
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 4);
    }

    @Test
    public void restartsAtZeroAfterStopping() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);

        rampStopped().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 4);
    }

}