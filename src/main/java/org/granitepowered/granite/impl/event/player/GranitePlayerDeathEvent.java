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


package org.granitepowered.granite.impl.event.player;

import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.GraniteEntity;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.mc.MCDamageSource;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.player.PlayerDeathEvent;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.world.Location;

import java.util.Collection;

public class GranitePlayerDeathEvent extends GranitePlayerEvent implements PlayerDeathEvent {

    Message deathMessage;

    Optional<Cause> deathCause;

    public GranitePlayerDeathEvent(GranitePlayer player, MCDamageSource playerDeathCause, Message deathMessage) {
        super(player);
        GraniteEntity source = MinecraftUtils.wrap(playerDeathCause.getSourceOfDamage());
        if (source == null) {
            this.deathCause = Optional.of(new Cause(new Cause(null, playerDeathCause.getDamageType(), null), player, null));
        } else {
            this.deathCause = Optional.of(new Cause(new Cause(null, playerDeathCause.getDamageType(), null), source, null));
        }

        this.deathMessage = deathMessage;
    }

    @Override
    public Message getDeathMessage() {
        return deathMessage;
    }

    @Override
    public void setDeathMessage(Message message) {
        checkModify();
        deathMessage = message;
    }

    @Override
    public boolean keepsInventory() {
        throw new NotImplementedException("");
    }

    @Override
    public void setKeepsInventory(boolean keepInventory) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean keepsLevel() {
        throw new NotImplementedException("");
    }

    @Override
    public void setKeepsLevel(boolean keepLevel) {
        throw new NotImplementedException("");
    }

    @Override
    public double getNewExperience() {
        throw new NotImplementedException("");
    }

    @Override
    public void setNewExperience(double experience) {
        throw new NotImplementedException("");
    }

    @Override
    public int getNewLevel() {
        throw new NotImplementedException("");
    }

    @Override
    public void setNewLevel(int level) {
        throw new NotImplementedException("");
    }

    @Override
    public Optional<Cause> getCause() {
        return deathCause;
    }

    @Override
    public Collection<Item> getDroppedItems() {
        throw new NotImplementedException("");
    }

    @Override
    public Location getLocation() {
        throw new NotImplementedException("");
    }

    @Override
    public double getDroppedExperience() {
        throw new NotImplementedException("");
    }

    @Override
    public void setDroppedExperience(double experience) {
        throw new NotImplementedException("");
    }
}
