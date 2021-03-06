package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.SampleCountingStubMaxComms;
import org.testng.annotations.BeforeMethod;

import java.util.LinkedHashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Base setup for tests interacting with MspStepperImpl
 */
public abstract class MspStepperImplBaseTest {
    private static final int RAMP_LENGTH = 100;
    private static final int HALF = RAMP_LENGTH / 2;
    protected MspStepperImpl stepper;
    private final SampleCountingStubMaxComms stubMaxComms = new SampleCountingStubMaxComms();

    private final float[] firstHalfRamp = new float[HALF];
    private final float[] secondHalfRamp = new float[HALF];

    protected MspStepperImplBaseTest() {
        for (int i = 0; i < RAMP_LENGTH; i++) {
            float value = (float) i / RAMP_LENGTH;
            if (i < HALF) {
                firstHalfRamp[i] = value;
            } else {
                secondHalfRamp[i - HALF] = value;
            }
        }
    }

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new MspStepperImpl(stubMaxComms, 32);
        stepper.rampTimeInSamples(RAMP_LENGTH);
    }

    protected final MspStepperImplBaseTest rampFirstHalf() {
        process(firstHalfRamp);
        return this;
    }

    protected final MspStepperImplBaseTest rampSecondHalf() {
        process(secondHalfRamp);
        return this;
    }

    protected final MspStepperImplBaseTest fullRamp() {
        process(firstHalfRamp);
        process(secondHalfRamp);
        return this;
    }

    protected final MspStepperImplBaseTest rampStopped() {
        process(new float[] {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f});
        return this;
    }

    protected final void process(float[] values) {
        for (Float value : values) {
            stubMaxComms.updateSampleIndex();
            stepper.process(value);
        }
    }

    protected final void assertStepsOutputAt(StepOutput... expectedSteps) {
        Map<Integer, Integer> expected = new LinkedHashMap<Integer, Integer>();

        for (StepOutput stepOutput : expectedSteps) {
            expected.put(stepOutput.sampleIndex, stepOutput.step);
        }

        assertEquals(expected, stubMaxComms.values());
    }

    protected static final class StepOutput {
        private final int sampleIndex;
        private final int step;

        StepOutput(int sampleIndex, int step) {
            this.sampleIndex = sampleIndex;
            this.step = step;
        }
    }

    StepOutput stepOutput(int sampleIndex, int step) {
        return new StepOutput(sampleIndex, step);
    }
}
