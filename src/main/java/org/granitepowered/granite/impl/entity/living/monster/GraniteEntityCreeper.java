package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityCreeper;
import org.spongepowered.api.entity.living.monster.Creeper;

public class GraniteEntityCreeper extends GraniteEntityMonster<MCEntityCreeper> implements Creeper {

    public GraniteEntityCreeper(MCEntityCreeper obj) {
        super(obj);
    }

    @Override
    public boolean isPowered() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(17).fieldGet$watchedObject() == 1;
    }

    @Override
    public void setPowered(boolean powered) {
        obj.fieldGet$dataWatcher().updateObject(17, powered ? (byte) 1 : (byte) 0);
    }

    @Override
    public int getExplosionRadius() {
        return obj.fieldGet$explosionRadius();
    }

    @Override
    public void setExplosionRadius(int explosionRadius) {
        obj.fieldSet$explosionRadius(explosionRadius);
    }

    @Override
    public void ignite() {
        obj.fieldGet$dataWatcher().updateObject(18, (byte) 1);
    }

    @Override
    public void ignite(int fuseTime) {
        obj.fieldSet$lastActiveTime(0);
        obj.fieldSet$fuseTime(fuseTime);
        ignite();
    }

    @Override
    public int getFuseDuration() {
        return obj.fieldGet$fuseTime() - obj.fieldGet$lastActiveTime();
    }

    @Override
    public void setFuseDuration(int fuseDuration) {
        obj.fieldSet$timeSinceIgnited(obj.fieldGet$fuseTime() - fuseDuration);
    }

    @Override
    public void detonate() {
        obj.detonate();
    }
}
