/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.impl.item;

import org.granitepowered.granite.composite.Composite;
import mc.MCEnchantment;
import mc.MCItemStack;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEnchantment extends Composite<MCEnchantment> implements Enchantment {

    public GraniteEnchantment(Object obj) {
        super(obj);
    }

    @Override
    public String getId() {
        return "minecraft:" + obj.name;
    }

    @Override
    public int getWeight() {
        return obj.weight;
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
        return canBeAppliedToStack(itemStack);
    }

    @Override
    public boolean isCompatibleWith(Enchantment enchantment) {
        return obj.canApplyTogether((MCEnchantment) MinecraftUtils.unwrap(enchantment));
    }
}
