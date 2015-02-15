package org.granitepowered.granite.impl.entity.living.monster;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.mc.MCEntityZombie;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.monster.Zombie;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Random;

public class GraniteEntityZombie<T extends MCEntityZombie> extends GraniteEntityMonster<T> implements Zombie {

    public GraniteEntityZombie(T obj) {
        super(obj);
    }

    @Override
    public boolean isVillagerZombie() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(13).fieldGet$watchedObject() == 1;
    }

    @Override
    public void setVillagerZombie(boolean villager) {
        obj.fieldGet$dataWatcher().updateObject(13, villager ? (byte) 1 : (byte) 0 );
    }

    @Override
    public int getAge() {
        return isBaby() ? -2400 : 0;
    }

    @Override
    public void setAge(int age) {
        obj.setChild(age < 0);
    }

    @Override
    public void setBaby() {
        obj.setChild(true);
    }

    @Override
    public void setAdult() {
        obj.setChild(false);
    }

    @Override
    public boolean isBaby() {
        return (byte) obj.fieldGet$dataWatcher().getWatchedObject(12).fieldGet$watchedObject() == 1;
    }

    @Override
    public boolean canBreed() {
        return obj.fieldGet$attributeMap().getAttributeInstance(obj.fieldGet$reinforcementsAttribute()).getAttributeValue() > 0;
    }

    @Override
    public void setBreeding(boolean breeding) {
        obj.fieldGet$attributeMap().getAttributeInstance(obj.fieldGet$reinforcementsAttribute()).setBaseValue(breeding ? new Random().nextDouble() * 0.10000000149011612D : 0);
    }

    @Override
    public void setScaleForAge() {
        obj.setScale(isBaby() ? 0.5F : 1.0F);
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
