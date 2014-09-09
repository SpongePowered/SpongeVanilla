package org.granitemc.granite.item;

import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;

/**
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

public class ItemStack {

    Object vanillaItemInstance = null;
    String targetClass = "net.minecraft.item.ItemStack";

    public ItemStack(Object instance) {
        vanillaItemInstance = instance;
    }

    //need more constructors here that are capable of creating vanilla item instances

    public Object getItem() {
        return invoke("getItem");
    }

    public int getItemDamage() {
        return (int) invoke("getItemDamage");
    }

    public void setItemDamage(int damage) {
        invoke("setItemDamage", damage);
    }

    public String[] getItemLore() {
        return (String[]) invoke("getItemLore");
    }

    public int getMaxDamage() {
        return (int) invoke("getMaxDamage");
    }

    public String getDisplayName() {
        return (String) invoke("getDisplayName");
    }

    public ItemStack setDisplayName(String name) {
        return new ItemStack(invoke("setDisplayName", name));
    }

    public boolean hasDisplayName() {
        return (boolean) invoke("hasDisplayName");
    }

    public Object invoke(String targetMethod, Object... parameters) {
        try {
            return Mappings.getMethod(targetClass, targetMethod).invoke(vanillaItemInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    //ItemStack constructors:
    //ItemStack(Block|Item, int stacksize, int meta|damage) amj(atr|alq, int, int)
    //ItemStack(Block|Item, int stacksize) amj(atr|alq, int)
    //ItemStack(Block|Item) amj(atr|alq)

    //ItemStack onUseItemRightClick(World w, EntityPlayer e) amj a(aqu w, ahd e)
}
