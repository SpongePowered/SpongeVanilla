/****************************************************************************************
 * License (MIT)                                                                        *
 *                                                                                      *
 * Copyright (c) 2014. Granite Team                                                     *
 *                                                                                      *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this *
 * software and associated documentation files (the "Software"), to deal in the         *
 * Software without restriction, including without limitation the rights to use, copy,  *
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,  *
 * and to permit persons to whom the Software is furnished to do so, subject to the     *
 * following conditions:                                                                *
 *                                                                                      *
 * The above copyright notice and this permission notice shall be included in all       *
 * copies or substantial portions of the Software.                                      *
 *                                                                                      *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,  *
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A        *
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT   *
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION    *
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE       *
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                               *
 ****************************************************************************************/

package org.granitemc.granite.entities;

import org.granitemc.granite.entities.player.EntityPlayer;
import org.granitemc.granite.reflect.Composite;
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class Entity extends Composite {
    public Entity(Object parent) {
        super(parent);
    }

    public int getEntityId() {
        //Obf: F
        return (Integer) invoke("getEntityID");
    }

    public void setEntityId(int Id) {
        //Obf: d
        invoke("setEntityId", Id);
    }

    public Object getDataWatcher() {
        //Obf: H
        return invoke("getDataWatcher");
    }

    public void setDead() {
        //Obf: J
        invoke("setDead");
    }

    public void setSize(float width, float height) {
        //Obf: a
        invoke("setSize", width, height);
    }

    public void setRotation(float yaw, float pitch) {
        //Obf: b
        invoke("setRotation", yaw, pitch);
    }

    public void setPosition(double x, double y, double z) {
        //Obf: b
        invoke("setPosition", x, y, z);
    }

    public void onUpdate() {
        //Obf: s_
        invoke("onUpdate");
    }

    public void onEntityUpdate() {
        //Obf: K
        invoke("onEntityUpdate");
    }

    public int getMaxInPortalTime() {
        //Obf: L
        return (Integer) invoke("getMaxInPortalTime");
    }

    public void setOnFireFromLava() {
        //Obf: M
        invoke("setOnFireFromLava");
    }

    public void setFire(int seconds) {
        //Obf: e
        invoke("setFire", seconds);
    }

    public void extinguish() {
        //Obf: N
        invoke("extinguish");
    }

    public void kill() {
        //Obf: O
        invoke("kill");
    }

    public boolean isOffsetPositionInLiquid(double x, double y, double z) {
        //Obf: c
        return (boolean) invoke("isOffsetPositionInLiquid", x, y, z);
    }

    //TODO: What does this do?
    /*private boolean b(brt var1) {
        //Obf: b
        return (boolean) invoke("b", var1);
    }*/

    public void moveEntity(double x, double y, double z) {
        //Obf: d
        invoke("moveEntity", x, y, z);
    }

    //TODO: What does this do?
    /*private void m() {
        //Obf: m
        invokve("m");
    }*/

    //TODO: What does this do?
    @Deprecated
    public void func_145775_I() {
        //Obf: Q
        invoke("func_145775_I");
    }

    //TODO: What does this do?
    @Deprecated
    protected void func_145780_a(Object var1, Object var2) {
        //Obf: a
        invoke("func_145780_a", var1, var2);
    }

    //TODO: Work out what var1 and var2 do
    public void playSound(String soundName, float var1, float var2) {
        //Obf: a
        invoke("playSound", soundName, var1, var2);
    }

    public boolean canTriggerWalking() {
        //Obf: r_
        return (boolean) invoke("canTriggerWalking");
    }

    //TODO: Work out what var4 (class atr) and var5 (class dt) do
    @Deprecated
    public void updateFallState(double distanceFallenThisTick, boolean onGround, Object var4, Object var5) {
        //Obf: a
        invoke("updateFallState", distanceFallenThisTick, onGround, var4, var5);
    }

    public Object getBoundingBox() {
        //Obf: S
        return invoke("getBoundingBox");
    }

    public void dealFireDamage(int amountDamage) {
        //Obf: f
        invoke("dealFireDamage", amountDamage);
    }

    public boolean isImmuneToFire() {
        //Obf: K
        return (boolean) invoke("isImmuneToFire");
    }

    public void fall(float var1, float var2) {
        //Obf: e
        invoke("fall", var1, var2);
    }

    public boolean isWet() {
        //Obf: U
        return (boolean) invoke("isWet");
    }

    public boolean isInWater() {
        //Obf: V
        return (boolean) invoke("isInWater");
    }

    public boolean handleWaterMovement() {
        //Obf: W
        return (boolean) invoke("handleWaterMovement");
    }

    //TODO: What does this do?
    /*public void X() {
        //Obf: X
        invoke("X");
    }*/

    //TODO: What does this do?
    /*public void Y() {
        //Obj: Y
        invoke("Y");
    }*/

    public String getSplashSound() {
        //Obf: aa
        return (String) invoke("getSplashSound");
    }

    //TODO: Work out what var1 (class bof) is
    /*public boolean isInsideOfMaterial(Object var1) {
        //Obf: a
        return (boolean) invoke("isInsideOfMaterial");
    }*/

    public boolean handleLavaMovement() {
        //Obf: ab
        return (boolean) invoke("handleLavaMovement");
    }

    //TODO: Work out what var1,2 and 3 do?
    /*public void moveFlying(float var1, float var2, float var3) {
        //Obf: a
        invoke("moveFlying", var1, var2, var3);
    }*/

    //TODO: Work out what var1 does
    /*public float getBrightness(float var1) {
        //Obf: c
        return (float) invoke("getBrightness", var1);
    }*/

    //TODO: Work out what class aqu is
    /*public void setWorld(Object var1) {
        //Obf: a
        invoke("setWorld", var1);
    }*/

    //TODO: Work out what class dt is
    /*public void a(Object var1, float var2, float var3) {
        //Obf: a
        invoke("a", var1, var2, var3);
    }*/

    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        //Obf: b
        invoke("setLocationAndAngles", x, y, z, yaw, pitch);
    }

    public float getDistanceToEntity(Entity entity) {
        //Obf: g
        return (float) invoke("getDistanceToEntity", entity);
    }

    public double getDistanceSq(double x, double y, double z) {
        //Obf: e
        return (double) invoke("getDistanceSq", x, y, z);
    }

    //TODO: Find out what class dt is
    /*public double b(Object var1) {
        //Obf: b
        return (double) invoke("b", var1);
    }*/

    //TODO: Find out what class dt is
    /*public double c(Object var1) {
        //Obf: c
        return (double) invoke("b", var1);
    }*/

    public double getDistance(double x, double y, double z) {
        //Obf: f
        return (double) invoke("getDistance", x, y, z);
    }

    public double getDistanceSqToEntity(Entity entity) {
        //Obf: h
        return (double) invoke("getDistanceSqToEntity", entity);
    }

    public void onCollideWithPlayer(EntityPlayer entityPlayer) {
        //Obf: d
        invoke("onCollideWithPlayer", entityPlayer);
    }

    public void applyEntityCollision(Entity entity) {
        //Obf: i
        invoke("applyEntityCollision", entity);
    }

    public void addVelocity(double x, double y, double z) {
        //Obf: g
        invoke("addVelocity", x, y, z);
    }

    public void setBeenAttacked() {
        //Obf: ac
        invoke("setBeenAttacked");
    }

    //TODO: Work out what class ro is and what var2 does
    public void attackEntityFrom(Object var1, float var2) {
        //Obf: a
        invoke("attackEntityFrom", var1, var2);
    }

    //TODO: Work out what class brw is and what var1 does
    /*public brw d(float var1) {
        //Obf: d
        return (brw) invoke("d", var1);
    }*/

    public boolean canBeCollidedWith() {
        //Obf: ad
        return (boolean) invoke("canBeCollidedWith");
    }

    public boolean canBePushed() {
        //Obf: ae
        return (boolean) invoke("canBePushed");
    }

    //Currently does nothing
    public void addToPlayerScore(Entity entity, int amount) {
        //Obf: b
        invoke("addToPlayerScore", entity, amount);
    }

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public boolean writeMountToNBT(fn var1) {
        //Obf: c
        return (boolean) invoke("writeMountToNBT", var1);
    }*/

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public boolean writeToNBTOptional(fn var1) {
        //Obf: d
        return (boolean) invoke("writeToNBTOptional", var1);
    }*/

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public void writeToNBT(fn var1) {
        //Obf: e
        invoke("writeToNBT", var1);
    }*/

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public void readFromNBT(fn var1) {
        //Obf: f
        invoke("readFromNBT", var1);
    }*/

    //TODO: Do we really need this?
    public boolean shouldSetPosAfterLoading() {
        //Obf: af
        return (boolean) invoke("shouldSetPosAfterLoading");
    }

    public String getEntityString() {
        //Obf: ag
        return (String) invoke("getEntityString");
    }

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public void readEntityFromNBT(fn var1) {
        //Obf: a
        invoke("readEntityFromNBT", var1);
    }*/

    //TODO: Find out what class fn is.
    //TODO: Do we really need this?
    /*public void writeEntityToNBT(fn var1) {
        //Obf: b
        invoke("writeEntityToNBT", var1);
    }*/

    //TODO: Currently not used
    @Deprecated
    public void onChunkLoad() {
        //Obf: ah
        invoke("onChunkLoad");
    }

    //TODO: Find out what class fv is.
    //TODO: Find out what var1 does.
    //TODO: Do we really need this?
    /*public fv newDoubleNBTList(double ... var1) {
        //Obf: a
        return (fv) invoke("newDoubleNBTList", var1);
    }*/

    //TODO: Find out what class fv is.
    //TODO: Find out what var1 does.
    //TODO: Do we really need this?
    /*public fv newFloatNBTList(float ... var1) {
        //Obf: a
        return (fv) invoke("newFloatNBTList", var1);
    }*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw func_145779_a(alq var1, int var2) {
        //Obf: s
        return (adw) invoke("func_145779_a", var1, var2);
    }*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw func_145778_a(alq var1, int var2, float var3) {
        //Obf: a
        return (adw) invoke("func_145778_a", var1, var2, var3);
    }*/

    //TODO: find out what adw and amj class is
    //TODO: what do the vars do?
    /*public adw entityDropItem(amj var1, float var2) {
        //Obf: a
        return (adw) invoke("entityDropItem", var1, var2);
    }*/

    public boolean isEntityAlive() {
        //Obf: ai
        return (boolean) invoke("isEntityAlive");
    }

    public boolean isEntityInsideOpaqueBlock() {
        //Obf: aj
        return (boolean) invoke("isEntityInsideOpaqueBlock");
    }

    //TODO: find out what class ahd is
    /*public boolean interactFirst(ahd var1) {
        //Obf:
        return (boolean) invoke("interactFirst", var1);
    }*/

    //TODO: work out what class brt is
    /*public brt getCollisionBox() {
        //Obf: j
        return (brt) invoke("getCollisionBox");
    }*/

    public void updateRidden() {
        //Obf: ak
        invoke("updateRidden");
    }

    public void updateRiderPosition() {
        //Obf: al
        invoke("updateRiderPosition");
    }

    public double getYOffset() {
        //Obf: am
        return (double) invoke("getYOffset");
    }

    public double getMountedYOffset() {
        //Obf: an
        return (double) invoke("getMountedYOffset");
    }

    public void mountEntity(Entity entity) {
        //Obf: a
        invoke("mountEntity", entity);
    }

    public float getCollisionBorderSize() {
        //Obf: ao
        return (float) invoke("getCollisionBorderSize");
    }

    //TODO: work out what class brt is
    /*public brw getLookVector() {
        //Obf: ap
        return (brw) invoke("getLookVector");
    }*/

    public boolean isEating() {
        //Obf: aq
        return (boolean) invoke("isEating");
    }

    public void setEating(boolean var1) {
        //Obf: f
        invoke("setEating", var1);
    }

    //TODO: work out what class amj is
    /*public amj[] getInventory() {
        //Obf: at
        return (amj[]) invoke("getInventory");
    }*/

    //TODO: work out what class amj is
    /*public void setCurrentItemOrArmor(int var1, amj var2) {
        //Obf: c
        invoke("setCurrentItemOrArmor", var1, var2);
    }*/

    public int getPortalCooldown() {
        //Obf: ar
        return (Integer) invoke("getPortalCooldown");
    }

    public boolean isBurning() {
        //Obf: au
        return (boolean) invoke("isBurning");
    }

    public boolean isRiding() {
        //Obf: av
        return (boolean) invoke("isRiding");
    }

    public boolean isSneaking() {
        //Obf: aw
        return (boolean) invoke("isSneaking");
    }

    public void setSneaking(boolean var1) {
        //Obf: c
        invoke("setSneaking", var1);
    }

    public boolean isSprinting() {
        //Obf: ax
        return (boolean) invoke("isSprinting");
    }

    public void setSprinting(boolean var1) {
        //Obf: d
        invoke("setSprinting", var1);
    }

    public boolean isInvisible() {
        //Obf: ay
        return (boolean) invoke("isInvisible");
    }

    public void setInvisible(boolean var1) {
        //Obf: e
        invoke("setInvisible", var1);
    }

    public boolean getFlag(int flag) {
        //Obf: g
        return (boolean) invoke("getFlag", flag);
    }

    public void setFlag(int flag, boolean var2) {
        //Obf: b
        invoke("setFlag", flag, var2);
    }

    public int getAir() {
        //Obf: aA
        return (Integer) invoke("getAir");
    }

    public void setAir(int var1) {
        //Obf: h
        invoke("setAir", var1);
    }

    //TODO: Find out what class ads is
    /*public void onStruckByLightning(ads var1) {
        //Obf: a
        invoke("onStruckByLightning", var1);
    }*/

    //TODO: Find out what class xm is
    /*public void onKillEntity(xm var1) {
        //Obf: a
        invoke("onKillEntity", var1);
    }*/

    //TODO: Work out a suitable name and what the vars do
    @Deprecated
    public boolean func_145771_j(double var1, double var2, double var3) {
        //Obf: j
        return (boolean) invoke("func_145771_j", var1, var2, var3);
    }

    public void setInWeb() {
        //Obf: aB
        invoke("setInWeb");
    }

    public String getCommandSenderName() {
        //Obf: d_
        return (String) invoke("getCommandSenderName");
    }

    public Entity[] getParts() {
        //Obf: aC
        return (Entity[]) invoke("getParts");
    }

    public boolean isEntityEqual(Entity entity) {
        //Obf: k
        return (boolean) invoke("isEntityEqual", entity);
    }

    public float getRotationYawHead() {
        //Obf: aD
        return (float) invoke("getRotationYawHead");
    }

    //TODO: Find a suitable name (currently not used)
    public void f(float var1) {
        //Obf: f
        invoke("f");
    }

    public boolean canAttackWithItem() {
        //Obf: aE
        return (boolean) invoke("canAttackWithItem");
    }

    public boolean hitByEntity(Entity entity) {
        //Obf: l
        return (boolean) invoke("hitByEntity", entity);
    }

    public void copyDataFrom(Entity entity) {
        //Obf: n
        invoke("copyDataFrom", entity);
    }

    public void travelToDimension(int dimension) {
        //Obf: c
        invoke("travelToDimension", dimension);
    }

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public float func_145772_a(aqo var1, aqu var2, dt var3, bec var4) {
        //Obf: a
        return (float) invoke("func_145772_a", aqo var1, aqu var2, dt var3, bec var4);
    }*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public boolean func_145774_a(aqo var1, aqu var2, dt var3, bec var4, float var5) {
        //Obf: a
        return (boolean) invoke("func_145774_a", aqo var1, aqu var2, dt var3, bec var4, float var5);
    }*/

    public int getMaxSafePointTries() {
        //Obf: aF
        return (Integer) invoke("getMaxSafePointTries");
    }

    public int getTeleportDirection() {
        //Obf: aG
        return (Integer) invoke("getTeleportDirection");
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        //Obf: aH
        return (boolean) invoke("doesEntityNotTriggerPressurePlate");
    }

    //TODO: find out what class j is
    /*public void addEntityCrashInfo(j var1) {
        //Obf: a
        invoke("addEntityCrashInfo", var1);
    }*/

    public UUID getUniqueID() {
        //Obf: aJ
        return (UUID) invoke("getUniqueID");
    }

    public boolean isPushedByWater() {
        //Obf: aK
        return (boolean) invoke("isPushedByWater");
    }

    //TODO: Find Suitable name and get ho class
    /*public ho func_145748_c_() {
        //Obf: e_
        return (ho) invoke("func_145748_c_");
    }*/

    //TODO: These are new in 1.8?
    /**
     public void a(String var1) {
     //Obf: a
     invoke("a", var1);
     }

     public String aL() {
     //Obf: aL
     return (String) invoke("aL")
     }

     //HERE!!!!!!!
     public boolean k_() {
     //Obf: k_
     return (boolean) invoke("k_");
     }

     public void g(boolean var1) {
     //Obf: g
     invoke("g", var1);
     }

     public boolean aM() {
     //Obf: aM
     return (boolean) invoke("aM");
     }

     public void a(double var1, double var3, double var5) {
     //Obf: a
     invoke("a", var1, var3, var5);
     }*/

    //TODO: Find Suitable name (not currently used)
    /*public void func_145781_i(int var1) {
        //Obf: i
        invoke("func_145781_i", var1);
    }*/

    //TODO: These are new in 1.8?

    /**
     * public ej aO() {
     * //Obf: aO
     * return (ej) invoke("aO");
     * }
     *
     * protected hr aP() {
     * //Obf: aP
     * return (hr) invoke("aO");
     * }
     *
     * public boolean a(qw var1) {
     * //Obf: a
     * return (boolean) invoke("a");
     * }
     *
     * public brt aQ() {
     * //Obf: aQ
     * return (brt) invoke("aQ");
     * }
     *
     * public void a(brt var1) {
     * //Obf: a
     * invoke("a", var1);
     * }
     *
     * public float aR() {
     * //Obf: aR
     * return (float) invoke("aR");
     * }
     *
     * public boolean aS() {
     * //Obf: aS
     * return (boolean) invoke("aS");
     * }
     *
     * public void h(boolean var1) {
     * //Obf: h
     * invoke("h", var1);
     * }
     *
     * public boolean d(int var1, amj var2) {
     * //Obf: d
     * return (boolean) invoke("d", var1, var2);
     * }
     *
     * //TODO: ? (not currently used)
     * public void a(ho var1) {
     * //Obf: a
     * }
     *
     * public boolean a(int var1, String var2) {
     * //Obf: a
     * return (boolean) invoke("a", var1, var2);
     * }
     *
     * public dt c() {
     * //Obf: c
     * return (dt) invoke("c");
     * }
     *
     * public brw d() {
     * //Obf: d
     * return (brw) invoke("d");
     * }
     *
     * public aqu e() {
     * //Obf: e
     * return (aqu) invoke("e")
     * }
     *
     * public Entity f() {
     * //Obf: f
     * return (Entity) invoke("f");
     * }
     *
     * public boolean t_() {
     * //Obf: t_
     * return (boolean) invoke("t_");
     * }
     *
     * public void a(ag var1, int var2) {
     * //Obf: a
     * invoke("a", var1, var2);
     * }
     *
     * public af aT() {
     * //Obf: aT
     * return (af) invoke("aT");
     * }
     *
     * public void o(Entity var1) {
     * //Obf: o
     * invoke("o", var1);
     * }
     *
     * public fn aU() {
     * //Obf: aU
     * return (fn) invoke("aU");
     * }
     *
     * public boolean a(ahd var1, brw var2) {
     * //Obf: a
     * return (boolean) invoke("a", var1, var2);
     * }
     *
     * public boolean aV() {
     * //Obf: aV
     * return (boolean) invoke("aV");
     * }
     * 
     * protected void a(xm var1, wv var2) {
     * //Obf: a
     * invoke("a", var1, var2);
     * }
     */
}
