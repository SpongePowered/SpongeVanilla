package org.granitepowered.granite.impl.item;

import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCEnchantment;
import org.granitepowered.granite.mc.MCItemStack;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEnchantment extends Composite<MCEnchantment> implements Enchantment {

    public GraniteEnchantment(Object obj) {
        super(obj);
    }

    @Override
    public String getId() {
        return "minecraft:" + obj.fieldGet$name();
    }

    @Override
    public int getWeight() {
        return obj.fieldGet$weight();
    }

    @Override
    public int getMinimumLevel() {
        return obj.getMinLevel();
    }

    @Override
    public int getMaximumLevel() {
        return obj.getMaxLevel();
    }

    @Override
    public int getMinimumEnchantabilityForLevel(int level) {
        return obj.getMinEnchantability(level);
    }

    @Override
    public int getMaximumEnchantabilityForLevel(int level) {
        return obj.getMaxEnchantability(level);
    }

    @Override
    public boolean canBeAppliedToStack(ItemStack itemStack) {
        return obj.canApply((MCItemStack) MinecraftUtils.unwrap(itemStack));
    }

    @Override
    public boolean canBeAppliedByTable(ItemStack itemStack) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return obj.canApplyTogether((MCEnchantment) MinecraftUtils.unwrap(enchantment));
    }
}
