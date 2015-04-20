package com.chippanfire.max.msp.sqz;

import org.testng.annotations.Test;

import java.util.LinkedHashMap;

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

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(101, 1);
            put(201, 5);
            put(301, 6);
        }});
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

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(101, 5);
            put(201, 6);
        }});
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

        assertStepsOutputAt(new LinkedHashMap<Integer, Integer>() {{
            put(1, 0);
            put(101, 5);
            put(201, 5);
        }});
    }
}
