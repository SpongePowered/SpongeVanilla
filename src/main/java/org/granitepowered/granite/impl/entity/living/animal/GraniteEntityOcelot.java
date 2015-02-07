package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityOcelot;
import org.spongepowered.api.entity.living.animal.Ocelot;
import org.spongepowered.api.entity.living.animal.OcelotType;

public class GraniteEntityOcelot extends GraniteEntityTameable<MCEntityOcelot> implements Ocelot {

    public GraniteEntityOcelot(MCEntityOcelot obj) {
        super(obj);
    }

    @Override
    public OcelotType getOcelotType() {
        return Granite.instance.getGameRegistry().ocelots.get(obj.fieldGet$dataWatcher().getWatchedObject(18).fieldGet$watchedObject());
    }

    @Override
    public void setOcelotType(OcelotType ocelotType) {
        obj.fieldGet$dataWatcher().updateObject(18, ((GraniteMeta) ocelotType).type << 8);
    }
}
