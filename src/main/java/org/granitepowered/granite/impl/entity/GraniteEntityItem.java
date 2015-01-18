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

package org.granitepowered.granite.impl.entity;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import org.granitepowered.granite.mc.MCEntityItem;
import org.granitepowered.granite.mc.MCItemStack;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.player.User;
import org.spongepowered.api.item.inventory.ItemStack;

public class GraniteEntityItem extends GraniteEntity<MCEntityItem> implements Item {

    public GraniteEntityItem(MCEntityItem obj) {
        super(obj);
    }

    @Override
    public ItemStack getItemStack() {
        return new GraniteItemStack((MCItemStack) obj.getEntityItem());
    }

    @Override
    public int getPickupDelay() {
        throw new NotImplementedException("");
    }

    @Override
    public void setPickupDelay(int i) {
        throw new NotImplementedException("");
    }

    @Override
    public void setInfinitePickupDelay() {
        throw new NotImplementedException("");
    }

    @Override
    public int getDespawnTime() {
        throw new NotImplementedException("");
    }

    @Override
    public void setDespawnTime(int i) {
        throw new NotImplementedException("");
    }

    @Override
    public void setInfiniteDespawnTime() {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<User> getThrower() {
        throw new NotImplementedException("");
    }

    @Override
    public void setThrower(User user) {
        throw new NotImplementedException("");
    }
}
