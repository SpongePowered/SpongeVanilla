package org.granitepowered.granite.impl;

import com.google.common.base.Optional;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCGameProfile;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.entity.player.User;

import java.util.UUID;

public class GraniteGameProfile extends Composite<MCGameProfile> implements GameProfile {

    public GraniteGameProfile(Object obj) {
        super(obj);
    }

    @Override
    public String getName() {
        return obj.fieldGet$name();
    }

    @Override
    public Optional<User> getUser() {
        return null;
    }

    @Override
    public UUID getUniqueId() {
        return obj.fieldGet$id();
    }
}
