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

    @Test
    public void extremeSwingCausesZigZagStepping() throws Exception {
        stepper.updateSwing(Arrays.asList(0f, 0.75f, 0.25f, 0.5f, 1f));

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
                put(35, 1);
                put(68, 2);
                put(101, 3);
                put(102, 2);
                put(152, 1);
                put(301, 2);
                put(352, 3);

                put(401, 4);
                put(435, 5);
                put(468, 6);
                put(501, 7);
                put(502, 6);
                put(552, 5);
                put(701, 6);
                put(751, 7);
            }},
            stubMaxComms.values()
        );
    }
}