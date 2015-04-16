package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.SampleCountingStubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.LinkedHashMap;

import static org.testng.AssertJUnit.assertEquals;

public class SwingTest {
    private MspStepperImpl stepper;
    private Swing swing;

    private SampleCountingStubMaxComms countingStubMaxComms = new SampleCountingStubMaxComms();

    @BeforeMethod
    public void setUp() throws Exception {
        swing = swing.unswung(4);
        countingStubMaxComms.reset();
        stepper = new MspStepperImpl(countingStubMaxComms, 32);
    }

    @Test
    public void noInterpolationWhenNoSwing() throws Exception {
        assertEquals(swing.process(0f), 0f);
        assertEquals(swing.process(0.5f), 0.5f);
        assertEquals(swing.process(1f), 1f);
    }

    @Test
    public void interpolates() throws Exception {
        swing = swing.withSwingValues(Arrays.asList(0f, 0.25f, 0.3f, 0.75f, 1f));

        assertEquals(swing.process(0f), 0f);
        assertEquals(swing.process(0.125f), 0.125f);
        assertEquals(swing.process(0.25f), 0.25f);
        assertEquals(swing.process(0.375f), 0.275f);
        assertEquals(swing.process(0.5f), 0.3f);
        assertEquals(swing.process(0.75f), 0.75f);
    }

    @Test
    public void stepsAtRegularIntervalsWithoutSwing() throws Exception {
        process(
            0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f,
            0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f,
            0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f,
            0f, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f,
            0f, 0.1f
        );
        assertEquals(countingStubMaxComms.messageCount(), 5);
        assertEquals(
            countingStubMaxComms.values(),
            new LinkedHashMap<Integer, Integer>(){{
                put(1, 0);
                put(11, 1);
                put(21, 2);
                put(31, 3);
                put(41, 4);
            }}
        );
    }

    private void process(Float... samples) {
        for (Float sample : samples) {
            countingStubMaxComms.updateSampleIndex();
            stepper.process(sample);
        }
    }
}