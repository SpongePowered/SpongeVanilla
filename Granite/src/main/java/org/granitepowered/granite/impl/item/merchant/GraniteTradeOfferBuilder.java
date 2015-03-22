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

import com.google.common.base.Preconditions;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.mc.MCMerchantRecipe;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.merchant.TradeOffer;
import org.spongepowered.api.item.merchant.TradeOfferBuilder;

public class GraniteTradeOfferBuilder implements TradeOfferBuilder {

    private ItemStack firstBuyingItem;
    private ItemStack secondBuyingItem;
    private ItemStack sellingItem;
    private int useCount;
    private int maxUses;
    private boolean grantExp;

    public GraniteTradeOfferBuilder() {
        reset();
    }

    @Override
    public TradeOfferBuilder firstBuyingItem(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "Buying item cannot be null");
        this.firstBuyingItem = itemStack;
        return this;
    }

    @Override
    public TradeOfferBuilder secondBuyingItem(ItemStack itemStack) {
        this.secondBuyingItem = itemStack;
        return this;
    }

    @Override
    public TradeOfferBuilder sellingItem(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "Selling item cannot be null");
        this.sellingItem = itemStack;
        return this;
    }

    @Override
    public TradeOfferBuilder uses(int uses) {
        Preconditions.checkArgument(uses >= 0, "Usage count cannot be negative");
        this.useCount = uses;
        return this;
    }

    @Override
    public TradeOfferBuilder maxUses(int maxUses) {
        Preconditions.checkArgument(maxUses > 0, "Max usage count must be greater than 0");
        this.maxUses = maxUses;
        return this;
    }

    @Override
    public TradeOfferBuilder setCanGrantExperience(boolean grantExp) {
        this.grantExp = grantExp;
        return this;
    }

    @Override
    public TradeOffer build() throws IllegalStateException {
        Preconditions.checkState(this.firstBuyingItem != null, "Trading item has not been set");
        Preconditions.checkState(this.sellingItem != null, "Selling item has not been set");
        Preconditions.checkState(this.useCount <= this.maxUses, "Usage count cannot be greater than the max usage count (%s)", this.maxUses);
        MCMerchantRecipe
                recipe =
                Instantiator.get().newMerchantRecipe(MinecraftUtils.unwrap((GraniteItemStack) firstBuyingItem),
                        MinecraftUtils.unwrap((GraniteItemStack) secondBuyingItem),
                        MinecraftUtils.unwrap((GraniteItemStack) sellingItem), useCount, maxUses);
        recipe.grantExperience = grantExp;
        return new GraniteTradeOffer(recipe);
    }

    @Override
    public TradeOfferBuilder from(TradeOffer tradeOffer) {
        this.firstBuyingItem = tradeOffer.getFirstBuyingItem();
        this.secondBuyingItem = tradeOffer.getSecondBuyingItem().orNull();
        this.sellingItem = tradeOffer.getSellingItem();
        this.useCount = tradeOffer.getUses();
        this.maxUses = tradeOffer.getMaxUses();
        this.grantExp = tradeOffer.doesGrantExperience();
        return this;
    }

    @Override
    public TradeOfferBuilder reset() {
        this.firstBuyingItem = null;
        this.secondBuyingItem = null;
        this.sellingItem = null;
        this.useCount = 0;
        this.maxUses = 7;
        this.grantExp = true;
        return this;
    }
}
