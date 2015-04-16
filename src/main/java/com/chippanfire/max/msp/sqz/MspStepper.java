package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.MaxCommsFactory;
import com.cycling74.max.Atom;
import com.cycling74.max.DataTypes;
import com.cycling74.msp.MSPPerformer;
import com.cycling74.msp.MSPSignal;

import java.util.ArrayList;
import java.util.List;

/**
 * This class acts as the adaptor layer between Max (via extension of MaxObject) and the MspStepperImpl implementation
 */
public class MspStepper extends MSPPerformer {
    private static final String[] INLET_ASSIST = new String[] {"input (sig)"};
    private static final String[] OUTLET_ASSIST = new String[] {"step (int)"};

    private final MspStepperImpl impl = new MspStepperImpl(MaxCommsFactory.proxy(this, 0), 32);

    public MspStepper() {
        declareInlets(new int[]{SIGNAL});
        declareOutlets(new int[]{DataTypes.INT});

        setInletAssist(INLET_ASSIST);
        setOutletAssist(OUTLET_ASSIST);
    }

    public void rampLengthInSamples(float stepSizeInSamples) {
        impl.rampTimeInSamples(stepSizeInSamples);
    }

    public void jumpToStep(int nextStep) {
        impl.jumpToStep(nextStep);
    }

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

        impl.updateSwing(swingValues);
    }

    @Override
    public void dspsetup(MSPSignal[] ins, MSPSignal[] outs) {
        // No setup required
    }

    @Override
    public void perform(MSPSignal[] ins, MSPSignal[] outs) {
        float [] inputRamp = ins[0].vec;

        for (int index = 0; index < inputRamp.length; index++) {
            impl.process(inputRamp[index]);
        }
    }

}
