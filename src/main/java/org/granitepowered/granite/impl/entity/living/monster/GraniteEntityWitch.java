package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityWitch;
import org.spongepowered.api.entity.living.monster.Witch;
import org.spongepowered.api.entity.projectile.Projectile;

public class GraniteEntityWitch extends GraniteEntityMonster<MCEntityWitch> implements Witch {

    public GraniteEntityWitch(MCEntityWitch obj) {
        super(obj);
    }

    @Override
    public boolean isAggressive() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(21).fieldGet$watchedObject() == 1;
    }

    @Override
    public void setAggressive(boolean aggressive) {
        obj.fieldGet$dataWatcher().updateObject(21, aggressive ? (byte) 1 : (byte) 0 );
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass, Vector3f vector3f) {
        throw new NotImplementedException("");
    }
}
