/*
 * License (MIT)
 *
 * Copyright (c) 2014. Granite Team
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
 * PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.granitemc.granite.entity;

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.entity.player.GraniteEntityPlayer;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.util.UUID;

public class GraniteEntity extends Composite implements Entity {

    public GraniteEntity(Object parent) {
        super(parent);
    }

    @Override
    public int getEntityId() {
        return (Integer) invoke("getEntityID");
    }

    @Override
    public void setDead() {
        invoke("setDead");
    }

    @Override
    public void setSize(float width, float height) {
        invoke("setSite", width, height);
    }

    @Override
    public int getMaxInPortalTime() {
        return (Integer) invoke("getMaxTimeInPortal");
    }

    @Override
    public void setOnFireFromLava() {
        invoke("setOnFireFromLava");
    }

    @Override
    public void setFire(int seconds) {
        invoke("setFire", seconds);
    }

    @Override
    public void extinguish() {
        invoke("extinguish");
    }

    @Override
    public void kill() {
        invoke("kill");
    }

    @Override
    public String getSwimSound() {
        return (String) invoke("getSwimSound");
    }

    @Override
    public void playSound(String name, float volume, float pitch) {
        invoke("playSound", name, volume, pitch);
    }

    @Override
    public boolean isSilent() {
        return (boolean) invoke("isSilent");
    }

    @Override
    public void dealFireDamage(int amount) {
        invoke("dealFireDamage", amount);
    }

    @Override
    public boolean isImmuneToFire() {
        return (boolean) invoke("isImmuneToFire");
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        invoke("fall", distance, damageMultiplier);
    }

    @Override
    public boolean isWet() {
        return (boolean) invoke("isWet");
    }

    @Override
    public boolean isInWater() {
        return (boolean) invoke("isInWater");
    }

    @Override
    public void createRunningParticles() {
        invoke("createRunningParticles");
    }

    @Override
    public String getSplashSound() {
        return (String) invoke("getSplashSound");
    }

    @Override
    public boolean isInLava() {
        return (boolean) invoke("isInLava");
    }

    @Override
    public void moveFlying(float strafe, float forward, float friction) {
        invoke("moveFlying", strafe, forward, friction);
    }

    @Override
    public void setWorld(World world) {
        invoke("setWorld", ((GraniteWorld) world).parent);
    }

    @Override
    public float getDistanceToEntity(Entity entity) {
        if (getLocation().getWorld().equals(entity.getWorld())) {
            return (float) invoke("getDistanceToEntity", entity);
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public double getDistanceSq(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("getDistanceSq", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public double getDistanceSqToCenter(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("getDistanceSq", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public double getDistance(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("getDistance", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public double getDistanceSqToEntity(Entity entity) {
        if (getLocation().getWorld().equals(entity.getWorld())) {
            return (double) invoke("getDistanceSq", entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        invoke("addVelocity", x, y, z);
    }

    @Override
    public void setBeenAttacked() {
        invoke("setBeenAttacked");
    }

    /*@Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return (boolean) invoke("attackEntityFrom", source, amount);
    }*/

    @Override
    public Vector getLook(float vec) {
        return MinecraftUtils.fromMinecraftVector(invoke("getLook", vec));
    }

    @Override
    public boolean canBeCollidedWith() {
        return (boolean) invoke("canBeCollidedWith");
    }

    @Override
    public boolean canBePushed() {
        return (boolean) invoke("canBePushed");
    }

    @Override
    public String getEntityString() {
        return (String) invoke("getEntityString");
    }

    @Override
    public EntityItem entityDropItem(ItemStack itemStack, float offsetY) {
        return (EntityItem) MinecraftUtils.wrap(invoke("entityDropItem", itemStack, offsetY));
    }

    @Override
    public boolean isEntityAlive() {
        return (boolean) invoke("isEntityAlive");
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return (boolean) invoke("isEntityInsideOpaqueBlock");
    }

    @Override
    public boolean interactFirst(Player player) {
        return (boolean) invoke("interactFirst", ((GraniteEntityPlayer) player).parent);
    }

    @Override
    public double getMountedYOffset() {
        return (double) invoke("getMountedYOffset");
    }

    @Override
    public void mountEntity(Entity entity) {
        invoke("mountEntity", ((GraniteEntity) entity).parent);
    }

    @Override
    public Vector getLookVec() {
        return MinecraftUtils.fromMinecraftVector(invoke("getLookVec"));
    }

    @Override
    public void setInPortal() {
        invoke("setInPortal");
    }

    @Override
    public int getPortalCooldown() {
        return (int) invoke("getPortalCooldown");
    }

    @Override
    public ItemStack[] getInventory() {
        return (ItemStack[]) MinecraftUtils.wrap(invoke("getInventory"));
    }

    @Override
    public void setCurrentItemOrArmor(int slot, ItemStack itemStack) {
        invoke("setCurrentItemOrArmor", slot, ((GraniteItemStack) itemStack).parent);
    }

    @Override
    public boolean isBurning() {
        return (boolean) invoke("isBurning");
    }

    @Override
    public boolean isRiding() {
        return (boolean) invoke("isRiding");
    }

    @Override
    public boolean isSneaking() {
        return (boolean) invoke("isSneaking");
    }

    @Override
    public void setSneaking(boolean sneaking) {
        invoke("setSneaking", sneaking);
    }

    @Override
    public boolean isSprinting() {
        return (boolean) invoke("isSprinting");
    }

    @Override
    public void setSprinting(boolean sprinting) {
        invoke("setSprinting", sprinting);
    }

    @Override
    public boolean isInvisible() {
        return (boolean) invoke("isInvisible");
    }

    @Override
    public void setInvisible(boolean invisible) {
        invoke("setInvisible", invisible);
    }

    @Override
    public boolean isEating() {
        return (boolean) invoke("isEating");
    }

    @Override
    public boolean getFlag(int flag) {
        return (boolean) invoke("getFlag", flag);
    }

    @Override
    public void setFlag(int flag, boolean set) {
        invoke("setFlag", flag, set);
    }

    @Override
    public int getAir() {
        return (Integer) invoke("getAir");
    }

    @Override
    public void setAir(int air) {
        invoke("setAir", air);
    }

    @Override
    public void setInWeb() {
        invoke("setInWeb");
    }

    @Override
    public String getName() {
        return (String) invoke("getName");
    }

    @Override
    public Entity[] getParts() {
        Object[] nativeParts = (Object[]) invoke("getParts");
        Entity[] parts = new Entity[nativeParts.length];

        for (int i = 0; i < nativeParts.length; i++) {
            parts[i] = (Entity) MinecraftUtils.wrap(nativeParts[i]);
        }
        return parts;
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return (boolean) invoke("isEntityEqual", ((GraniteEntity) entity).parent);
    }

    @Override
    public boolean canAttackWithItem() {
        return (boolean) invoke("canAttackWithItem");
    }

    @Override
    public boolean hitByEntity(Entity entity) {
        return (boolean) MinecraftUtils.wrap(invoke("hitByEntity", entity));
    }

    /*@Override
    public boolean isEntityInvulnerable(DamageSource damageSource) {
        return (boolean) MinecraftUtils.wrap(invoke("getExplosionResistance", damageSource));
    }*/

    @Override
    public void travelToDimension(int dimensionId) {
        invoke("travelToDimension", dimensionId);
    }

    /*@Override
    public float getExplosionResistance(Explosion explosion, World world, Location location, IBlockState blockState) {
        return (float) MinecraftUtil.wrap(invoke("getExplosionResistance", explosion, world, location, blockState));
    }*/

    @Override
    public int getMaxFallHeight() {
        return (Integer) invoke("getMaxFallHeight");
    }

    @Override
    public boolean doesEntityNotTriggerPressurePlate() {
        return (boolean) invoke("doesEntityNotTriggerPressurePlate");
    }

    @Override
    public UUID getUniqueID() {
        return (UUID) invoke("getUniqueID");
    }

    /*@Override
    public IChatComponent getDisplayName() {
        return (IChatComponent) MinecraftUtil.wrap(invoke("getDisplayName"));
    }*/

    @Override
    public void setCustomNameTag(String nameTag) {
        invoke("setCustomNameTag", nameTag);
    }

    @Override
    public String getCustomNameTag() {
        return (String) invoke("getCustomNameTag");
    }

    @Override
    public boolean hasCustomName() {
        return (boolean) invoke("hasCustomName");
    }

    @Override
    public void setAlwaysRenderNameTag(boolean renderNameTag) {
        invoke("setAlwaysRenderNameTag", renderNameTag);
    }

    @Override
    public boolean getAlwaysRenderNameTag() {
        return (boolean) invoke("getAlwaysRenderNameTag");
    }

    @Override
    public float getEyeHeight() {
        return (float) invoke("getEyeHeight");
    }

    @Override
    public boolean isOutsideBorder() {
        return (boolean) invoke("isOutSideBorder");
    }

    /*@Override
    public void addChatMessage(IChatComponent message) {
        MinecraftUtils.wrap(invoke("addChatMessage", message));
    }*/

    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return (boolean) invoke("canCommandSenderUseCommand", permissionLevel, command);
    }

    @Override
    public Vector getPositionVector() {
        return MinecraftUtils.fromMinecraftVector(invoke("getPositionVector"));
    }

    @Override
    public World getWorld() {
        return (World) MinecraftUtils.wrap(invoke("getEntityWorld"));
    }

    @Override
    public Entity getCommandSenderEntity() {
        return (Entity) MinecraftUtils.wrap(invoke("getCommandSenderEntity"));
    }

    @Override
    public boolean sendCommandFeedback() {
        return (boolean) invoke("sendCommandFeedback");
    }


    /**
     * Granite Methods
     */


    @Override
    public Location getLocation() {
        return new Location((double) fieldGet(this, "posX"), (double) fieldGet(this, "posY"), (double) fieldGet(this, "posZ"), (float) fieldGet(this, "rotationYaw"), (float) fieldGet(this, "rotationPitch"));
    }

    @Override
    public void setLocation(Location location) {
        invoke("setPositionAndRotation", location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

}
