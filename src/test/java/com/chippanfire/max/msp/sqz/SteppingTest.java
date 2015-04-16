package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.StubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class SteppingTest {

    private MspStepperImpl stepper;
    private StubMaxComms stubMaxComms = new StubMaxComms();

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new MspStepperImpl(stubMaxComms, 32);
    }

    @Test
    public void outputsEachStep() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        stepper.numberOfSteps(3);
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 4);
    }

    @Test
    public void restartsAtZeroAfterStopping() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 2);
        assertEquals(stubMaxComms.messageCount(), 3);

        rampStopped().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 4);
    }

    private SteppingTest rampFirstHalf() {
        process(0f, 0.01f, 0.05f, 0.08f, 0.1f, 0.2f, 0.3f, 0.49f);
        return this;
    }

    private SteppingTest rampSecondHalf() {
        process(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.91f, 0.95f, 0.955f, 0.99f, 0.995f);
        return this;
    }

    private SteppingTest rampStopped() {
        process(0f, 0f, 0f, 0f, 0f);
        return this;
    }

    private void process(Float... values) {
        for (Float value : values) {
            stepper.process(value);
        }
    }
}
