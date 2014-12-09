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

package org.granitepowered.granite.impl.entity.player;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3f;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.entity.living.GraniteLiving;
import org.granitepowered.granite.impl.item.GraniteItemStack;
import org.granitepowered.granite.impl.text.message.GraniteMessage;
import org.granitepowered.granite.impl.text.message.GraniteMessageBuilder;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.effect.Particle;
import org.spongepowered.api.effect.Sound;
import org.spongepowered.api.entity.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.title.Title;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Locale;

public class GranitePlayer extends GraniteLiving implements Player {
    private Message<?> displayName;

    public GranitePlayer(Object parent) {
        super(parent);
    }

    @Override
    public Message<?> getDisplayName() {
        return displayName == null ? new GraniteMessageBuilder.GraniteTextMessageBuilder().content(getName()).build() : displayName;
    }

    @Override
    public boolean getAllowFlight() {
        return (boolean) fieldGet(getPlayerCapabilities(), "allowFlying");
    }

    @Override
    public void setAllowFlight(boolean allowFlight) {
        fieldSet(getPlayerCapabilities(), "allowFlying", allowFlight);
    }

    @Override
    public Locale getLocale() {
        // TODO: Find out if this is stored server side
        // See EntityPlayerMP.translator
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(ChatType type, String... message) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    @Override
    public void sendTitle(Title title) {
        // TODO: Title API
        throw new NotImplementedException("");
    }

    @Override
    public void resetTitle() {
        // TODO: Title API
        throw new NotImplementedException("");
    }

    @Override
    public void clearTitle() {
        // TODO: Title API
        throw new NotImplementedException("");
    }

    @Override
    public Optional<ItemStack> getHelmet() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrapComposite(((Object[]) fieldGet("equipment"))[4]));
    }

    @Override
    public void setHelmet(@Nullable ItemStack helmet) {
        ((Object[]) fieldGet("equipment"))[4] = helmet == null ? null : ((GraniteItemStack) helmet).parent;
    }

    @Override
    public Optional<ItemStack> getChestplate() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrapComposite(((Object[]) fieldGet("equipment"))[3]));
    }

    @Override
    public void setChestplate(@Nullable ItemStack chestplate) {
        ((Object[]) fieldGet("equipment"))[3] = chestplate == null ? null : ((GraniteItemStack) chestplate).parent;
    }

    @Override
    public Optional<ItemStack> getLeggings() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrapComposite(((Object[]) fieldGet("equipment"))[2]));
    }

    @Override
    public void setLeggings(@Nullable ItemStack leggings) {
        ((Object[]) fieldGet("equipment"))[2] = leggings == null ? null : ((GraniteItemStack) leggings).parent;
    }

    @Override
    public Optional<ItemStack> getBoots() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrapComposite(((Object[]) fieldGet("equipment"))[1]));

    }

    @Override
    public void setBoots(@Nullable ItemStack boots) {
        ((Object[]) fieldGet("equipment"))[1] = boots == null ? null : ((GraniteItemStack) boots).parent;
    }

    @Override
    public Optional<ItemStack> getItemInHand() {
        return Optional.fromNullable((ItemStack) MinecraftUtils.wrapComposite(((Object[]) fieldGet("equipment"))[0]));
    }

    @Override
    public void setItemInHand(@Nullable ItemStack itemInHand) {
        ((Object[]) fieldGet("equipment"))[0] = itemInHand == null ? null : ((GraniteItemStack) itemInHand).parent;
    }

    @Override
    public String getName() {
        return (String) fieldGet(getGameProfile(), "name");
    }

    @Override
    public boolean hasJoinedBefore() {
        return true;
    }

    @Override
    public Date getFirstPlayed() {
        // TODO: Not sure if possible
        throw new NotImplementedException("");
    }

    @Override
    public Date getLastPlayed() {
        // TODO: Not sure if possible
        throw new NotImplementedException("");
    }

    @Override
    public boolean isBanned() {
        // TODO: Check SCM.bannedPlayers
        throw new NotImplementedException("");
    }

    @Override
    public boolean isWhitelisted() {
        // TODO: Check SCM.whiteListedPlayers
        throw new NotImplementedException("");
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public Optional<Player> getPlayer() {
        return Optional.of((Player) this);
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, ItemType itemType) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void spawnParticles(Particle particle, int particleCount, Vector3d position, Vector3d offset, double speed, int radius, ItemType itemType) {
        // TODO: Particles API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    @Override
    public void playSound(Sound sound, Vector3d position, double volume, double pitch, double minVolume) {
        // TODO: Sound API
        throw new NotImplementedException("");
    }

    @Override
    public float getHunger() {
        return (float) fieldGet(getFoodStats(), "foodLevel");
    }

    @Override
    public void setHunger(float hunger) {
        fieldSet(getFoodStats(), "foodLevel", hunger);
    }

    @Override
    public float getSaturation() {
        return (float) fieldGet(getFoodStats(), "foodSaturationLevel");
    }

    @Override
    public void setSaturation(float saturation) {
        fieldSet(getFoodStats(), "foodSaturationLevel", saturation);
    }

    @Override
    public boolean isViewingInventory() {
        return fieldGet("openContainer") != null;
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) {
        // TODO: Projectile API
        throw new NotImplementedException("");
    }

    @Override
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass, Vector3f velocity) {
        // TODO: Projectile API
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(String... messages) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(Iterable<Message<?>> messages) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    @Override
    public boolean isAiEnabled() {
        return false;
    }

    @Override
    public void setAiEnabled(boolean aiEnabled) {
        throw new UnsupportedOperationException("Player doesn't have AI");
    }

    @Override
    public void sendMessage(Message<?>... messages) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(ChatType type, Message<?>... message) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    @Override
    public void sendMessage(ChatType type, Iterable<Message<?>> messages) {
        // TODO: Message API
        throw new NotImplementedException("");
    }

    public Object getGameProfile() {
        return fieldGet("gameProfile");
    }

    public Object getPlayerCapabilities() {
        return fieldGet("capabilities");
    }

    public Object getFoodStats() {
        return fieldGet("foodStats");
    }
}
