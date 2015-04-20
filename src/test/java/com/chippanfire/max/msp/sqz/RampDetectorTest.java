package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.Bangable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class RampDetectorTest {
    private RampDetector rampDetector;

    private final List<Action> actions = new LinkedList<Action>();

    private enum Action {STOP, START}

    private class StubBangable implements Bangable {
        private final Action action;

        private StubBangable(Action action) {
            this.action = action;
        }

        public void bang() {
            actions.add(action);
        }
    }

    @BeforeMethod
    public void setUp() throws Exception {
        actions.clear();

        rampDetector = new RampDetector(new StubBangable(Action.START), new StubBangable(Action.STOP));
    }

    /**
     * In Max, after changing tempo, I saw ramps that jumped from 0.001 back up to 0.999 then down to 0.001 again
     * @throws Exception
     */
    @Test
    public void canHandleJumpyShitInput() throws Exception {
        rampDetector.setExpectedRampTime(8);
        process(
            0, 0.05f, 0.2f, 0.4f, 0.5f, 0.7f, 0.99f,
            0.001f, 0.002f, 0.9985f, 0.999f,
            0.01f, 0.02f, 0.3f, 0.4f, 0.5f,
            0, 0, 0
        );

        assertEquals(actions.get(0), Action.START);
        assertEquals(actions.get(1), Action.START);
        assertEquals(actions.get(2), Action.STOP);
        assertEquals(actions.size(), 3);
    }

    private void process(float... vector) {
        for (float sample : vector) {
            rampDetector.process(sample);
        }
    }
}