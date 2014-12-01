package org.granitemc.granite.entity.item;

/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
