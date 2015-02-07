package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntitySheep;
import org.spongepowered.api.entity.living.animal.DyeColor;
import org.spongepowered.api.entity.living.animal.Sheep;

public class GraniteEntitySheep extends GraniteEntityAnimal<MCEntitySheep> implements Sheep {

    public GraniteEntitySheep(MCEntitySheep obj) {
        super(obj);
    }

    @Override
    public boolean isSheared() {
        return ((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 16) != 0;
    }

    @Override
    public void setSheared(boolean sheared) {
        byte isSheared = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(16, (byte) (sheared ? isSheared | 16 : isSheared & -17));
    }

    @Override
    public DyeColor getColor() {
        return Granite.instance.getGameRegistry().dyeColors.get((byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() & 15);
    }

    @Override
    public void setColor(DyeColor dyeColor) {
        byte currentColor = (byte) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject();
        obj.fieldGet$dataWatcher().updateObject(16, ((byte) (currentColor & 240 | ((GraniteMeta) dyeColor).type) & 15));
    }
}
