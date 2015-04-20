package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

public class JumpToStepTest extends MspStepperImplBaseTest {

    @Test
    public void canJumpToStep() throws Exception {
        rampFirstHalf();
        rampSecondHalf();
        rampFirstHalf();

        stepper.jumpToStep(5);

        rampSecondHalf();
        rampFirstHalf();
        rampSecondHalf();
        rampFirstHalf();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 1),
            stepOutput(201, 5),
            stepOutput(301, 6)
        );
    }

    @Test
    public void canJumpToStepRepeatedly() throws Exception {
        rampFirstHalf();

        stepper.jumpToStep(5);
        rampSecondHalf();
        rampFirstHalf();

        stepper.jumpToStep(6);
        rampSecondHalf();
        rampFirstHalf();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 5),
            stepOutput(201, 6)
        );
    }

    @Test
    public void canJumpRepeatedlyToSameStep() throws Exception {
        rampFirstHalf();

        stepper.jumpToStep(5);
        rampSecondHalf();
        rampFirstHalf();

        stepper.jumpToStep(5);
        rampSecondHalf();
        rampFirstHalf();

        assertStepsOutputAt(
            stepOutput(1, 0),
            stepOutput(101, 5),
            stepOutput(201, 5)
        );
    }
}
