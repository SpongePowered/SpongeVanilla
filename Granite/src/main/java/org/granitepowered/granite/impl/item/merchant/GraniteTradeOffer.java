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

package org.granitepowered.granite.impl.item.merchant;

import com.google.common.base.Optional;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.mc.MCMerchantRecipe;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.merchant.TradeOffer;

public class GraniteTradeOffer extends Composite<MCMerchantRecipe> implements TradeOffer {

    public GraniteTradeOffer(Object obj) {
        super(obj);
    }

    @Override
    public ItemStack getFirstBuyingItem() {
        return MinecraftUtils.wrap(obj.fieldGet$firstItemToBuy());
    }

    @Override
    public boolean hasSecondItem() {
        return obj.fieldGet$secondItemToBuy() != null;
    }

    @Override
    public Optional<ItemStack> getSecondBuyingItem() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrap(obj.fieldGet$secondItemToBuy()));
    }

    @Override
    public ItemStack getSellingItem() {
        return MinecraftUtils.wrap(obj.fieldGet$itemToSell());
    }

    @Override
    public int getUses() {
        return obj.fieldGet$uses();
    }

    @Override
    public int getMaxUses() {
        return obj.fieldGet$maxUses();
    }

    @Override
    public boolean hasExpired() {
        return getUses() >= getMaxUses();
    }

    @Override
    public boolean doesGrantExperience() {
        return obj.fieldGet$grantExperience();
    }
}
