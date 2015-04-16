package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.StubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JumpToStepTest {

    private MspStepperImpl stepper;
    private StubMaxComms stubMaxComms = new StubMaxComms();

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new MspStepperImpl(stubMaxComms);
    }

    @Test
    public void canJumpToStep() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        stepper.jumpToStep(5);

        rampSecondHalf();

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 5);
        assertEquals(stubMaxComms.messageCount(), 3);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 6);
        assertEquals(stubMaxComms.messageCount(), 4);
    }

    @Test
    public void canJumpToStepRepeatedly() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        stepper.jumpToStep(5);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 5);
        assertEquals(stubMaxComms.messageCount(), 2);

        stepper.jumpToStep(6);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 6);
        assertEquals(stubMaxComms.messageCount(), 3);
    }

    @Test(enabled = false) // TODO This highlights a bug to fix!
    public void canJumpRepeatedlyToSameStep() throws Exception {
        rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        stepper.jumpToStep(5);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 5);
        assertEquals(stubMaxComms.messageCount(), 2);

        stepper.jumpToStep(5);

        rampSecondHalf().rampFirstHalf();

        assertEquals(stubMaxComms.latest(), 5);
        assertEquals(stubMaxComms.messageCount(), 3);
    }

    private JumpToStepTest rampFirstHalf() {
        process(0f, 0.01f, 0.05f, 0.08f, 0.1f, 0.2f, 0.3f, 0.49f);
        return this;
    }

    private JumpToStepTest rampSecondHalf() {
        process(0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 0.91f, 0.95f, 0.955f, 0.99f, 0.995f);
        return this;
    }

    private void process(Float... values) {
        for (Float value : values) {
            stepper.process(value);
        }
    }
}
