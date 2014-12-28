/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.mc.MCDamageSource;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.player.PlayerDeathEvent;
import org.spongepowered.api.text.message.Message;

public class GranitePlayerDeathEvent extends GranitePlayerEvent implements PlayerDeathEvent {

    Message deathMessage;

    Optional<Cause> deathCause;

    public GranitePlayerDeathEvent(GranitePlayer player, MCDamageSource playerDeathCause) {
        super(player);
        this.deathCause = Optional.of(new Cause(null, playerDeathCause, null));
    }

    @Override
    public Message getDeathMessage() {
        return deathMessage;
    }

    @Override
    public void setDeathMessage(Message message) {
        deathMessage = message;
    }

    @Override
    public Optional<Cause> getCause() {
        return deathCause;
    }

}
