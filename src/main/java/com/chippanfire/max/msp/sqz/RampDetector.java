package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.Bangable;

/**
 * Watches the incoming signal from a transport locked phasor~
 *  - detects when the phasor~ starts
 *  - detects when the phasor~ 'rolls over' and a new ramp occurs
 *
 *  Performs some checking to handle the dodgy output of a @lock 1 phasor
 *
 *  Essentially this detector is used to drive an internal instance of Ramp from which swing can be applied and each
 *  step reliably detected and output
 *
 *  TODO Should this/Ramp/RampStartActions/RampStopActions be combined?
 */
class RampDetector {
    private final Bangable rampStartAction;
    private final Bangable rampStopAction;

    private float lastSample = 0;
    private float lastDelta = 0;
    private boolean about50PercentSinceLastBang = true;
    private boolean above90PercentSinceLastBang = true;
    private boolean isStopped = true;

    RampDetector(Bangable rampStartAction, Bangable rampStopAction) {
        this.rampStartAction = rampStartAction;
        this.rampStopAction = rampStopAction;
    }

    void process(float sample) {
        float delta = sample - lastSample;

        if (delta > 0) {
            isStopped = false;
            if (about50PercentSinceLastBang && above90PercentSinceLastBang && sample <= 0.1f) {
                above90PercentSinceLastBang = false;
                about50PercentSinceLastBang = false;
                rampStartAction.bang();
            } else if (about50PercentSinceLastBang && sample >= 0.9f) {
                above90PercentSinceLastBang = true;
            } else if (sample > 0.4f && sample < 0.6f) {
                about50PercentSinceLastBang = true;
            }
        } else if (!isStopped && (delta == 0) && (lastDelta == 0)) {
            isStopped = true;
            above90PercentSinceLastBang = true;
            about50PercentSinceLastBang = true;
            rampStopAction.bang();
        }

        lastDelta = delta;
        lastSample = sample;
    }
}
