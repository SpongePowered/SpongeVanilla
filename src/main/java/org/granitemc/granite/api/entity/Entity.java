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

import org.granitemc.granite.api.Player;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.world.Location;
import org.granitemc.granite.api.world.World;

import java.util.UUID;

public interface Entity {
    
    int getEntityId();

    //TODO: this is new in 1.8
    /*void G();*/

    boolean equals(Object object);

    void setDead();

    void setSize(float width, float height);
    
    void setFire(int seconds);
    
    void extinguish();
    
    void kill();

    //TODO: What does this do?
    /*void m();*/

    //TODO: What does this do?
    /*void Q();*/

    //TODO: What does this do?
    /*void a(dt var1, atr var2);*/

    //TODO: Work out what var1 and var2 do (volume/pitch, maybe inverse?)
    void playSound(String soundName, float var1, float var2);

    boolean isImmuneToFire();
    
    boolean isWet();
    
    boolean isInWater();

    //TODO: What does this do?
    /*void X();

    //TODO: What does this do?
    /*void Y();
    
    String getSplashSound();

    //TODO: Work out what var1 (class bof) is
    /*boolean isInsideOfMaterial(bof var1);*/

    String getSplashSound();

    void setWorld(World world);

    //TODO: Work out what class dt is
    /*void a(dt var1, float var2, float var3);*/
    
    float getDistanceToEntity(Entity entity);

    //TODO: Find out what class dt is
    /*double b(dt var1);*/

    //TODO: Find out what class dt is
    /*double c(dt var1);*/

    double getDistanceSqToEntity(Entity entity);
    
    void addVelocity(double x, double y, double z);
    
    boolean canBeCollidedWith();

    boolean canBePushed();

    String getEntityString();

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

    boolean isEntityAlive();
    
    boolean isEntityInsideOpaqueBlock();
    
    void mountEntity(Entity entity);
    
    boolean isEating();
    
    void setEating(boolean eating);

    //TODO: add this later
    /*ItemStack[] getInventory();*/

    void setCurrentItemOrArmor(int inventoryIndex, ItemStack itemStack);
    
    boolean isBurning();
    
    boolean isRiding();
    
    boolean isSneaking();
    
    void setSneaking(boolean sneaking);
    
    boolean isSprinting();
    
    void setSprinting(boolean sprinting);
    
    boolean isInvisible();
    
    void setInvisible(boolean invisible);

    //TODO: Work out what the flags are?
    /*boolean getFlag(int flag);*/

    /*void setFlag(int flag, boolean var2);*/
    
    int getAir();
    
    void setAir(int amount);

    //TODO: Find out what class ads is
    /*void onStruckByLightning(ads var1);*/

    //TODO: Work out a suitable name and what the vars do
    /*boolean j(double var1, double var2, double var3);*/
    
    void setInWeb();
    
    String getCommandSenderName();
    
    Entity[] getParts();
    
    boolean isEntityEqual(Entity entity);

    boolean canAttackWithItem();

    //TODO: Find suitable name and work out what the vars do and their classes
    /*float a(aqo var1, World world, dt var3, bec var4);*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*boolean a(aqo var1, World world, dt var3, bec var4, float var5);*/

    int getTeleportDirection();

    boolean doesEntityNotTriggerPressurePlate();

    UUID getUniqueID();

    boolean isPushedByWater();

    //TODO: Find Suitable name and get ho class
    /*ho e_();*/
    
    World getWorld();

    /*************************************************
     *                                               *
     *              Granite Methods                  *
     *                                               *
     *************************************************/

    void teleportToPlayer(Player player);

    double getDistanceToLocation(Location location);

    double getDistanceSqToLocation(Location location);

    Location getLocation();

    void setLocation(Location location);

    double getX();

    void setX(double x);

    double getY();

    void setY(double y);

    double getZ();

    void setZ(double z);

    float getPitch();

    void setPitch(float pitch);

    float getYaw();

    void setYaw(float yaw);

    Entity riddenByEntity();

    Entity ridingEntity();
}
