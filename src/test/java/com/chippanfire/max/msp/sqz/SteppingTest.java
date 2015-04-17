package com.chippanfire.max.msp.sqz;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static junit.framework.Assert.assertEquals;

public class SteppingTest extends MspStepperImplBaseTest {

    @Test
    public void startsCleanly() throws Exception {
        rampStopped();
        fullRamp();
        process(new float[]{0f, 0.01f});
        rampStopped();
        rampStopped();
        rampStopped();
        rampStopped();
        rampStopped();
        fullRamp();
        process(new float[]{0f, 0.01f});

        AssertJUnit.assertEquals(
            new LinkedHashMap<Integer, Integer>() {{
                put(11, 0);
                put(111, 1);
                put(163, 0);
                put(263, 1);
            }},
            stubMaxComms.values()
        );
    }

    @Test
    public void outputsEachStep() throws Exception {
        fullRamp();
        fullRamp();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(101, 1);
                put(201, 2);
            }},
            stubMaxComms.values()
        );
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        stepper.numberOfSteps(3);
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(101, 1);
                put(201, 2);
                put(301, 0);
            }},
            stubMaxComms.values()
        );
    }

    @Test
    public void restartsAtZeroAfterStopping() throws Exception {
        fullRamp();
        fullRamp();
        process(new float[] {0f, 0.01f});

        rampStopped();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(101, 1);
                put(201, 2);
                put(213, 0);
            }},
            stubMaxComms.values()
        );
    }

}
