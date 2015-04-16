package com.chippanfire.max.msp.sqz;

import java.util.Arrays;
import java.util.List;

/**
 * Takes an input of 0 -> 1 which represents N steps
 *
 * Applies a repeated swing ramp over X steps
 */
class Swing {
    private static final List<Float> NO_INTERPOLATION = Arrays.asList(0f, 1f);
    private static final float MIN = 0f;
    private static final float MAX = 1f;

    private final List<Float> swingLookupValues;
    private final int numberOfSteps;
    private final int numberOfSwingSteps;
    private final float lengthOfSwingRepetition;
    private final boolean isUnswung;

    static Swing unswung(int numberOfSteps) {
        return new Swing(numberOfSteps, NO_INTERPOLATION);
    }

    /**
     * @param numberOfSteps
     * @param swingLookupValues
     */
    private Swing (int numberOfSteps, List<Float> swingLookupValues) {
        this.swingLookupValues = swingLookupValues;
        this.numberOfSteps = numberOfSteps;

        numberOfSwingSteps = this.swingLookupValues.size() - 1;
        lengthOfSwingRepetition = (float) numberOfSwingSteps / numberOfSteps;

        isUnswung = NO_INTERPOLATION.equals(this.swingLookupValues);
    }

    Swing withSwingValues(List<Float> swingValues) {
        return new Swing(this.numberOfSteps, swingValues);
    }

    Swing withNumberOfSteps(int numberOfSteps) {
        return new Swing(numberOfSteps, this.swingLookupValues);
    }

    float process (float index) {
        if (isUnswung) {
            return index;
        }

        float zeroToOneThroughASwingRepetition = (index % lengthOfSwingRepetition) / lengthOfSwingRepetition;
        float zeroToOneWithSwingApplied = applySwing(zeroToOneThroughASwingRepetition);

        int wholeNumberOfCompleteSwingRepetitions = (int) (index / lengthOfSwingRepetition);

        return (wholeNumberOfCompleteSwingRepetitions + zeroToOneWithSwingApplied) * lengthOfSwingRepetition;
    }

    /**
     * Uses the provided index (0 -> 1) to calculate an interpolated value based on the stored list of swingLookupValues;
     */
    private float applySwing(float normalisedIndex) {
        float denormalisedIndex = normalisedIndex * numberOfSwingSteps;
        int denormalisedIntIndex = (int) denormalisedIndex;
        float lowerBound = swingLookupValues.get(denormalisedIntIndex);
        float indexDiff = denormalisedIndex - denormalisedIntIndex;
        float boundDiff = (swingLookupValues.get(denormalisedIntIndex + 1) - lowerBound) * indexDiff;
        return clippedBetweenZeroAndOne(lowerBound + boundDiff);
    }

    private float clippedBetweenZeroAndOne(float input) {
        return Math.min(MAX, Math.max(MIN, input));
    }
}
