package com.chippanfire.max.msp.sqz;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.testng.AssertJUnit.assertEquals;

public class SwingTest {
    private Swing swing;

    @BeforeMethod
    public void setUp() throws Exception {
        swing = new Swing(4, Arrays.asList(0f, 0.25f, 0.3f, 0.75f, 1f));
    }

    @Test
    public void noInterpolationWhenNoSwing() throws Exception {
        swing = swing.unswung();

        assertEquals(swing.process(0f), 0f);
        assertEquals(swing.process(0.5f), 0.5f);
        assertEquals(swing.process(1f), 1f);
    }

    @Test
    public void interpolates() throws Exception {
        assertEquals(swing.process(0f), 0f);
        assertEquals(swing.process(0.125f), 0.125f);
        assertEquals(swing.process(0.25f), 0.25f);
        assertEquals(swing.process(0.375f), 0.275f);
        assertEquals(swing.process(0.5f), 0.3f);
        assertEquals(swing.process(0.75f), 0.75f);
    }
}