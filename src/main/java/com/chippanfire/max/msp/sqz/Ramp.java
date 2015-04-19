package com.chippanfire.max.msp.sqz;

/**
 * Ramps from 0 to 1 over 'number of steps' x N samples
 *  - outputs an incremental value each time advance() called
 *  - jumps to the next step when step() called
 *  - ramp will stop incrementing when advance() called if it has reached the 'max' for the given step (max then held)
 *  - can be stopped to start counting from 0 again
 */
class Ramp {
    private Integer targetNextStep = null;

    private int numberOfSteps = 4;
    private int step = 0;

    private float nextSample = 0;
    private float increment = 0;
    private boolean isPlaying = false;

    Ramp setRampTime(float rampLengthInSamples) {
        increment = 1 / rampLengthInSamples;
        return this;
    }

    Ramp start() {
        isPlaying = true;
        return this;
    }

    Ramp stop() {
        isPlaying = false;
        nextSample = 0;
        step = 0;
        targetNextStep = null;
        return this;
    }

    boolean isPlaying() {
        return isPlaying;
    }

    float advance() {
        float output = (nextSample + step) / numberOfSteps;
        nextSample = Math.min(nextSample + increment, 1);
        return output;
    }

    Ramp step() {
        if (targetNextStep != null) {
            step = targetNextStep;
            targetNextStep = null;
        } else {
            step = ++step % numberOfSteps;
        }
        nextSample = 0;
        return this;
    }

    /**
     * Set max value that is stepped to. Note that a max of 32 results in output of 0 -> 31.9999 (i.e. max never reached)
     */
    Ramp setNumberOfSteps(int max) {
        numberOfSteps = max;

        step = step % numberOfSteps;

        return this;
    }

    Ramp setNextStep(int nextStep) {
        targetNextStep = nextStep % numberOfSteps;
        return this;
    }
}
