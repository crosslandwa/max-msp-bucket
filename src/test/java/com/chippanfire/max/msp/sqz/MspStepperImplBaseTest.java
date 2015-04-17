package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.SampleCountingStubMaxComms;
import org.testng.annotations.BeforeMethod;

/**
 * Base setup for tests interacting with MspStepperImpl
 */
public abstract class MspStepperImplBaseTest {
    protected MspStepperImpl stepper;
    protected final SampleCountingStubMaxComms stubMaxComms = new SampleCountingStubMaxComms();

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new MspStepperImpl(stubMaxComms, 32);
        stepper.rampTimeInSamples(10);
    }

    protected final MspStepperImplBaseTest rampFirstHalf() {
        process(0f, 0.1f, 0.2f, 0.3f, 0.4f);
        return this;
    }

    protected final MspStepperImplBaseTest rampSecondHalf() {
        process(0.5f, 0.6f, 0.7f, 0.8f, 0.9f);
        return this;
    }

    protected final MspStepperImplBaseTest rampStopped() {
        process(0f, 0f, 0f, 0f, 0f);
        return this;
    }

    protected final void process(Float... values) {
        for (Float value : values) {
            stubMaxComms.updateSampleIndex();
            stepper.process(value);
        }
    }
}
