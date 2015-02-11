package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityGuardian;
import org.spongepowered.api.entity.living.monster.Guardian;

public class GraniteEntityGuardian extends GraniteEntityMonster<MCEntityGuardian> implements Guardian{

    public GraniteEntityGuardian(MCEntityGuardian obj) {
        super(obj);
    }

    @Override
    public boolean isElder() {
        return ((int) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 4) != 0;
    }

    @Override
    public void setElder(boolean elder) {
        int object = (int) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(16, elder ? (object | 4) : (object & ~4));
    }
}
