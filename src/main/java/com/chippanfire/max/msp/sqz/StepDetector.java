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
    private float tenthThresholdDelta = thresholdDelta / 10;
    private boolean isLastSampleCloseToThreshold = false;

    StepDetector(MaxComms maxComms) {
        this.maxComms = maxComms;
    }

    void process(float sample) {
        boolean isSampleCloseToThreshold = (sample % thresholdDelta) < tenthThresholdDelta;

        if (isSampleCloseToThreshold && !isLastSampleCloseToThreshold) {
            maxComms.outlet(((int) (sample / thresholdDelta)) % numberOfSteps);
        }

        isLastSampleCloseToThreshold = isSampleCloseToThreshold;
    }

    StepDetector setNumberOfSteps(int numberOfSteps) {
        this.numberOfSteps = numberOfSteps;
        thresholdDelta = 1f / numberOfSteps;
        tenthThresholdDelta = thresholdDelta / 10; // Should this be calculate based on number of steps & ramp time?
        return this;
    }

    StepDetector reset() {
        isLastSampleCloseToThreshold = false;
        return this;
    }
}
