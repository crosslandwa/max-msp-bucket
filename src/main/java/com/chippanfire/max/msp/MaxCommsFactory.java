package com.chippanfire.max.msp;

import com.cycling74.max.MaxObject;

public class MaxCommsFactory {

    public static MaxComms proxy(MaxObject maxObject, int outletIndex) {
        return new MaxProxy(maxObject, outletIndex);
    }
}
