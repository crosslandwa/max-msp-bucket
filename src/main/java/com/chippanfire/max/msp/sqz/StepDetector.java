package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxComms;

/**
 * Accepts an input ramp of 0 -> 1
 * Outputs every time the input passes some threshold (in either direction)
 *
 * Threshold values = 0 .. N * (1 / N) where N is the number of steps
 *
 * At 32 steps, threshold delta is 0.03125
 * At 44.1Khz, input ramp of 4n at 60bpm (i.e. one ramp per second), one tenth threshold ~= 138 samples (0.003s) ~= 128th / 5
 *
 * At 44.1Khz, input ramp of 16n at 120bpm takes 0.125s (5512.5 samples), there sample delta (untransformed) is 0.00018
 * One tenth threshold ~ 17 samples
 *  - i.e. we will detect steps at very close to when transition 'should be', but could miss steps if the incoming rate
 *  of change is too great...
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
