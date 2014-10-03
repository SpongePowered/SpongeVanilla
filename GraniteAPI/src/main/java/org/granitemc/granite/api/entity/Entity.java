package org.granitemc.granite.api.entity;

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

import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.world.World;

import java.util.UUID;

public interface Entity {
    /**
     * Returns the entity ID
     */
    int getEntityId();

    //TODO: this is new in 1.8
    /*void G();*/

    boolean equals(Object object);

    /**
     * Sets the entity to be dead
     */
    void setDead();

    /**
     * Sets the size of this entity
     * @param width The width in blocks
     * @param height The height in blocks
     */
    // TODO: check if this actually does anything
    void setSize(float width, float height);

    /**
     * Sets this entity on fire
     * @param seconds The amount of seconds
     */
    void setFire(int seconds);

    /**
     * Extinguishes any fire
     */
    void extinguish();

    /**
     * Kills this entity
     */
    void kill();

    //TODO: What does this do?
    /*void m();*/

    //TODO: What does this do?
    /*void Q();*/

    //TODO: What does this do?
    /*void a(dt var1, atr var2);*/

    /**
     * Plays a sound to the entity
     * @param soundName The name of the sound played
     * @param volume The volume of the sound to be played, where 1.0F is normal
     * @param pitch The pitch of the sound to be played, where 1.0F is normal
     */
    void playSound(String soundName, float volume, float pitch);

    /**
     * Returns whether the entity is immune to fire
     */
    boolean isImmuneToFire();

    /**
     * Returns whether the entity is wet
     */
    boolean isWet();

    /**
     * Returns whether the entity is in water
     */
    // TODO: Check if this is the same as #isWet
    boolean isInWater();

    //TODO: What does this do?
    /*void X();

    //TODO: What does this do?
    /*void Y();
    
    String getSplashSound();

    //TODO: Work out what var1 (class bof) is
    /*boolean isInsideOfMaterial(bof var1);*/

    //String getSplashSound();

    /**
     * Sets the {@link org.granitemc.granite.api.world.World} this entity is in
     * @param world The world
     */
    void setWorld(World world);

    //TODO: Work out what class dt is
    /*void a(dt var1, float var2, float var3);*/

    /**
     * Returns the distance to another entity
     * @param entity The other entity
     */
    float getDistanceToEntity(Entity entity);

    //TODO: Find out what class dt is
    /*double b(dt var1);*/

    //TODO: Find out what class dt is
    /*double c(dt var1);*/

    /**
     * Returns the squared distance to another entity, this is much faster than {@link #getDistanceToEntity(Entity)}
     * @param entity The other entity
     */
    double getDistanceSqToEntity(Entity entity);

    /**
     * Adds velocity to this entity
     * @param x X velocity
     * @param y Y velocity
     * @param z Z velocity
     */
    void addVelocity(double x, double y, double z);

    /**
     * Returns whether this entity can be collided with
     */
    boolean canBeCollidedWith();

    /**
     * Returns whether this entity can be pushed
     */
    boolean canBePushed();

    //String getEntityString();

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*adw s(alq var1, int var2);*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*adw a(alq var1, int var2, float var3);*/

    //TODO: find out what adw is
    //TODO: what do the var2 does?
    /*adw entityDropItem(ItemStack itemStack, float var2);*/

    /**
     * Returns whether this entity is alive
     */
    boolean isEntityAlive();

    /**
     * Returns whether this entity is inside an opaque block
     */
    boolean isEntityInsideOpaqueBlock();

    /**
     * Mounts this entity on top of another entity
     * @param entity The other entity
     */
    void mountEntity(Entity entity);

    /**
     * Returns whether this entity is eating
     */
    boolean isEating();

    /**
     * Sets whether this entity is eating
     * @param eating Whether this entity is eating
     */
    void setEating(boolean eating);

    //TODO: add this later
    /*ItemStack[] getInventory();*/

    /**
     * Sets an inventory or armor slot
     * @param inventoryIndex The index to set
     * @param itemStack The {@link org.granitemc.granite.api.item.ItemStack} to set to
     */
    // TODO: explain index or change
    void setCurrentItemOrArmor(int inventoryIndex, ItemStack itemStack);

    /**
     * Returns whether this entity is currently burning
     */
    boolean isBurning();

    /**
     * Returns whether this entity is currently riding another entity
     */
    boolean isRiding();

    /**
     * Returns whether this entity is currently sneaking
     */
    boolean isSneaking();

    /**
     * Sets whether this entity is sneaking
     * @param sneaking Whether this entity is sneaking
     */
    void setSneaking(boolean sneaking);

    /**
     * Returns whether this entity is sprinting
     */
    boolean isSprinting();

    /**
     * Sets whether this entity is sneaking
     * @param sprinting Whether this entity is sneaking
     */
    void setSprinting(boolean sprinting);

    /**
     * Returns whether this entity is invisible
     */
    boolean isInvisible();

    /**
     * Sets whether this entity is invisible
     * @param invisible Whether this entity is invisible
     */
    void setInvisible(boolean invisible);

    //TODO: Work out what the flags are?
    /*boolean getFlag(int flag);*/

    /*void setFlag(int flag, boolean var2);*/

    /**
     * Returns how much air this entity has left
     */
    int getAir();

    /**
     * Sets how much air this entity has left
     * @param amount How much air this entity has left
     */
    void setAir(int amount);

    //TODO: Find out what class ads is
    /*void onStruckByLightning(ads var1);*/

    //TODO: Work out a suitable name and what the vars do
    /*boolean j(double var1, double var2, double var3);*/

    /**
     * Sets this entity to be in a web
     */
    void setInWeb();

    /**
     * Returns the command sender name of this entity
     */
    String getCommandSenderName();

    /**
     * Returns the parts of this entity
     */
    // TODO: figure out what this thing
    Entity[] getParts();

    /**
     * Returns whether this entity is equal to another entity
     * @param entity The other entity
     */
    boolean isEntityEqual(Entity entity);

    /**
     * Returns whether this entity can attack with an item
     */
    boolean canAttackWithItem();

    //TODO: Find suitable name and work out what the vars do and their classes
    /*float a(aqo var1, World world, dt var3, bec var4);*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*boolean a(aqo var1, World world, dt var3, bec var4, float var5);*/

    //TODO: Figure out what this does
    //int getTeleportDirection();

    //boolean doesEntityNotTriggerPressurePlate();

    /**
     * Returns the UUID of this entity
     */
    UUID getUniqueID();

    /**
     * Returns whether this entity is currently being pushed by water
     */
    boolean isPushedByWater();

    //TODO: Find Suitable name and get ho class
    /*ho e_();*/

    /**
     * Returns the {@link org.granitemc.granite.api.world.World} this entity is in
     */
    World getWorld();

    /**
     * Teleports the entity to a player
     * @param player The player to teleport to
     */
    void teleportToPlayer(Player player);

    /**
     * Returns the distance to a {@link org.granitemc.granite.api.utils.Location}
     * @param location The location
     */
    double getDistanceToLocation(Location location);

    /**
     * Returns the squared distance to a location, this is much faster than {@link #getDistanceToLocation(org.granitemc.granite.api.utils.Location)}
     * @param location The location
     */
    double getDistanceSqToLocation(Location location);

    /**
     * Returns the location of this entity
     */
    Location getLocation();

    /**
     * Returns the entity currently riding this entity (or null)
     */
    // TODO: rename this so it makes more sense
    //Entity getRiddenByEntity();

    /**
     * Returns the entity we are currently riding (or null)
     */
    // TODO: rename this so it makes more sense
    //Entity getRidingEntity();
}
