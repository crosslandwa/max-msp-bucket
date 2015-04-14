package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.Bangable;

class RampStopActions implements Bangable {
    private final StepDetector stepDetector;
    private final Ramp ramp;

    RampStopActions(StepDetector stepDetector, Ramp ramp) {
        this.stepDetector = stepDetector;
        this.ramp = ramp;
    }


    public void bang() {
        ramp.stop();
        stepDetector.reset();
    }
}
