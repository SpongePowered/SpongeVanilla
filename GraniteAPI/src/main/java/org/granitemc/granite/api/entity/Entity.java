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

package org.granitemc.granite.api.entity;

import org.granitemc.granite.api.entity.item.EntityItem;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.api.world.World;

import java.util.UUID;

public interface Entity {

    int getEntityId();

    void setDead();

    void setSize(float width, float height);

    int getMaxInPortalTime();

    void setOnFireFromLava();

    void setFire(int seconds);

    void extinguish();

    void kill();

    String getSwimSound();

    void playSound(String name, float volume, float pitch);

    boolean isSilent();

    void dealFireDamage(int amount);

    boolean isImmuneToFire();

    void fall(float distance, float damageMultiplier);

    boolean isWet();

    boolean isInWater();

    void createRunningParticles();

    String getSplashSound();

    boolean isInLava();

    void moveFlying(float strafe, float forward, float friction);

    void setWorld(World world);

    float getDistanceToEntity(Entity entity);

    double getDistanceSq(Location location);

    double getDistanceSqToCenter(Location location);

    double getDistance(Location location);

    double getDistanceSqToEntity(Entity entity);

    void addVelocity(double x, double y, double z);

    void setBeenAttacked();

    /*boolean attackEntityFrom(DamageSource source, float amount);*/

    Vector getLook(float vec);

    boolean canBeCollidedWith();

    boolean canBePushed();

    String getEntityString();

    EntityItem entityDropItem(ItemStack itemStack, float offsetY);

    boolean isEntityAlive();

    boolean isEntityInsideOpaqueBlock();

    boolean interactFirst(Player player);

    double getMountedYOffset();

    void mountEntity(Entity entity);

    Vector getLookVec();

    void setInPortal();

    int getPortalCooldown();

    ItemStack[] getInventory();

    void setCurrentItemOrArmor(int slot, ItemStack itemStack);

    boolean isBurning();

    boolean isRiding();

    boolean isSneaking();

    void setSneaking(boolean sneaking);

    boolean isSprinting();

    void setSprinting(boolean sprinting);

    boolean isInvisible();

    void setInvisible(boolean invisible);

    boolean isEating();

    boolean getFlag(int flag);

    void setFlag(int flag, boolean set);

    int getAir();

    void setAir(int air);

    void setInWeb();

    String getName();

    Entity[] getParts();

    boolean isEntityEqual(Entity entity);

    boolean canAttackWithItem();

    boolean hitByEntity(Entity entity);

    /*boolean isEntityInvulnerable(DamageSource p_180431_1_);*/

    void travelToDimension(int dimensionId);

    /*float getExplosionResistance(Explosion p_180428_1_, World worldIn, Location location, IBlockState p_180428_4_);*/

    int getMaxFallHeight();

    boolean doesEntityNotTriggerPressurePlate();

    UUID getUniqueID();

    /*IChatComponent getDisplayName();*/

    void setCustomNameTag(String nameTag);

    String getCustomNameTag();

    boolean hasCustomName();

    void setAlwaysRenderNameTag(boolean renderNameTag);

    boolean getAlwaysRenderNameTag();

    float getEyeHeight();

    boolean isOutsideBorder();

    /*void addChatMessage(IChatComponent message);*/

    boolean canCommandSenderUseCommand(int permissionLevel, String command);

    Vector getPositionVector();

    World getWorld();

    Entity getCommandSenderEntity();

    boolean sendCommandFeedback();

    Location getLocation();

    void setLocation(Location location);

    /**
     * Returns the entity currently riding this entity (or null)
     */
    Entity getEntityRidingThis();

    /**
     * Returns the entity we are currently riding (or null)
     */
    Entity getEntityRiddenByThis();

    /**
     * Returns the type string of this entity (e.g. Zombie for zombie, and CaveSpider for cave spider)
     */
    String getType();
}
