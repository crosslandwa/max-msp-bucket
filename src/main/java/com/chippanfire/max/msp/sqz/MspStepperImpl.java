package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxComms;

import java.util.List;

/**
 * A signal rate stepper to drive a sequencer.
 *
 * Takes an input ramp with an assumed frequency of 1 'beat', and outputs a new step (between 0 and N) every time
 * the input ramp rolls over
 *
 * Additionally implements swing via the provision of a list of values that describe a 'swing profile'
 *  - a list of 0, 0.5, 1 (a linear line) would introduce no swing
 *  - a list of 0, 0.3, 1 (a non-linear line) causes every second beat to be late
 *
 * The list length controls over how many beats the swing profile is repeated
 */
class MspStepperImpl {
    private final Ramp ramp;
    private final StepDetector stepDetector;
    private final RampDetector rampDetector;
    private Swing swing;

    MspStepperImpl(MaxComms maxComms, int numberOfSteps) {
        stepDetector = new StepDetector(maxComms).setNumberOfSteps(numberOfSteps);

        ramp = new Ramp().setNumberOfSteps(numberOfSteps);
        rampDetector = new RampDetector(new RampStartActions(ramp), new RampStopActions(stepDetector, ramp));

        swing = Swing.unswung(numberOfSteps);
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
        swing = swing.withSwingValues(values);
    }

    void rampTimeInSamples(float rampTimeInSamples) {
        ramp.setRampTime(rampTimeInSamples);
    }

    void jumpToStep(int nextStep) {
        ramp.setNextStep(nextStep);
    }

    synchronized void numberOfSteps(int numberOfSteps) {
        stepDetector.setNumberOfSteps(numberOfSteps);
        swing = swing.withNumberOfSteps(numberOfSteps);
        ramp.setNumberOfSteps(numberOfSteps);
    }
}
