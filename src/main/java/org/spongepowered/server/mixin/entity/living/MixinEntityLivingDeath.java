/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.server.mixin.entity.living;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.world.World;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.SpongeEventFactory;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.interfaces.entity.IMixinEntity;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.StaticMixinHelper;

import java.util.Optional;

//@Mixin({EntityLivingBase.class, EntityPlayerMP.class})
public abstract class MixinEntityLivingDeath extends EntityLivingBase {

    public MixinEntityLivingDeath(World worldIn) {
        super(worldIn);
    }

    //@Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void callDestructEntityEventDeath(DamageSource source, CallbackInfo ci) {
        MessageChannel originalChannel = this instanceof Player ? ((Player) this).getMessageChannel() : MessageChannel.TO_NONE;
        Optional<Text> deathMessage = Optional.ofNullable(SpongeTexts.toText(getCombatTracker().getDeathMessage()));

        Optional<User> sourceCreator = Optional.empty();

        Cause cause;
        if (source instanceof EntityDamageSource) {
            EntityDamageSource damageSource = (EntityDamageSource) source;
            IMixinEntity spongeEntity = (IMixinEntity) damageSource.getSourceOfDamage();
            sourceCreator = spongeEntity.getTrackedPlayer(NbtDataUtil.SPONGE_ENTITY_CREATOR);
        }

        if (sourceCreator.isPresent()) {
            cause = Cause.of(NamedCause.source(source), NamedCause.of("Victim", this), NamedCause.owner(sourceCreator.get()));
        } else {
            cause = Cause.of(NamedCause.source(source), NamedCause.of("Victim", this));
        }

        DestructEntityEvent.Death event = SpongeEventFactory.createDestructEntityEventDeath(cause, originalChannel, Optional.of(originalChannel),
                deathMessage, deathMessage, (Living) this);
        if (!SpongeImpl.postEvent(event)) {
            event.getMessage().ifPresent(text -> event.getChannel().ifPresent(channel -> channel.send(text)));

            // Store cause for drop event which is called after this event
            if (sourceCreator.isPresent()) {
                StaticMixinHelper.dropCause = Cause.of(NamedCause.source(this), NamedCause.of("Attacker", source), NamedCause.owner(sourceCreator.get()));
            } else {
                StaticMixinHelper.dropCause = Cause.of(NamedCause.source(this), NamedCause.of("Attacker", source));
            }
        } else {
            ci.cancel(); // Cancel entity death
        }
    }

}
