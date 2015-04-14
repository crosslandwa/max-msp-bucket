package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxComms;

/**
 * Accepts an input ramp of 0 -> 1
 * Outputs every time the input passes some threshold (in either direction)
 *
 * Threshold values = 0 .. N * (1 / N) where N is the number of steps
 */
class StepDetector {
    private final MaxComms maxComms;
    private int numberOfSteps = 4;
    private float thresholdDelta = 1f / numberOfSteps;
    private Integer lastStep = null;

    StepDetector(MaxComms maxComms) {
        this.maxComms = maxComms;
    }

    void process(float sample) {
        Integer step = ((int) (sample / thresholdDelta)) % numberOfSteps;
        if (!step.equals(lastStep)) {
            maxComms.outlet(step);
        }

        lastStep = step;
    }

    StepDetector setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
        thresholdDelta = 1f / numberOfSteps;
        return this;
    }

    StepDetector reset() {
        lastStep = null;
        return this;
    }
}
