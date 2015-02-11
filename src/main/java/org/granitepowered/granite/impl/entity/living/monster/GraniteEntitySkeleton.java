package org.granitepowered.granite.impl.entity.living.monster;

import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntitySkeleton;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.monster.Skeleton;
import org.spongepowered.api.entity.living.monster.SkeletonType;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntitySkeleton extends GraniteEntityMonster<MCEntitySkeleton> implements Skeleton {

    public GraniteEntitySkeleton(MCEntitySkeleton obj) {
        super(obj);
    }

    @Override
    public SkeletonType getSkeletonType() {
        return Granite.instance.getGameRegistry().skeletonTypes.get(obj.fieldGet$dataWatcher().getWatchedObject(13).fieldGet$watchedObject());
    }

    @Override
    public void setSkeletonType(SkeletonType skeletonType) {
        obj.setSkeletonType(((GraniteMeta) skeletonType).getType());
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

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass) {
        throw new NotImplementedException("");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> aClass, Vector3f vector3f) {
        throw new NotImplementedException("");
    }
}
