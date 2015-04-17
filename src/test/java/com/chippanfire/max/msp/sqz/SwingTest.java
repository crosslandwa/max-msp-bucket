package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.testng.AssertJUnit.assertEquals;

public class SwingTest extends MspStepperImplBaseTest {

    @Test
    public void stepsAtRegularIntervalsWithoutSwing() throws Exception {
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(101, 1);
                put(201, 2);
                put(301, 3);
                put(401, 4);
            }},
            stubMaxComms.values()
        );
    }

    @Test
    public void everyOtherStepIsEarlyWith2StepSwingPattern() throws Exception {
        stepper.updateSwing(Arrays.asList(0f, 0.75f, 1f));

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(68, 1);
                put(201, 2);
                put(268, 3);

                put(401, 4);
                put(468, 5);
                put(601, 6);
                put(668, 7);
            }},
            stubMaxComms.values()
        );
    }

    @Test
    public void everyThirdStepIsLateWith4StepSwingPattern() throws Exception {
        stepper.updateSwing(Arrays.asList(0f, 0.25f, 0.3f, 0.75f, 1f));

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(101, 1);
                put(246, 2);
                put(301, 3);

                put(401, 4);
                put(501, 5);
                put(646, 6);
                put(701, 7);
            }},
            stubMaxComms.values()
        );
    }

    @Test(enabled = false)
    public void extremeSwingCausesZigZagStepping() throws Exception {
        stepper.updateSwing(Arrays.asList(0f, 1f, 0.5f, 0f, 1f));

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();
        process(new float[] {0f, 0.01f});

        assertEquals(
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(26, 1);
                put(51, 2);
                put(76, 3);
                put(101, 4);
                put(151, 3);
                put(201, 2);
                put(251, 1);
                put(301, 0);
                put(326, 1);
                put(351, 2);
                put(376, 3);
                put(401, 4);
            }},
            stubMaxComms.values()
        );
    }
}