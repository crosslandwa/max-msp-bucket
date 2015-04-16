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
        process(0f, 0.05f);

        assertEquals(stubMaxComms.latest(), 0);
        assertEquals(stubMaxComms.messageCount(), 1);

        process(0.5f, 0.95f);

        process(0f, 0.05f);

        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        stepper.jumpToStep(5);
        process(0.5f, 0.95f);
        assertEquals(stubMaxComms.latest(), 1);
        assertEquals(stubMaxComms.messageCount(), 2);

        process(0f, 0.05f);

        assertEquals(stubMaxComms.latest(), 5);
        assertEquals(stubMaxComms.messageCount(), 3);
    }



    private void process(Float... values) {
        for (Float value : values) {
            stepper.process(value);
        }
    }
}
