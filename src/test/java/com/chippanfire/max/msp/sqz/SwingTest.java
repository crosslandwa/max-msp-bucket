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
                put(11, 1);
                put(21, 2);
                put(31, 3);
                put(41, 4);
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
                put(8, 1);
                put(21, 2);
                put(28, 3);
                put(41, 4);
                put(48, 5);
                put(61, 6);
                put(68, 7);
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
                put(11, 1);
                put(26, 2);
                put(31, 3);
                put(41, 4);
                put(51, 5);
                put(66, 6);
                put(71, 7);
            }},
            stubMaxComms.values()
        );
    }
}