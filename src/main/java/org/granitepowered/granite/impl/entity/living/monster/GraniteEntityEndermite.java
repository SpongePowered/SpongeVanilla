package org.granitepowered.granite.impl.entity.living.monster;

import org.granitepowered.granite.mc.MCEntityEndermite;
import org.spongepowered.api.entity.living.monster.Endermite;

public class GraniteEntityEndermite extends GraniteEntityMonster<MCEntityEndermite> implements Endermite {

    public GraniteEntityEndermite(MCEntityEndermite obj) {
        super(obj);
    }

    @Override
    public boolean isPlayerCreated() {
        return obj.fieldGet$playerSpawned();
    }

    @Override
    public void setPlayerCreated(boolean playerCreated) {
        obj.fieldSet$playerSpawned(playerCreated);
    }
}
