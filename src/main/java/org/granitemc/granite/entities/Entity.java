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
import org.granitemc.granite.utils.Mappings;

import java.lang.reflect.InvocationTargetException;

public class Entity {

    private Object entityInstance = null;

    public Entity(Object instance) {
        instance = entityInstance;
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

    //TODO: Upto line 1145 (e) 1.8

    private Object invoke(String targetMethod, Object... parameters) {
        try {
            String targetClass = "net.minecraft.entity.EntityPlayer";
            return Mappings.getMethod(targetClass, targetMethod).invoke(entityInstance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
