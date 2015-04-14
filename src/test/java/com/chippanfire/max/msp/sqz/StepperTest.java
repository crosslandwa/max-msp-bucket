package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.StubMaxComms;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class StepperTest {

    private final StubMaxComms stubMaxComms = new StubMaxComms();
    private Stepper stepper;

    @BeforeMethod
    public void setUp() throws Exception {
        stubMaxComms.reset();
        stepper = new Stepper(stubMaxComms);
    }

    @Test
    public void outputsEachStep() throws Exception {
        stepper.step().step().step();

        assertEquals(new Integer(0), stubMaxComms.capturedAt(0));
        assertEquals(new Integer(1), stubMaxComms.capturedAt(1));
        assertEquals(new Integer(2), stubMaxComms.capturedAt(2));
    }

    @Test
    public void canBeReset() throws Exception {
        stepper.step().step().reset().step();

        assertEquals(new Integer(0), stubMaxComms.capturedAt(0));
        assertEquals(new Integer(1), stubMaxComms.capturedAt(1));
        assertEquals(new Integer(0), stubMaxComms.capturedAt(2));
    }

    @Test
    public void rollsOverAtMax() throws Exception {
        stepper.setMax(3).step().step().step().step().step().step();

        assertEquals(new Integer(0), stubMaxComms.capturedAt(0));
        assertEquals(new Integer(1), stubMaxComms.capturedAt(1));
        assertEquals(new Integer(2), stubMaxComms.capturedAt(2));
        assertEquals(new Integer(0), stubMaxComms.capturedAt(3));
        assertEquals(new Integer(1), stubMaxComms.capturedAt(4));
        assertEquals(new Integer(2), stubMaxComms.capturedAt(5));
    }
}