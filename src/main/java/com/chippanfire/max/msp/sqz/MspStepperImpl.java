package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxComms;

import java.util.List;

/**
 * A signal rate stepper to drive a sequencer.
 *
 * Takes an input ramp with an assumed frequency of 1 'beat', and outputs a new step (between 0 and N) every time
 * the input ramp rolls over
 */
class MspStepperImpl {
    private static final int NUMBER_OF_STEPS = 32;

    private final Ramp ramp;
    private final StepDetector stepDetector;
    private final RampDetector rampDetector;
    private Swing swing = Swing.unswung();

    MspStepperImpl(MaxComms maxComms) {
        stepDetector = new StepDetector(maxComms).setNumberOfSteps(NUMBER_OF_STEPS);

        ramp = new Ramp().setNumberOfSteps(NUMBER_OF_STEPS);
        rampDetector = new RampDetector(new RampStartActions(ramp), new RampStopActions(stepDetector, ramp));
    }

    void process(float sample) {
        rampDetector.process(sample);
        if (ramp.isPlaying()) {
            stepDetector.process(swing(ramp.advance()));
        }
    }

    private synchronized float swing(float index) {
        return swing.process(index);
    }

    /**
     * Update swing settings
     */
    synchronized void updateSwing(List<Float> values) {
        swing = new Swing(NUMBER_OF_STEPS, values);
    }

    void rampTimeInSamples(float rampTimeInSamples) {
        ramp.setRampTime(rampTimeInSamples);
    }

    void jumpToStep(int nextStep) {
        ramp.setNextStep(nextStep);
    }
}
