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
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.impl.item.inventory.GraniteItemStack;
import mc.MCEntityItem;
import mc.MCItemStack;
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
        return obj.delayBeforeCanPickup;
    }

    @Override
    public void setPickupDelay(int pickupDelay) {
        obj.delayBeforeCanPickup = pickupDelay;
    }

    @Override
    public void setInfinitePickupDelay() {
        obj.delayBeforeCanPickup = 32767;
    }

    @Override
    public int getDespawnTime() {
        return obj.age == -32768 ? -1 : 6000 - obj.age;
    }

    @Override
    public void setDespawnTime(int despawnTime) {
        if (despawnTime == -1) {
            obj.age = -32768;
        } else {
            obj.age = (6000 - despawnTime);
        }
    }

    @Override
    public void setInfiniteDespawnTime() {
        obj.age = -32768;
    }

    @Override
    public Optional<User> getThrower() {
        // TODO: make get for offline player
        return Optional.<User>fromNullable(Granite.getInstance().getServer().get().getPlayer(obj.thrower).orNull());
    }

    @Override
    public void setThrower(User user) {
        obj.thrower = user.getName();
    }
}
