package org.granitepowered.granite.impl.entity.living.monster;

import com.google.common.base.Optional;
import org.granitepowered.granite.mc.MCEntityGiantZombie;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.monster.Giant;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntityGiant extends GraniteEntityMonster<MCEntityGiantZombie> implements Giant {

    public GraniteEntityGiant(MCEntityGiantZombie obj) {
        super(obj);
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$equipment()[4]));
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        obj.fieldGet$equipment()[4] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$equipment()[3]));
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        obj.fieldGet$equipment()[3] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$equipment()[2]));
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        obj.fieldGet$equipment()[2] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$equipment()[1]));
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        obj.fieldGet$equipment()[1] = MinecraftUtils.unwrap(itemStack);
    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$equipment()[0]));
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {
        obj.fieldGet$equipment()[0] = MinecraftUtils.unwrap(itemStack);
    }
}
