package org.granitepowered.granite.impl.entity.living.animal;

import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityRabbit;
import org.spongepowered.api.entity.living.animal.Rabbit;
import org.spongepowered.api.entity.living.animal.RabbitType;
import org.spongepowered.api.text.translation.Translations;

public class GraniteEntityRabbit extends GraniteEntityAnimal<MCEntityRabbit> implements Rabbit {

    public GraniteEntityRabbit(MCEntityRabbit obj) {
        super(obj);
    }

    @Override
    public RabbitType getRabbitType() {
        return Granite.instance.getGameRegistry().rabbits.get(obj.fieldGet$dataWatcher().getWatchedObject(18).fieldGet$watchedObject());
    }

    @Override
    public void setRabbitType(RabbitType rabbitType) {
        int type = ((GraniteMeta) rabbitType).getType();

        if (type == 99) {
            // TODO: Add and Remove AI Tasks
            setCustomName(Translations.of("entity.KillerBunny.name").toString());
        }

        obj.fieldGet$dataWatcher().updateObject(18, (byte) (type << 8));
    }
}
