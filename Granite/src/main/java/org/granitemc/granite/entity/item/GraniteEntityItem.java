package org.granitemc.granite.entity.item;

/*****************************************************************************************
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
 ****************************************************************************************/

import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.entity.GraniteEntity;

public class GraniteEntityItem extends GraniteEntity implements EntityItem {
    public GraniteEntityItem(Object parent) {
        super(parent);
    }

    public GraniteEntityItem(Object parent, boolean bool) {
        super(parent);
    }

    @Override
    public void searchForOtherItemsNearby() {
        invoke("n.m.entity.item.EntityItem", "searchForOtherItemsNearby");
    }

    @Override
    public void setAgeToCreativeDespawnTime() {
        invoke("n.m.entity.item.EntityItem", "setAgeToCreativeDespawnTime");
    }

    @Override
    public void dealFireDamage(int amount) {
        invoke("n.m.entity.item.EntityItem", "dealFireDamage", amount);
    }

    //TODO: Enable after DamageSource has been created
    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("n.m.entity.item.EntityItem", "attackEntityFrom", source, amount);
    }*/

    @Override
    public String getName() {
        return (String) invoke("n.m.entity.item.EntityItem", "getName");
    }

    @Override
    public ItemStack getEntityItem() {
        return (ItemStack) invoke("n.m.entity.item.EntityItem", "getEntityItem");
    }

    @Override
    public void setEntityItemStack(ItemStack itemStack) {
        invoke("n.m.entity.item.EntityItem", "setEntityItemStack", itemStack);
    }

    @Override
    public String getOwner() {
        return (String) invoke("n.m.entity.item.EntityItem", "getOwner");
    }

    @Override
    public void setOwner(String owner) {
        invoke("n.m.entity.item.EntityItem", "setOwner", owner);
    }

    @Override
    public String getThrower() {
        return (String) invoke("n.m.entity.item.EntityItem", "getThrower");
    }

    @Override
    public void setThrower(String thrower) {
        invoke("n.m.entity.item.EntityItem", "setThrower", thrower);
    }

    //func_174872_o()
    @Override
    public int getAge() {
        return (Integer) invoke("n.m.entity.item.EntityItem", "getAge");
    }

    @Override
    public void setDefaultPickupDelay() {
        invoke("n.m.entity.item.EntityItem", "setDefaultPickupDelay");
    }

    @Override
    public void setNoPickupDelay() {
        invoke("n.m.entity.item.EntityItem", "setNoPickupDelay");
    }

    @Override
    public void setInfinitePickupDelay() {
        invoke("n.m.entity.item.EntityItem", "setInfinitePickupDelay");
    }

    @Override
    public void setPickupDelay(int ticks) {
        invoke("n.m.entity.item.EntityItem", "setPickupDelay", ticks);
    }

    //func_174874_s()
    @Override
    public boolean canPickUp() {
        return (boolean) invoke("n.m.entity.item.EntityItem", "canPickup");
    }

    //TODO: Set not despawnable?
    /*@Override
    public void func_174873_u() {
    }*/

    //TODO: Set not despawnable and not able to pickup?
    /*@Override
    public void func_174870_v(){
    }*/
}
