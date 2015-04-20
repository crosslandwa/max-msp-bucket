package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;

public class SwingTest extends MspStepperImplBaseTest {

    @Test
    public void stepsAtRegularIntervalsWithoutSwing() throws Exception {
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(101, 1);
            put(201, 2);
            put(301, 3);
            put(401, 4);
        }});
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

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(68, 1);
            put(201, 2);
            put(268, 3);

            put(401, 4);
            put(468, 5);
            put(601, 6);
            put(668, 7);
        }});
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

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(101, 1);
            put(246, 2);
            put(301, 3);

            put(401, 4);
            put(501, 5);
            put(646, 6);
            put(701, 7);
        }});
    }

    @Test
    public void extremeSwingCausesZigZagStepping() throws Exception {
        stepper.updateSwing(Arrays.asList(0f, 1f, 0.5f, 0f, 1f));

        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();
        process(new float[] {0f, 0.01f});

        /*
         * Comments show nominal times expect steps to happen. Actual values reflect that stepping actually occurs at
         * a 'close enough' time (i.e. accurate within a couple of samples)
         */
        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);      // 1
            put(26, 1);     // 26
            put(52, 2);     // 51
            put(77, 3);     // 76
            put(101, 4);    // 101

            put(147, 3);    // 151
            put(197, 2);    // 201
            put(247, 1);    // 251
            put(297, 0);    // 301

            put(326, 1);    // 326
            put(352, 2);    // 351
            put(377, 3);    // 376
            put(401, 4);    // 401
        }});
    }
}