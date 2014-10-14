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
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.reflect.composite.Composite;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.util.UUID;

public class GraniteEntity extends Composite implements Entity {

    public GraniteEntity(Object parent) {
        super(parent);
    }

    public GraniteEntity(Object parent, boolean bool) {
        super(parent);
    }

    public int getEntityId() {
        return (Integer) invoke("getEntityID");
    }

    public void setDead() {
        invoke("setDead");
    }

    public void setSize(float width, float height) {
        invoke("setSize", width, height);
    }

    public void setFire(int seconds) {
        invoke("setFire", seconds);
    }

    public void extinguish() {
        invoke("extinguish");
    }

    public void kill() {
        invoke("kill");
    }

    public void playSound(String soundName, float volume, float pitch) {
        invoke("playSound", soundName, volume, pitch);
    }

    public boolean isImmuneToFire() {
        return (boolean) invoke("isImmuneToFire");
    }

    public boolean isWet() {
        return (boolean) invoke("isWet");
    }

    public boolean isInWater() {
        return (boolean) invoke("isInWater");
    }

    public void resetHeight() {
        invoke("resetHeight");
    }

    // TODO: bof is Material
    /*public boolean isInsideOfMaterial(Material var1) {
        return (boolean) invoke("isInsideOfMaterial");
    }*/

    public World getWorld() {
        return (World) MinecraftUtils.wrap(invoke("getWorld"));
    }

    public void setWorld(World world) {
        invoke("setWorld", ((GraniteWorld) world).parent);
    }

    public float getDistanceToEntity(Entity entity) {
        return (float) invoke("getDistanceToEntity", entity);
    }

    public double getDistanceSqToEntity(Entity entity) {
        return (double) invoke("getDistanceSqToEntity", entity);
    }

    public void addVelocity(double x, double y, double z) {
        invoke("addVelocity", x, y, z);
    }

    public boolean canBeCollidedWith() {
        return (boolean) invoke("canBeCollidedWith");
    }

    public boolean canBePushed() {
        return (boolean) invoke("canBePushed");
    }

    public EntityItem entityDropItem(ItemStack itemStack, float yPos) {
        return (EntityItem) MinecraftUtils.wrap(invoke("entityDropItem", itemStack, yPos));
    }

    public boolean isEntityAlive() {
        return (boolean) invoke("isEntityAlive");
    }

    public boolean isEntityInsideOpaqueBlock() {
        return (boolean) invoke("isEntityInsideOpaqueBlock");
    }

    public void mountEntity(Entity entity) {
        invoke("mountEntity", entity);
    }

    public boolean isEating() {
        return (boolean) invoke("isEating");
    }

    public void setEating(boolean eating) {
        invoke("setEating", eating);
    }

    public ItemStack[] getInventory() {
        return (ItemStack[]) MinecraftUtils.wrap(invoke("getInventory"));
    }

    public void setCurrentItemOrArmor(int inventoryIndex, ItemStack itemStack) {
        invoke("setCurrentItemOrArmor", inventoryIndex, itemStack);
    }

    public boolean isBurning() {
        return (boolean) invoke("isBurning");
    }

    public boolean isRiding() {
        return (boolean) invoke("isRiding");
    }

    public boolean isSneaking() {
        return (boolean) invoke("isSneaking");
    }

    public void setSneaking(boolean sneaking) {
        invoke("setSneaking", sneaking);
    }

    public boolean isSprinting() {
        return (boolean) invoke("isSprinting");
    }

    public void setSprinting(boolean sprinting) {
        invoke("setSprinting", sprinting);
    }

    public boolean isInvisible() {
        return (boolean) invoke("isInvisible");
    }

    public void setInvisible(boolean invisible) {
        invoke("isInvisible", invisible);
    }

    public boolean getFlag(int flag) {
        return (boolean) invoke("getFlag", flag);
    }

    public void setFlag(int flag, boolean bool) {
        invoke("setFlag", flag, bool);
    }

    public int getAir() {
        return (Integer) invoke("getAir");
    }

    public void setAir(int amount) {
        invoke("setAir", amount);
    }

