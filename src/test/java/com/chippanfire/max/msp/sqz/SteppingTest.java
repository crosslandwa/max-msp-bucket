package com.chippanfire.max.msp.sqz;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static org.testng.Assert.assertEquals;

public class SteppingTest extends MspStepperImplBaseTest {

    @Test
    public void startsCleanly() throws Exception {
        rampStopped();
        rampStopped();
        fullRamp();
        fullRamp();

        AssertJUnit.assertEquals(
            new LinkedHashMap<Integer, Integer>() {{
                put(11, 0);
                put(111, 1);
            }},
            stubMaxComms.values()
        );
    }

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
