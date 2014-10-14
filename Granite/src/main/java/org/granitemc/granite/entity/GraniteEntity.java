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
        invoke("Entity", "setSize", width, height);
    }

    public void setFire(int seconds) {
        invoke("setFire", seconds);
    }

    public void extinguish() {
        invoke("Entity", "extinguish");
    }

    public void kill() {
        invoke("Entity", "kill");
    }

    public void playSound(String soundName, float volume, float pitch) {
        invoke("Entity", "playSound", soundName, volume, pitch);
    }

    public boolean isImmuneToFire() {
        return (boolean) invoke("Entity", "isImmuneToFire");
    }

    public boolean isWet() {
        return (boolean) invoke("Entity", "isWet");
    }

    public boolean isInWater() {
        return (boolean) invoke("Entity", "isInWater");
    }

    public void resetHeight() {
        invoke("Entity", "resetHeight");
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
        return (float) invoke("Entity", "getDistanceToEntity", entity);
    }

    public double getDistanceSqToEntity(Entity entity) {
        return (double) invoke("Entity", "getDistanceSqToEntity", entity);
    }

    public void addVelocity(double x, double y, double z) {
        invoke("Entity", "addVelocity", x, y, z);
    }

    public boolean canBeCollidedWith() {
        return (boolean) invoke("Entity", "canBeCollidedWith");
    }

    public boolean canBePushed() {
        return (boolean) invoke("Entity", "canBePushed");
    }

    public EntityItem entityDropItem(ItemStack itemStack, float yPos) {
        return (EntityItem) MinecraftUtils.wrap(invoke("entityDropItem", itemStack, yPos));
    }

    public boolean isEntityAlive() {
        return (boolean) invoke("Entity", "isEntityAlive");
    }

    public boolean isEntityInsideOpaqueBlock() {
        return (boolean) invoke("Entity", "isEntityInsideOpaqueBlock");
    }

    public void mountEntity(Entity entity) {
        invoke("Entity", "mountEntity", entity);
    }

    public boolean isEating() {
        return (boolean) invoke("Entity", "isEating");
    }

    public void setEating(boolean eating) {
        invoke("Entity", "setEating", eating);
    }

    public ItemStack[] getInventory() {
        return (ItemStack[]) MinecraftUtils.wrap(invoke("Entity", "getInventory"));
    }

    public void setCurrentItemOrArmor(int inventoryIndex, ItemStack itemStack) {
        invoke("Entity", "setCurrentItemOrArmor", inventoryIndex, itemStack);
    }

    public boolean isBurning() {
        return (boolean) invoke("Entity", "isBurning");
    }

    public boolean isRiding() {
        return (boolean) invoke("Entity", "isRiding");
    }

    public boolean isSneaking() {
        return (boolean) invoke("Entity", "isSneaking");
    }

    public void setSneaking(boolean sneaking) {
        invoke("Entity", "setSneaking", sneaking);
    }

    public boolean isSprinting() {
        return (boolean) invoke("Entity", "isSprinting");
    }

    public void setSprinting(boolean sprinting) {
        invoke("Entity", "setSprinting", sprinting);
    }

    public boolean isInvisible() {
        return (boolean) invoke("Entity", "isInvisible");
    }

    public void setInvisible(boolean invisible) {
        invoke("Entity", "isInvisible", invisible);
    }

    public boolean getFlag(int flag) {
        return (boolean) invoke("getFlag", flag);
    }

    public void setFlag(int flag, boolean bool) {
        invoke("setFlag", flag, bool);
    }

    public int getAir() {
        return (Integer) invoke("Entity", "getAir");
    }

    public void setAir(int amount) {
        invoke("Entity", "setAir", amount);
    }

    public void setInWeb() {
        invoke("Entity", "setInWeb");
    }

    public String getCommandSenderName() {
        return (String) invoke("Entity", "getCommandSenderName");
    }

    public Entity[] getParts() {
        Object[] nativeParts = (Object[]) invoke("Entity", "getParts");
        Entity[] parts = new Entity[nativeParts.length];

        for (int i = 0; i < nativeParts.length; i++) {
            parts[i] = (Entity) MinecraftUtils.wrap(nativeParts[i]);
        }

        return parts;
    }

    public boolean isEntityEqual(Entity entity) {
        return (boolean) invoke("Entity", "isEntityEqual", entity);
    }

    public boolean canAttackWithItem() {
        return (boolean) invoke("Entity", "canAttackWithItem");
    }

    public int getTeleportDirection() {
        return (Integer) invoke("Entity", "getTeleportDirection");
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return (boolean) invoke("Entity", "doesEntityNotTriggerPressurePlate");
    }

    public UUID getUniqueID() {
        return (UUID) invoke("Entity", "getUniqueID");
    }

    public boolean isPushedByWater() {
        return (boolean) invoke("Entity", "isPushedByWater");
    }

    // TODO: wait until IChatComponent has been wrapped
    /*public ho getDisplayName() {
        return (ho) invoke("getDisplayName");
    }*/

    public void setCustomNameTag(String name) {
        invoke("Entity", "setCustomNameTag", name);
    }

    public String getCustomNameTag() {
        return (String) invoke("Entity", "getCustomNameTag");
    }

    public boolean hasCustomName() {
        return (boolean) invoke("Entity", "hasCustomName");
    }

    public void setAlwaysRenderNameTag(boolean bool) {
        invoke("Entity", "setAlwaysRenderNameTag", bool);
    }

    public boolean getAlwaysRenderNameTag() {
        return (boolean) invoke("Entity", "getAlwaysRenderNameTag");
    }

    public void setPosition(double xPos, double yPos, double zPos) {
        invoke("Entity", "setPosition", xPos, yPos, zPos);
    }

    public void setPositionAndRotation(double xPos, double yPos, double zPos, float pitch, float yaw) {
        invoke("Entity", "setPositionAndRotation", xPos, yPos, zPos, pitch, yaw);
    }

    public boolean isOutsideBorder() {
        return (boolean) invoke("Entity", "isOutsideBorder");
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
            return (double) invoke("Entity", "getDistanceToLocation", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    public double getDistanceSqToLocation(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("Entity", "getDistanceSqToLocation", location.getX(), location.getY(), location.getZ());
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
        return (Entity) MinecraftUtils.wrap(fieldGet("Entity", "riddenByEntity"));
    }

    public Entity getEntityRiddenByThis() {
        return (Entity) MinecraftUtils.wrap(fieldGet("Entity", "ridingEntity"));
    }

    @Override
    public String getType() {
        return (String) invoke("getEntityString");
    }

}
