package com.chippanfire.max.msp;

import com.cycling74.max.MaxObject;

/**
 * Wrapper for a MaxObject to provide proxy to a Max outlet
 */
class MaxProxy implements MaxComms {
    private final MaxObject maxObject;
    private final int outletIndex;

    public MaxProxy(MaxObject maxObject, int outletIndex) {
        this.maxObject = maxObject;
        this.outletIndex = outletIndex;
    }

    public boolean outlet(int value) {
        return maxObject.outlet(outletIndex, value);
    }
}
