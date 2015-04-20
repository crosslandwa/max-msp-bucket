package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import java.util.Arrays;

public class SwingTest extends MspStepperImplBaseTest {

    @Test
    public void stepsAtRegularIntervalsWithoutSwing() throws Exception {
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2),
            stepOutput(301, 3),
            stepOutput(401, 4)
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

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(68, 1),
            stepOutput(201, 2),
            stepOutput(268, 3),

            stepOutput(401, 4),
            stepOutput(468, 5),
            stepOutput(601, 6),
            stepOutput(668, 7)
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

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(246, 2),
            stepOutput(301, 3),

            stepOutput(401, 4),
            stepOutput(501, 5),
            stepOutput(646, 6),
            stepOutput(701, 7)
        );
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
        assertStepsOutputAt(
            stepOutput(1, 0),      // 1
            stepOutput(26, 1),     // 26
            stepOutput(52, 2),     // 51
            stepOutput(77, 3),     // 76
            stepOutput(101, 4),    // 101

            stepOutput(147, 3),    // 151
            stepOutput(197, 2),    // 201
            stepOutput(247, 1),    // 251
            stepOutput(297, 0),    // 301

            stepOutput(326, 1),    // 326
            stepOutput(352, 2),    // 351
            stepOutput(377, 3),    // 376
            stepOutput(401, 4)     // 401
        );
    }
}