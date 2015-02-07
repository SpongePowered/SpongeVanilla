package org.granitepowered.granite.impl.entity.living.animal;

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.mc.MCEntityTameable;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.entity.living.Sittable;
import org.spongepowered.api.entity.living.Tameable;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.living.animal.Wolf;

public class GraniteEntityTameable<T extends MCEntityTameable> extends GraniteEntityAnimal<T> implements Sittable, Tameable {

    public GraniteEntityTameable(T obj) {
        super(obj);
    }

    @Override
    public boolean isSitting() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 1) != 0;
    }

    @Override
    public void setSitting(boolean sitting) {
        byte isSitting = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(16, sitting ? Byte.valueOf((byte) (isSitting | 1)) : Byte.valueOf((byte) (isSitting | -2)));
    }

    @Override
    public boolean isTamed() {
        if (this instanceof Horse) {
            return ((int) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 2) != 0;
        } else {
            return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 4) != 0;
        }
    }

    @Override
    public void setTamed(boolean tamed) {
        if (this instanceof Horse) {
            obj.fieldGet$dataWatcher().updateObject(2, tamed);
        } else {
            byte isTamed = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
            obj.fieldGet$dataWatcher().updateObject(16, tamed ? Byte.valueOf((byte) (isTamed | 4)) : Byte.valueOf((byte) (isTamed & -5)));
            if (this instanceof Wolf) {
                // TODO: Add SharedMonsterAttributes (Base health of 8 when false, Base health of 20 when true)
                // TODO: Set attack damage to 4
            }
        }
    }

    @Override
    public Optional<Tamer> getOwner() {
        return Optional.fromNullable((Tamer) Granite.getInstance().getServer()
                .getPlayer((String) obj.fieldGet$dataWatcher().getWatchedObject(21).fieldGet$watchedObject()));
    }

    @Override
    public void setOwner(Tamer tamer) {
        obj.fieldGet$dataWatcher().updateObject(21, tamer.getUniqueId().toString());
        setTamed(true);
    }
}
