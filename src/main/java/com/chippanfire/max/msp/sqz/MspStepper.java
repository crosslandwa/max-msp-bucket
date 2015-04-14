package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxProxy;
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.msp.MSPPerformer;
import com.cycling74.msp.MSPSignal;

import java.util.ArrayList;
import java.util.List;

/**
 * A signal rate stepper to drive a sequencer.
 *
 * This class acts as the adaptor layer between Max (via extension of MaxObject) and the MspStepper implementation
 *
 * Takes an input ramp with an assumed frequency of 1 'beat', and outputs a new step (between 0 and N) every time
 * the input ramp rolls over
 */
public class MspStepper extends MSPPerformer {
    private static final String[] INLET_ASSIST = new String[] {"input (sig)"};
    private static final String[] OUTLET_ASSIST = new String[] {"step (int)"};

    public static final int NUMBER_OF_STEPS = 32;

    private final Ramp ramp = new Ramp().setNumberOfSteps(NUMBER_OF_STEPS);
    private final StepDetector stepDetector = new StepDetector(new MaxProxy(this, 0)).setNumberOfSteps(NUMBER_OF_STEPS);
    private final RampStartActions rampStartAction = new RampStartActions(ramp);
    private final RampStopActions rampStopAction = new RampStopActions(stepDetector, ramp);
    private final RampDetector rampDetector = new RampDetector(rampStartAction, rampStopAction);
    private Swing swing = Swing.unswung();

    public MspStepper() {
        declareInlets(new int[]{SIGNAL});
        declareOutlets(new int[]{DataTypes.INT});

        setInletAssist(INLET_ASSIST);
        setOutletAssist(OUTLET_ASSIST);
    }

    public void rampLengthInSamples(float stepSizeInSamples) {
        ramp.setRampTime(stepSizeInSamples);
    }

    public void jumpToStep(int nextStep) {
        ramp.setNextStep(nextStep);
    }

    /**
     * Update swing settings
     *
     * TODO - What happens when this.swing re-instantiated in message thread (but processing occurring in audio thread)?
     * @param args
     */
    public void swing (Atom[] args) {
        List<Float> swingValues = new ArrayList<Float>();

        for (int index = 0; index < args.length; index++) {
            Atom arg = args[index];
            if (arg.isFloat() || arg.isInt()) {
                swingValues.add(args[index].toFloat());
            } else {
                post("swing takes a list of floats (between 0 and 1) - none float argument given: " + args[index]);
                return;
            }
        }

        swing = new Swing(NUMBER_OF_STEPS, swingValues);
    }

    @Override
    public void dspsetup(MSPSignal[] ins, MSPSignal[] outs) {
        // No setup required
    }

    @Override
    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        float [] inputRamp = ins[0].vec;

        /**
         * TODO - move this micro implementation into a class, making this class a pure adaptor
         */
        for (int index = 0; index < inputRamp.length; index++) {
            rampDetector.process(inputRamp[index]);
            if (ramp.isPlaying()) {
                stepDetector.process(swing.process(ramp.advance()));
            }
        }
    }

}
