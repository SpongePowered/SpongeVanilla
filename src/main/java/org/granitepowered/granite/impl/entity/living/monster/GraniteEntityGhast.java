package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import org.granitepowered.granite.impl.entity.living.GraniteEntityAerial;
import org.granitepowered.granite.mc.MCEntityGhast;
import org.spongepowered.api.entity.living.monster.Ghast;
import org.spongepowered.api.entity.projectile.Projectile;

public class GraniteEntityGhast extends GraniteEntityAerial<MCEntityGhast> implements Ghast {

    public GraniteEntityGhast(MCEntityGhast obj) {
        super(obj);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass) {
        return null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass, Vector3f vector3f) {
        return null;
    }
}
