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

import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.entity.player.GranitePlayer;

import java.util.UUID;

public interface Entity {

    public int getEntityId();

    public void setEntityId(int Id);

    //TODO: this is new in 1.8
    public void G();

    /*public Entity(aqu var1);*/

    public void entityInit();

    /*public xv getDataWatcher();*/

    public void setDead();

    public void setSize(float width, float height);

    public void setRotation(float yaw, float pitch);

    public void setPosition(double x, double y, double z);

    public void onUpdate();

    public void onEntityUpdate();

    public int getMaxInPortalTime();

    public void setOnFireFromLava();

    public void setFire(int seconds);

    public void extinguish();

    public void kill();

    public boolean isOffsetPositionInLiquid(double x, double y, double z);

    //TODO: What does this do?
    /*private boolean b(brt var1);*/

    public void moveEntity(double x, double y, double z);

    //TODO: What does this do?
    public void m();

    //TODO: What does this do?
    public void Q();

    //TODO: What does this do?
    /*protected void a(dt var1, atr var2);*/

    //TODO: Work out what var1 and var2 do
    public void playSound(String soundName, float var1, float var2);

    public boolean canTriggerWalking();

    //TODO: ???
    /*public void updateFallState(double distanceFallenThisTick, boolean onGround, atr var4, dt var5);*/

    /*public brt getBoundingBox();*/

    public void dealFireDamage(int amountDamage);

    public boolean isImmuneToFire();

    public void fall(float var1, float var2);

    public boolean isWet();

    public boolean isInWater();

    public boolean handleWaterMovement();

    //TODO: What does this do?
    public void X();

    //TODO: What does this do?
    public void Y();

    public String getSplashSound();

    //TODO: Work out what var1 (class bof) is
    /*public boolean isInsideOfMaterial(bof var1);*/

    public boolean handleLavaMovement();

    //TODO: Work out what var1,2 and 3 do?
    public void moveFlying(float var1, float var2, float var3);

    //TODO: Work out what var1 does
    public float getBrightness(float var1);

    //TODO: Work out what class aqu is
    /*public void setWorld(aqu var1) {
        //Obf: a
        invoke("setWorld", var1);
    }*/

    //TODO: Work out what class dt is
    /*public void a(dt var1, float var2, float var3);*/

    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

    public float getDistanceToEntity(GraniteEntity entity);

    public double getDistanceSq(double x, double y, double z);

    //TODO: Find out what class dt is
    /*public double b(dt var1);*/

    //TODO: Find out what class dt is
    /*public double c(dt var1);*/

    public double getDistance(double x, double y, double z);

    public double getDistanceSqToEntity(GraniteEntity entity);

    public void onCollideWithPlayer(GranitePlayer entityPlayer);

    public void applyEntityCollision(GraniteEntity entity);

    public void addVelocity(double x, double y, double z);

    public void setBeenAttacked();

    //TODO: Work out what class ro is and what var2 does
    /*public void attackEntityFrom(ro var1, float var2);*/

    //TODO: Work out what class brw is and what var1 does
    /*public brw d(float var1);*/

    public boolean canBeCollidedWith();

    public boolean canBePushed();

    //TODO: Currently does nothing
    public void addToPlayerScore(GraniteEntity entity, int amount);

    public String getEntityString();

    //TODO: Currently not used
    public void onChunkLoad();

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw s(alq var1, int var2);*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw a(alq var1, int var2, float var3);*/

    //TODO: find out what adw and amj class is
    //TODO: what do the vars do?
    /*public adw entityDropItem(amj var1, float var2);*/

    public boolean isEntityAlive();

    public boolean isEntityInsideOpaqueBlock();

    //TODO: find out what class ahd is
    /*public boolean interactFirst(ahd var1);*/

    //TODO: work out what class brt is
    /*public brt getCollisionBox();*/

    public void updateRidden();

    public void updateRiderPosition();

    public double getYOffset();

    public double getMountedYOffset();

    public void mountEntity(GraniteEntity entity);

    public float getCollisionBorderSize();

    //TODO: work out what class brt is
    /*public brw getLookVector();*/

    public boolean isEating();

    public void setEating(boolean var1);

    //TODO: work out what class amj is
    /*public amj[] getInventory();*/

    //TODO: work out what class amj is
    /*public void setCurrentItemOrArmor(int var1, amj var2);*/

    public int getPortalCooldown();

    public boolean isBurning();

    public boolean isRiding();

    public boolean isSneaking();

    public void setSneaking(boolean var1);

    public boolean isSprinting();

    public void setSprinting(boolean var1);

    public boolean isInvisible();

    public void setInvisible(boolean var1);

    public boolean getFlag(int flag);

    public void setFlag(int flag, boolean var2);

    public int getAir();

    public void setAir(int var1);

    //TODO: Find out what class ads is
    /*public void onStruckByLightning(ads var1);*/

    //TODO: Find out what class xm is
    /*public void onKillEntity(xm var1);*/

    //TODO: Work out a suitable name and what the vars do
    public boolean j(double var1, double var2, double var3);

    public void setInWeb();

    public String getCommandSenderName();

    public GraniteEntity[] getParts();

    public boolean isEntityEqual(GraniteEntity entity);

    public float getRotationYawHead();

    //TODO: Find a suitable name (currently not used)
    public void f(float var1);

    public boolean canAttackWithItem();

    public boolean hitByEntity(GraniteEntity entity);

    public void copyDataFrom(GraniteEntity entity);

    public void travelToDimension(int dimension);

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public float a(aqo var1, aqu var2, dt var3, bec var4);*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public boolean a(aqo var1, aqu var2, dt var3, bec var4, float var5);*/

    public int getMaxSafePointTries();

    public int getTeleportDirection();

    public boolean doesEntityNotTriggerPressurePlate();

    //TODO: find out what class j is
    /*public void addEntityCrashInfo(j var1);*/

    public UUID getUniqueID();

    public boolean isPushedByWater();

    //TODO: Find Suitable name and get ho class
    /*public ho e_();*/

    //TODO: These are new in 1.8?

    /**
     * public void a(String var1);
     * <p/>
     * public String aL();
     * <p/>
     * public boolean k_();
     * <p/>
     * public void g(boolean var1);
     * <p/>
     * public boolean aM();
     * <p/>
     * public void a(double var1, double var3, double var5);
     */

    //TODO: Find Suitable name (not currently used)
    public void i(int var1);

    //TODO: These are new in 1.8?

    /**
     * public ej aO();
     *
     * protected hr aP();
     *
     * public boolean a(qw var1);
     *
     * public brt aQ();
     *
     * public void a(brt var1);
     *
     * public float aR();
     *
     * public boolean aS();
     *
     * public void h(boolean var1);
     *
     * public boolean d(int var1, amj var2);
     *
     * //TODO: ? (not currently used)
     * public void a(ho var1);
     *
     * public boolean a(int var1, String var2);
     *
     * public dt c();
     *
     * public brw d();
     *
     * public aqu e();
     *
     * public Entity f();
     *
     * public boolean t_();
     *
     * public void a(ag var1, int var2);
     *
     * public af aT();
     *
     * public void o(Entity var1);
     *
     * public fn aU();
     *
     * public boolean a(ahd var1, brw var2);
     *
     * public boolean aV();
     *
     * protected void a(xm var1, Entity var2);
     */
}
