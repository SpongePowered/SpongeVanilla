package org.granitepowered.granite.block.meta;

import org.spongepowered.api.block.meta.SkullType;

public class GraniteSkullType implements SkullType {

    private final byte id;
    private final String name;

    public GraniteSkullType(byte id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public byte getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
