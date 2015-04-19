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
 *
 *  TODO samplesSinceLastTransition is an untested guard against super fast input ramps (seen when starting in Max) - add test!
 */
class RampDetector {
    private final Bangable rampStartAction;
    private final Bangable rampStopAction;

    private float lastSample = 0;
    private float lastDelta = 0;
    private boolean about50PercentSinceLastBang = true;
    private boolean above90PercentSinceLastBang = true;
    private boolean isStopped = true;
    private float thirdExpectedRampTime = 10000f;

    private int samplesSinceLastTransition = 0;

    RampDetector(Bangable rampStartAction, Bangable rampStopAction) {
        this.rampStartAction = rampStartAction;
        this.rampStopAction = rampStopAction;
    }

    void process(float sample) {
        float delta = sample - lastSample;

        if (delta > 0) {
            isStopped = false;
            samplesSinceLastTransition += 1;
            if (about50PercentSinceLastBang && above90PercentSinceLastBang && below10Percent(sample)) {
                above90PercentSinceLastBang = false;
                about50PercentSinceLastBang = false;
                if ((samplesSinceLastTransition <= 1) || (samplesSinceLastTransition >= thirdExpectedRampTime)) {
                    rampStartAction.bang();
                }
                samplesSinceLastTransition = 0;
            } else if (!about50PercentSinceLastBang && about50Percent(sample)) {
                about50PercentSinceLastBang = true;
                samplesSinceLastTransition = 0;
            } else if (about50PercentSinceLastBang && !above90PercentSinceLastBang && over90Percent(sample)) {
                above90PercentSinceLastBang = true;
            }
        } else if (!isStopped && (delta == 0) && (lastDelta == 0)) {
            isStopped = true;
            above90PercentSinceLastBang = true;
            about50PercentSinceLastBang = true;
            samplesSinceLastTransition = 0;
            rampStopAction.bang();
        }

        lastDelta = delta;
        lastSample = sample;
    }

    private boolean below10Percent(float sample) {
        return sample <= 0.1f;
    }

    private boolean over90Percent(float sample) {
        return sample >= 0.9f;
    }

    private boolean about50Percent(float sample) {
        return sample > 0.4f && sample < 0.6f;
    }

    RampDetector setExpectedRampTime(float rampTimeInSamples) {
        thirdExpectedRampTime = rampTimeInSamples / 3;
        return this;
    }
}
