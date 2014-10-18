package org.granitemc.granite.api.entity.item;

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.item.ItemStack;

public interface EntityItem extends Entity {

    void setAgeToCreativeDespawnTime();

    void dealFireDamage(int amount);

    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    String getName();

    void travelToDimension(int dimensionId);

    ItemStack getEntityItem();

    void setEntityItemStack(ItemStack stack);

    String getOwner();

    void setOwner(String owner);

    String getThrower();

    void setThrower(String thrower);

    void setDefaultPickupDelay();

    void setNoPickupDelay();

    void setInfinitePickupDelay();

    void setPickupDelay(int ticks);

    boolean cannotPickup();

    void setNoDespawn();

    /*void func_174870_v();*/

}
