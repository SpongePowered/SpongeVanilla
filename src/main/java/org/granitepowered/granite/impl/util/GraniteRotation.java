package org.granitepowered.granite.impl.util;

import org.spongepowered.api.util.rotation.Rotation;

public class GraniteRotation implements Rotation {

    int angle;

    public GraniteRotation(int angle) {
        this.angle = angle;
    }

    @Override
    public int getAngle() {
        return this.angle;
    }
}
