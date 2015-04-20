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

        assertEquals(
            new LinkedHashMap<Integer, Integer>() {{
                put(1, 0);
                put(101, 1);
                put(201, 2);
                put(302, 3);
                put(431, 4);
                put(532, 5);
                put(661, 6);
                put(761, 7);
                put(861, 8);
            }},
            stubMaxComms.values()
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
