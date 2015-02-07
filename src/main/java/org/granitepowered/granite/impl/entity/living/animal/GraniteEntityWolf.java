package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityWolf;
import org.spongepowered.api.entity.living.animal.DyeColor;
import org.spongepowered.api.entity.living.animal.Wolf;

public class GraniteEntityWolf extends GraniteEntityTameable<MCEntityWolf> implements Wolf {

    public GraniteEntityWolf(MCEntityWolf obj) {
        super(obj);
    }

    @Override
    public DyeColor getColor() {
        return Granite.instance.getGameRegistry().dyeColors.get((byte) obj.fieldGet$dataWatcher().getWatchedObject(20).fieldGet$watchedObject() & 15);
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        obj.fieldGet$dataWatcher().updateObject(20, ((byte) (((GraniteMeta) dyeColor).type) & 15));
    }
}
