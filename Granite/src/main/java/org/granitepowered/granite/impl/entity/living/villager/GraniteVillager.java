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

package org.granitepowered.granite.impl.entity.living.villager;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.living.GraniteEntityAgeable;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.item.merchant.GraniteTradeOffer;
import org.granitepowered.granite.impl.meta.GraniteMeta;
import org.granitepowered.granite.mc.MCEntityVillager;
import org.granitepowered.granite.mc.MCInterface;
import org.granitepowered.granite.mc.MCMerchantRecipe;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.living.Human;
import org.spongepowered.api.entity.living.villager.Career;
import org.spongepowered.api.entity.living.villager.Villager;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.types.CarriedInventory;
import org.spongepowered.api.item.merchant.TradeOffer;

import java.util.ArrayList;
import java.util.List;

public class GraniteVillager extends GraniteEntityAgeable<MCEntityVillager> implements Villager {

    public GraniteVillager(MCEntityVillager obj) {
        super(obj);
    }

    @Override
    public boolean isPlaying() {
        return obj.fieldGet$isPlaying();
    }

    @Override
    public void setPlaying(boolean playing) {
        obj.fieldSet$isPlaying(playing);
    }

    @Override
    public boolean isTrading() {
        return obj.fieldGet$buyingPlayer() != null;
    }

    // TODO: Make Tidier and smaller
    @Override
    public Career getCareer() {
        /*int professionId = Math.max((int) obj.fieldGet$dataWatcher().getWatchedObject(16).fieldGet$watchedObject() % 5, 0);
        List<Profession> professions = ImmutableList.copyOf(Granite.getInstance().getRegistry().professions.values());
        Career villagerCareer = null;
        for (Profession profession : professions) {
            if (((GraniteMeta) profession).getType() == professionId) {
                List<Career> careers = ImmutableList.copyOf(Granite.instance.getGameRegistry().professionCareers.get(profession));
                for (Career career : careers) {
                    if (((GraniteMeta) career).getType() == obj.fieldGet$career()) {
                        villagerCareer = career;
                    }
                }
            }
        }
        return villagerCareer;*/
        throw new NotImplementedException("");
    }

    @Override
    public void setCareer(Career career) {
        obj.fieldGet$dataWatcher().updateObject(16, ((GraniteMeta) career.getProfession()).getType());
        obj.fieldSet$career(((GraniteMeta) career).getType());
    }

    @Override
    public Optional<Human> getCustomer() {
        return Optional.fromNullable((Human) MinecraftUtils.wrap(obj.fieldGet$buyingPlayer()));
    }

    @Override
    public void setCustomer(Human human) {
        obj.interact(MinecraftUtils.unwrap((GranitePlayer) human));
    }

    @Override
    public List<TradeOffer> getOffers() {
        ArrayList recipes = (ArrayList) obj.fieldGet$buyingList();
        List<TradeOffer> tradeOffers = new ArrayList<>();
        for (Object recipe : recipes) {
            tradeOffers.add((GraniteTradeOffer) MinecraftUtils.wrap((MCInterface) recipe));
        }
        return tradeOffers;
    }

    @Override
    public void setOffers(List<TradeOffer> list) {
        ArrayList recipes = (ArrayList) obj.fieldGet$buyingList();
        recipes.clear();
        for (TradeOffer recipe : list) {
            recipes.add(MinecraftUtils.unwrap(recipe));
        }
    }

    @Override
    public void addOffer(TradeOffer tradeOffer) {
        ArrayList<MCMerchantRecipe> recipes = (ArrayList<MCMerchantRecipe>) obj.fieldGet$buyingList();
        recipes.add((MCMerchantRecipe) MinecraftUtils.unwrap(tradeOffer));
    }

    @Override
    public CarriedInventory<? extends Carrier> getInventory() {
        throw new NotImplementedException("");
    }
}
