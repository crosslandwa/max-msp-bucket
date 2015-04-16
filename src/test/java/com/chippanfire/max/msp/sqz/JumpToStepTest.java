package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class JumpToStepTest extends MspStepperImplBaseTest {

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
}
