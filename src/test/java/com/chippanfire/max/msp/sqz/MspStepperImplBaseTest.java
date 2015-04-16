package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.StubMaxComms;
import org.testng.annotations.BeforeMethod;

/**
 * Base setup for tests interacting with MspStepperImpl
 */
public abstract class MspStepperImplBaseTest {
    protected MspStepperImpl stepper;
    protected final StubMaxComms stubMaxComms = new StubMaxComms();

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new MspStepperImpl(stubMaxComms, 32);
    }

    protected final MspStepperImplBaseTest rampFirstHalf() {
        process(0f, 0.01f, 0.05f, 0.08f, 0.1f, 0.2f, 0.3f, 0.49f);
        return this;
    }

    protected final MspStepperImplBaseTest rampSecondHalf() {
        process(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.91f, 0.95f, 0.955f, 0.99f, 0.995f);
        return this;
    }

    protected final MspStepperImplBaseTest rampStopped() {
        process(0f, 0f, 0f, 0f, 0f);
        return this;
    }

    protected final void process(Float... values) {
        for (Float value : values) {
            stepper.process(value);
        }
    }
}
