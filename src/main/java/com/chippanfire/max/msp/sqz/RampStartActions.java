package com.chippanfire.max.msp.sqz;

import com.chippanfire.max.msp.Bangable;

class RampStartActions implements Bangable {
    private final Ramp ramp;

    RampStartActions(Ramp ramp) {
        this.ramp = ramp;
    }


    public void bang() {
        if (!ramp.isPlaying()) {
            ramp.start();
        } else {
            ramp.step();
        }
    }
}
