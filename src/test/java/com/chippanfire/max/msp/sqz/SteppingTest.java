package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

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

        assertStepsOutputAt(
            stepOutput(11, 0),
            stepOutput(111, 1),
            stepOutput(163, 0),
            stepOutput(263, 1)
        );
    }

    @Test
    public void outputsEachStep() throws Exception {
        fullRamp();
        fullRamp();
        fullRamp();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2)
        );
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        stepper.numberOfSteps(3);
        fullRamp();
        fullRamp();
        fullRamp();
        fullRamp();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2),
            stepOutput(301, 0)
        );
    }

    @Test
    public void restartsAtZeroAfterStopping() throws Exception {
        fullRamp();
        fullRamp();
        process(new float[]{0f, 0.01f});

        rampStopped();
        fullRamp();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2),
            stepOutput(213, 0)
        );
    }

    @Test
    public void ignoresSpuriousRapidRamps() throws Exception {
        fullRamp(); //1 [on next start]
        fullRamp(); //2 [on next start]
        process(miniRamp(30));
        fullRamp(); // 3 after 70 samples
        fullRamp(); //4 [on next start] (130 samples after 3)
        process(miniRamp(30));
        fullRamp(); // 5 after 70
        fullRamp(); // 6 [on next start] (130 samples after 5)
        fullRamp(); // 7 [on next start]
        fullRamp(); // 8 [on next start]
        fullRamp(); // 9 [on next start]

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2),
            stepOutput(302, 3),
            stepOutput(431, 4),
            stepOutput(532, 5),
            stepOutput(661, 6),
            stepOutput(761, 7),
            stepOutput(861, 8)
        );
    }

    @Test
    public void ignoresStepsWhenInputRampJumpsFromLowBackToHighThenLowAgain() throws Exception {
        fullRamp(); //1 [on next start]
        fullRamp(); //2 [on next start]
        process(new float[]{0f, 0.001f, 0.002f, 0.9985f, 0.999f});
        fullRamp(); //3 on 95 samples
        fullRamp(); //4 [on next start] (105 samples after 3)
        fullRamp(); //5 [on next start]
        fullRamp(); //6 [on next start]

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 2),
            stepOutput(302, 3),
            stepOutput(406, 4),
            stepOutput(506, 5)
        );
    }

    private float[] miniRamp(int miniRampLength) {
        float[] miniRamp = new float[miniRampLength];

        for (int i = 0; i < miniRampLength; i++) {
            miniRamp[i] = i / (float) miniRampLength;
        }
        return miniRamp;
    }
}
