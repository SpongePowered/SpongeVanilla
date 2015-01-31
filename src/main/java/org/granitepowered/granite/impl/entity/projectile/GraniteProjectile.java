package org.granitepowered.granite.impl.entity.projectile;

import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.mc.MCEntity;
import org.spongepowered.api.entity.projectile.Projectile;

public abstract class GraniteProjectile<T extends MCEntity> extends GraniteEntity<T> implements Projectile {

    public GraniteProjectile(T obj) {
        super(obj);
    }
}