    public void setInWeb() {
        invoke("setInWeb");
    }

    public String getCommandSenderName() {
        return (String) invoke("getCommandSenderName");
    }

    public Entity[] getParts() {
        Object[] nativeParts = (Object[]) invoke("getParts");
        Entity[] parts = new Entity[nativeParts.length];

        for (int i = 0; i < nativeParts.length; i++) {
            parts[i] = (Entity) MinecraftUtils.wrap(nativeParts[i]);
        }

        return parts;
    }

    public boolean isEntityEqual(Entity entity) {
        return (boolean) invoke("isEntityEqual", entity);
    }

    public boolean canAttackWithItem() {
        return (boolean) invoke("canAttackWithItem");
    }

    public int getTeleportDirection() {
        return (Integer) invoke("getTeleportDirection");
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return (boolean) invoke("doesEntityNotTriggerPressurePlate");
    }

    public UUID getUniqueID() {
        return (UUID) invoke("getUniqueID");
    }

    public boolean isPushedByWater() {
        return (boolean) invoke("isPushedByWater");
    }

    // TODO: wait until IChatComponent has been wrapped
    /*public ho getDisplayName() {
        return (ho) invoke("getDisplayName");
    }*/

    public void setCustomNameTag(String name) {
        invoke("setCustomNameTag", name);
    }

    public String getCustomNameTag() {
        return (String) invoke("getCustomNameTag");
    }

    public boolean hasCustomName() {
        return (boolean) invoke("hasCustomName");
    }

    public void setAlwaysRenderNameTag(boolean bool) {
        invoke("setAlwaysRenderNameTag", bool);
    }

    public boolean getAlwaysRenderNameTag() {
        return (boolean) invoke("getAlwaysRenderNameTag");
    }

    public void setPosition(double xPos, double yPos, double zPos) {
        invoke("setPosition", xPos, yPos, zPos);
    }

    public void setPositionAndRotation(double xPos, double yPos, double zPos, float pitch, float yaw) {
        invoke("setPositionAndRotation", xPos, yPos, zPos, pitch, yaw);
    }

    public boolean isOutsideBorder() {
        return (boolean) invoke("isOutsideBorder");
    }

    /**
     * Granite Methods
     */

    public void teleportToPlayer(Player player) {
        GranitePlayer player2 = (GranitePlayer) player;
        setLocation(player2.getLocation());
    }

    public double getDistanceToLocation(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("getDistanceToLocation", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    public double getDistanceSqToLocation(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("getDistanceSqToLocation", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    public Location getLocation() {
        return new Location(getWorld(), getX(), getY(), getZ(), getPitch(), getYaw());
    }

    public void setLocation(Location location) {
        setWorld(location.getWorld());
        setX(location.getX());
        setY(location.getY());
        setZ(location.getZ());
        setYaw(location.getYaw());
        setPitch(location.getPitch());
    }

    public double getX() {
        return (double) fieldGet("posX");
    }

    public void setX(double xPos) {
        fieldSet("posX", xPos);
    }

    public double getY() {
        return (double) fieldGet("posY");
    }

    public void setY(double yPos) {
        fieldSet("posY", yPos);
    }

    public double getZ() {
        return (double) fieldGet("posZ");
    }

    public void setZ(double zPos) {
        fieldSet("posZ", zPos);
    }

    public float getPitch() {
        return (float) fieldGet("rotationPitch");
    }

    public void setPitch(float pitch) {
        fieldSet("rotationPitch", pitch);
    }

    public float getYaw() {
        return (float) fieldGet("rotationYaw");
    }

    public void setYaw(float yaw) {
        fieldSet("rotationYaw", yaw);
    }

    public Entity getEntityRidingThis() {
        return (Entity) MinecraftUtils.wrap(fieldGet("riddenByEntity"));
    }

    public Entity getEntityRiddenByThis() {
        return (Entity) MinecraftUtils.wrap(fieldGet("ridingEntity"));
    }

    @Override
    public String getType() {
        return (String) invoke("getEntityString");
    }

}
