package com.chippanfire.max.msp.sqz;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertTrue;

public class RampTest {
    private Ramp ramp;

    @BeforeMethod
    public void setUp() throws Exception {
        ramp = new Ramp().setRampTime(10).setNumberOfSteps(32);
    }

    @Test
    public void rampsFromZeroToMaxThenStaysAtMax() throws Exception {
        assertEqualsWithin6DP(0f, ramp.advance());
        assertEqualsWithin6DP(0.003125f, ramp.advance());
        assertEqualsWithin6DP(0.00625f, ramp.advance());
        assertEqualsWithin6DP(0.009375f, ramp.advance());
        assertEqualsWithin6DP(0.0125f, ramp.advance());
        assertEqualsWithin6DP(0.015625f, ramp.advance());
        assertEqualsWithin6DP(0.01875f, ramp.advance());
        assertEqualsWithin6DP(0.021875f, ramp.advance());
        assertEqualsWithin6DP(0.025f, ramp.advance());
        assertEqualsWithin6DP(0.028125f, ramp.advance());
        assertEqualsWithin6DP(0.03125f, ramp.advance());
        assertEqualsWithin6DP(0.03125f, ramp.advance());
        assertEqualsWithin6DP(0.03125f, ramp.advance());
    }

    @Test
    public void canBeStepped() throws Exception {
        assertEqualsWithin6DP(0f, ramp.advance());
        assertEqualsWithin6DP(0.003125f, ramp.advance());
        assertEqualsWithin6DP(0.00625f, ramp.advance());
        assertEqualsWithin6DP(0.009375f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.03125f, ramp.advance());
        assertEqualsWithin6DP(0.034375f, ramp.advance());
        assertEqualsWithin6DP(0.0375f, ramp.advance());
        assertEqualsWithin6DP(0.040625f, ramp.advance());
    }

    @Test
    public void canBeReset() throws Exception {
        assertEqualsWithin6DP(0f, ramp.advance());
        assertEqualsWithin6DP(0.003125f, ramp.advance());
        assertEqualsWithin6DP(0.00625f, ramp.advance());
        assertEqualsWithin6DP(0.009375f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.03125f, ramp.advance());
        assertEqualsWithin6DP(0.034375f, ramp.advance());
        assertEqualsWithin6DP(0.0375f, ramp.advance());
        assertEqualsWithin6DP(0.040625f, ramp.advance());
        ramp.stop();
        assertEqualsWithin6DP(0f, ramp.advance());
        assertEqualsWithin6DP(0.003125f, ramp.advance());
        assertEqualsWithin6DP(0.00625f, ramp.advance());
        assertEqualsWithin6DP(0.009375f, ramp.advance());
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        ramp = new Ramp().setRampTime(10).setNumberOfSteps(3);

        assertEqualsWithin6DP(0f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.333333f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.666666f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.333333f, ramp.advance());
        ramp.step();
        assertEqualsWithin6DP(0.666666f, ramp.advance());
    }

    @Test
    public void canJumpToStep() throws Exception {
        ramp.setNextStep(4);

        // advance within current step...
        assertEqualsWithin6DP(0f, ramp.advance());
        assertEqualsWithin6DP(0.003125f, ramp.advance());
        assertEqualsWithin6DP(0.00625f, ramp.advance());
        assertEqualsWithin6DP(0.009375f, ramp.advance());

        // until a step occurs
        ramp.step();

        assertEqualsWithin6DP(0.125f, ramp.advance());
        assertEqualsWithin6DP(0.128125f, ramp.advance());
        assertEqualsWithin6DP(0.13125f, ramp.advance());
        assertEqualsWithin6DP(0.134375f, ramp.advance());

        ramp.setNextStep(0);
        ramp.step();
        assertEqualsWithin6DP(0f, ramp.advance());
    }

    private void assertEqualsWithin6DP(float one, float two) {
        assertTrue("Expected " + two + " to equal " + one, Math.abs(one - two) < 0.000001);
    }
}