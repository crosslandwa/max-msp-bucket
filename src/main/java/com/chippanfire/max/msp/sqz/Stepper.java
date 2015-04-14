package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxComms;

class Stepper {
    private static final int INIT = -1;

    private final MaxComms maxComms;
    private int numberOfSteps = 32;
    private int step = INIT;

    Stepper(MaxComms maxComms) {
        this.maxComms = maxComms;
    }

    /**
     * Increments step and outputs new step
     */
    Stepper step() {
        step = ++step % numberOfSteps;
        maxComms.outlet(step);
        return this;
    }

    Stepper reset() {
        step = INIT;
        return this;
    }

    /**
     * Set max value that is counted to. Note that a max of 32 results in output of 0 -> 31
     * @param max
     * @return
     */
    Stepper setMax(int max) {
        numberOfSteps = max;

        step = step % numberOfSteps;

        return this;
    }

    Stepper setNextStep(int nextStep) {
        if (nextStep == 0) {
            step = numberOfSteps - 1;
        } else {
            step = (nextStep - 1) % numberOfSteps;
        }
        return this;
    }
}
