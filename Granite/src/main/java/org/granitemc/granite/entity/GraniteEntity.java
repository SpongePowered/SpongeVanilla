package org.granitemc.granite.entity;

/*****************************************************************************************
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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ****************************************************************************************/

import org.granitemc.granite.api.entity.Entity;
import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.IItemStack;
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
        invoke("n.m.entity.Entity", "setSize(float;float)", width, height);
    }

    public void setFire(int seconds) {
        invoke("n.m.entity.Entity", "setFire(int)", seconds);
    }

    public void extinguish() {
        invoke("n.m.entity.Entity", "extinguish");
    }

    public void kill() {
        invoke("n.m.entity.Entity", "kill");
    }

    public void playSound(String soundName, float volume, float pitch) {
        invoke("n.m.entity.Entity", "playSound(String;float;float)", soundName, volume, pitch);
    }

    public boolean isImmuneToFire() {
        return (boolean) invoke("n.m.entity.Entity", "isImmuneToFire");
    }

    public boolean isWet() {
        return (boolean) invoke("n.m.entity.Entity", "isWet");
    }

    public boolean isInWater() {
        return (boolean) invoke("n.m.entity.Entity", "isInWater");
    }

    public void resetHeight() {
        invoke("n.m.entity.Entity", "resetHeight");
    }

    // TODO: bof is Material
    /*public boolean isInsideOfMaterial(Material var1) {
        return (boolean) invoke("isInsideOfMaterial");
    }*/

    public World getWorld() {
        return (World) MinecraftUtils.wrap(invoke("n.m.entity.Entity", "getWorld"));
    }

    public void setWorld(World world) {
        invoke("n.m.entity.Entity", "setWorld(n.m.world.World)", ((GraniteWorld) world).parent);
    }

    public float getDistanceToEntity(Entity entity) {
        return (float) invoke("n.m.entity.Entity", "getDistanceToEntity(n.m.entity.Entity)", entity);
    }

    public double getDistanceSqToEntity(Entity entity) {
        return (double) invoke("n.m.entity.Entity", "getDistanceSqToEntity(n.m.entity.Entity)", entity);
    }

    public void addVelocity(double x, double y, double z) {
        invoke("n.m.entity.Entity", "addVelocity(double;double;double)", x, y, z);
    }

    public boolean canBeCollidedWith() {
        return (boolean) invoke("n.m.entity.Entity", "canBeCollidedWith");
    }

    public boolean canBePushed() {
        return (boolean) invoke("n.m.entity.Entity", "canBePushed");
    }

    public EntityItem entityDropItem(IItemStack itemStack, float yPos) {
        return (EntityItem) MinecraftUtils.wrap(invoke("entityDropItem", itemStack, yPos));
    }

    public boolean isEntityAlive() {
        return (boolean) invoke("n.m.entity.Entity", "isEntityAlive");
    }

    public boolean isEntityInsideOpaqueBlock() {
        return (boolean) invoke("n.m.entity.Entity", "isEntityInsideOpaqueBlock");
    }

    public void mountEntity(Entity entity) {
        invoke("n.m.entity.Entity", "mountEntity(n.m.entity.Entity)", entity);
    }

    public boolean isEating() {
        return (boolean) invoke("n.m.entity.Entity", "isEating");
    }

    public void setEating(boolean eating) {
        invoke("n.m.entity.Entity", "setEating(boolean)", eating);
    }

    public IItemStack[] getInventory() {
        return (IItemStack[]) MinecraftUtils.wrap(invoke("n.m.entity.Entity", "getInventory"));
    }

    public void setCurrentItemOrArmor(int inventoryIndex, IItemStack itemStack) {
        invoke("n.m.entity.Entity", "setCurrentItemOrArmor(int;n.m.item.ItemStack)", inventoryIndex, itemStack);
    }

    public boolean isBurning() {
        return (boolean) invoke("n.m.entity.Entity", "isBurning");
    }

    public boolean isRiding() {
        return (boolean) invoke("n.m.entity.Entity", "isRiding");
    }

    public boolean isSneaking() {
        return (boolean) invoke("n.m.entity.Entity", "isSneaking");
    }

    public void setSneaking(boolean sneaking) {
        invoke("n.m.entity.Entity", "setSneaking(boolean)", sneaking);
    }

    public boolean isSprinting() {
        return (boolean) invoke("n.m.entity.Entity", "isSprinting");
    }

    public void setSprinting(boolean sprinting) {
        invoke("n.m.entity.Entity", "setSprinting(boolean)", sprinting);
    }

    public boolean isInvisible() {
        return (boolean) invoke("n.m.entity.Entity", "isInvisible");
    }

    public void setInvisible(boolean invisible) {
        invoke("n.m.entity.Entity", "isInvisible(boolean)", invisible);
    }

    public boolean getFlag(int flag) {
        return (boolean) invoke("getFlag", flag);
    }

    public void setFlag(int flag, boolean bool) {
        invoke("setFlag", flag, bool);
    }

    public int getAir() {
        return (Integer) invoke("n.m.entity.Entity", "getAir");
    }

    public void setAir(int amount) {
        invoke("n.m.entity.Entity", "setAir(int)", amount);
    }

    public void setInWeb() {
        invoke("n.m.entity.Entity", "setInWeb");
    }

    public String getCommandSenderName() {
        return (String) invoke("n.m.entity.Entity", "getCommandSenderName");
    }

    public Entity[] getParts() {
        Object[] nativeParts = (Object[]) invoke("n.m.entity.Entity", "getParts");
        Entity[] parts = new Entity[nativeParts.length];

        for (int i = 0; i < nativeParts.length; i++) {
            parts[i] = (Entity) MinecraftUtils.wrap(nativeParts[i]);
        }

        return parts;
    }

    public boolean isEntityEqual(Entity entity) {
        return (boolean) invoke("n.m.entity.Entity", "isEntityEqual(n.m.entity.Entity)", entity);
    }

    public boolean canAttackWithItem() {
        return (boolean) invoke("n.m.entity.Entity", "canAttackWithItem");
    }

    public int getTeleportDirection() {
        return (Integer) invoke("n.m.entity.Entity", "getTeleportDirection");
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return (boolean) invoke("n.m.entity.Entity", "doesEntityNotTriggerPressurePlate");
    }

    public UUID getUniqueID() {
        return (UUID) invoke("n.m.entity.Entity", "getUniqueID");
    }

    public boolean isPushedByWater() {
        return (boolean) invoke("n.m.entity.Entity", "isPushedByWater");
    }

    // TODO: wait until IChatComponent has been wrapped
    /*public ho getDisplayName() {
        return (ho) invoke("getDisplayName");
    }*/

    public void setCustomNameTag(String name) {
        invoke("n.m.entity.Entity", "setCustomNameTag", name);
    }

    public String getCustomNameTag() {
        return (String) invoke("n.m.entity.Entity", "getCustomNameTag");
    }

    public boolean hasCustomName() {
        return (boolean) invoke("n.m.entity.Entity", "hasCustomName");
    }

    public void setAlwaysRenderNameTag(boolean bool) {
        invoke("n.m.entity.Entity", "setAlwaysRenderNameTag", bool);
    }

    public boolean getAlwaysRenderNameTag() {
        return (boolean) invoke("n.m.entity.Entity", "getAlwaysRenderNameTag");
    }

    public void setPosition(double xPos, double yPos, double zPos) {
        invoke("n.m.entity.Entity", "setPosition", xPos, yPos, zPos);
    }

    public void setPositionAndRotation(double xPos, double yPos, double zPos, float pitch, float yaw) {
        invoke("n.m.entity.Entity", "setPositionAndRotation", xPos, yPos, zPos, pitch, yaw);
    }

    public boolean isOutsideBorder() {
        return (boolean) invoke("n.m.entity.Entity", "isOutsideBorder");
    }

    /**
     * **********************************************
     * *
     * Granite Methods                  *
     * *
     * ***********************************************
     */

    public void teleportToPlayer(Player player) {
        GranitePlayer player2 = (GranitePlayer) player;
        setLocation(player2.getLocation());
    }

    public double getDistanceToLocation(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("n.m.entity.Entity", "getDistanceToLocation(double;double;double)", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    public double getDistanceSqToLocation(Location location) {
        if (getLocation().getWorld().equals(location.getWorld())) {
            return (double) invoke("n.m.entity.Entity", "getDistanceSqToLocation(double;double;double)", location.getX(), location.getY(), location.getZ());
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
        return (double) fieldGet("n.m.entity.Entity", "posX");
    }

    public void setX(double xPos) {
        fieldSet("n.m.entity.Entity", "posX", xPos);
    }

    public double getY() {
        return (double) fieldGet("n.m.entity.Entity", "posY");
    }

    public void setY(double yPos) {
        fieldSet("n.m.entity.Entity", "posY", yPos);
    }

    public double getZ() {
        return (double) fieldGet("n.m.entity.Entity", "posZ");
    }

    public void setZ(double zPos) {
        fieldSet("n.m.entity.Entity", "posZ", zPos);
    }

    public float getPitch() {
        return (float) fieldGet("n.m.entity.Entity", "rotationPitch");
    }

    public void setPitch(float pitch) {
        fieldSet("n.m.entity.Entity", "rotationPitch", pitch);
    }

    public float getYaw() {
        return (float) fieldGet("n.m.entity.Entity", "rotationYaw");
    }

    public void setYaw(float yaw) {
        fieldSet("n.m.entity.Entity", "rotationYaw", yaw);
    }

    public Entity getEntityRidingThis() {
        return (Entity) MinecraftUtils.wrap(fieldGet("n.m.entity.Entity", "riddenByEntity"));
    }

    public Entity getEntityRiddenByThis() {
        return (Entity) MinecraftUtils.wrap(fieldGet("n.m.entity.Entity", "ridingEntity"));
    }

}
