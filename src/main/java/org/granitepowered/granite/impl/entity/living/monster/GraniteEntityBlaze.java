package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityBlaze;
import org.spongepowered.api.entity.living.monster.Blaze;
import org.spongepowered.api.entity.projectile.Projectile;

public class GraniteEntityBlaze extends GraniteEntityMonster<MCEntityBlaze> implements Blaze {

    public GraniteEntityBlaze(MCEntityBlaze obj) {
        super(obj);
    }

    @Override
    public boolean isOnFire() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 1) != 0;
    }

    @Override
    public void setOnFire(boolean onFire) {
        obj.fieldGet$dataWatcher().updateObject(16, onFire ? (byte) 1 : (byte) 0);
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass) {
        // TODO: launch Projectiles
        throw new NotImplementedException("");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass, Vector3f vector3f) {
        // TODO: launch Projectiles
        throw new NotImplementedException("");
    }
}
