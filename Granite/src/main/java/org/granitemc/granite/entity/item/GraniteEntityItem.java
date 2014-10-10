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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.entity.item;

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

    public void searchForOtherItemsNearby() {
        invoke("EntityItem", "searchForOtherItemsNearby");
    }

    public void setAgeToCreativeDespawnTime() {
        invoke("EntityItem", "setAgeToCreativeDespawnTime");
    }

    public void dealFireDamage(int amount) {
        invoke("EntityItem", "dealFireDamage", amount);
    }

    //TODO: Enable after DamageSource has been created
    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("EntityItem", "attackEntityFrom", source, amount);
    }*/

    public String getName() {
        return (String) invoke("EntityItem", "getName");
    }

    public ItemStack getEntityItem() {
        return (ItemStack) invoke("EntityItem", "getEntityItem");
    }

    public void setEntityItemStack(ItemStack itemStack) {
        invoke("EntityItem", "setEntityItemStack", itemStack);
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }

    @Override
    public void setItemStack(ItemStack itemStack) {

    }

    @Override
    public String getOwnerName() {
        return (String) invoke("EntityItem", "getOwnerName");
    }

    @Override
    public void setOwnerName(String owner) {
        invoke("EntityItem", "setOwnerName", owner);
    }

    @Override
    public String getThrowerName() {
        return (String) invoke("EntityItem", "getThrowerName");
    }

    public void setThrower(String thrower) {
        invoke("EntityItem", "setThrower", thrower);
    }

    public void setDefaultPickupDelay() {
        invoke("EntityItem", "setDefaultPickupDelay");
    }

    public void setNoPickupDelay() {
        invoke("EntityItem", "setNoPickupDelay");
    }

    public void setInfinitePickupDelay() {
        invoke("EntityItem", "setInfinitePickupDelay");
    }

    @Override
    public void setPickupDelay(int ticks) {
        invoke("EntityItem", "setPickupDelay", ticks);
    }

    //func_174874_s()
    @Override
    public boolean canPickUp() {
        return (boolean) invoke("EntityItem", "canPickup");
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
