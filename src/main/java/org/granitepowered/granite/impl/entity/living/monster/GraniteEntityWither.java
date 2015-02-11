package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityWither;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.monster.Wither;
import org.spongepowered.api.entity.projectile.Projectile;

import java.util.List;

public class GraniteEntityWither extends GraniteEntityMonster<MCEntityWither> implements Wither {

    public GraniteEntityWither(MCEntityWither obj) {
        super(obj);
    }

    @Override
    public int getInvulnerableTicks() {
        return (int) obj.fieldGet$dataWatcher().getWatchedObject(20).fieldGet$watchedObject();
    }

    @Override
    public void setInvulnerableTicks(int invulnerableTicks) {
        obj.fieldGet$dataWatcher().updateObject(20, invulnerableTicks);
    }

    @Override
    public List<Living> getTargets() {
        throw new NotImplementedException("");
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
