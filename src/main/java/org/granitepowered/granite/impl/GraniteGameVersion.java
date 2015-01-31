package org.granitepowered.granite.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.MinecraftVersion;

public class GraniteGameVersion implements MinecraftVersion {

    private final String version;
    private final int protocol;

    public GraniteGameVersion(String version, int protocol) {
        this.version = version;
        this.protocol = protocol;
    }

    @Override
    public String getName() {
        return this.version;
    }

    public int getProtocol() {
        return this.protocol;
    }

    @Override
    public boolean isLegacy() {
        return this.protocol < 47;
    }

    @Override
    public int compareTo(MinecraftVersion minecraftVersion) {
        throw new NotImplementedException("");
    }
}
