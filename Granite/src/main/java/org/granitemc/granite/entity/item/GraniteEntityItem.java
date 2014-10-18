package org.granitemc.granite.entity.item;

import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.utils.MinecraftUtils;

public class GraniteEntityItem extends GraniteEntity implements EntityItem {

    public GraniteEntityItem(Object parent) {
        super(parent);
    }

    @Override
    public void setAgeToCreativeDespawnTime() {
        invoke("setAgeToCreativeDespawnTime");
    }

    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }*/

    @Override
    public ItemStack getEntityItem() {
        return (ItemStack) MinecraftUtils.wrap(invoke("getEntityItem"));
    }

    @Override
    public void setEntityItemStack(ItemStack itemStack) {
        invoke("setEntityItemStack", ((GraniteItemStack) itemStack).parent);
    }

    @Override
    public String getOwner() {
        return (String) invoke("getOwner");
    }

    @Override
    public void setOwner(String owner) {
        invoke("setOwner", owner);
    }

    @Override
    public String getThrower() {
        return (String) invoke("getThrower");
    }

    @Override
    public void setThrower(String thrower) {
        invoke("setThrower", thrower);
    }

    @Override
    public void setDefaultPickupDelay() {
        invoke("setDefaultPickupDelay");
    }

    @Override
    public void setNoPickupDelay() {
        invoke("setNoPickupDelay");
    }

    @Override
    public void setInfinitePickupDelay() {
        invoke("setInfinitePickupDelay");
    }

    @Override
    public void setPickupDelay(int ticks) {
        invoke("setPickupDelay", ticks);
    }

    @Override
    public boolean cannotPickup() {
        return (boolean) invoke("cannotPickup");
    }

    @Override
    public void setNoDespawn() {
        invoke("setnoDespawn");
    }

    // TODO: Not sure what to call this yet
    /*@Override
    public void func_174870_v() {

    }*/
}
