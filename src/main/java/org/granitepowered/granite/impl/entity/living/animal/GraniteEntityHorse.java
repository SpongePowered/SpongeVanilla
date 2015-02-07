package org.granitepowered.granite.impl.entity.living.animal;

import com.google.common.base.Optional;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityHorse;
import org.granitepowered.granite.mc.MCItemStack;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.animal.Horse;
import org.spongepowered.api.entity.living.animal.HorseColor;
import org.spongepowered.api.entity.living.animal.HorseStyle;
import org.spongepowered.api.entity.living.animal.HorseVariant;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntityHorse extends GraniteEntityTameable<MCEntityHorse> implements Horse {

    public GraniteEntityHorse(MCEntityHorse obj) {
        super(obj);
    }

    @Override
    public HorseStyle getStyle() {
        return Granite.getInstance().getGameRegistry().horseStyles
                .get(((int) obj.fieldGet$dataWatcher().getWatchedObject(20).fieldGet$watchedObject()) & 0xFF);
    }

    @Override
    public void setStyle(HorseStyle horseStyle) {
        obj.fieldGet$dataWatcher().updateObject(20, ((GraniteMeta) getColor()).type & 0xFF | ((GraniteMeta) horseStyle).type << 8);
    }

    @Override
    public HorseColor getColor() {
        return Granite.getInstance().getGameRegistry().horseColors
                .get(((int) obj.fieldGet$dataWatcher().getWatchedObject(20).fieldGet$watchedObject()) & 0xFF);
    }

    @Override
    public void setColor(HorseColor horseColor) {
        obj.fieldGet$dataWatcher().updateObject(20, ((GraniteMeta) horseColor).type & 0xFF | ((GraniteMeta) getStyle()).type << 8);
    }

    @Override
    public HorseVariant getVariant() {
        return Granite.getInstance().getGameRegistry().horseVariants.get(obj.fieldGet$dataWatcher().getWatchedObject(19).fieldGet$watchedObject());
    }

    @Override
    public void setVariant(HorseVariant horseVariant) {
        obj.fieldGet$dataWatcher().updateObject(19, ((GraniteMeta) horseVariant).type);
    }

    @Override
    public Optional<ItemStack> getSaddle() {
        ItemStack itemStack = MinecraftUtils.wrap(obj.fieldGet$animalChest().fieldGet$inventoryContents()[0]);
        return Optional.fromNullable(itemStack);
    }

    @Override
    public void setSaddle(ItemStack itemStack) {
        if (itemStack.getItem() == ItemTypes.SADDLE) {
            MCItemStack[] inventory = obj.fieldGet$animalChest().fieldGet$inventoryContents();
            inventory[0] = MinecraftUtils.unwrap(itemStack);
            obj.fieldGet$animalChest().fieldSet$inventoryContents(inventory);
        }
    }
}
