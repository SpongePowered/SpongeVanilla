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

import java.util.UUID;

public interface Entity {
    int getEntityId();

    void setSize(float width, float height);

    void setRotation(float yaw, float pitch);

    void setPosition(double x, double y, double z);

    void setFire(int seconds);

    void extinguish();

    void kill();

    void moveEntity(double x, double y, double z);

    //TODO: Work out what var1 and var2 do (volume/pitch, maybe inverse?)
    void playSound(String soundName, float var1, float var2);

    boolean canTriggerWalking();

    void dealFireDamage(int amountDamage);

    boolean isImmuneToFire();

    boolean isWet();

    boolean isInWater();

    String getSplashSound();

    void setLocationAndAngles(double x, double y, double z, float yaw, float pitch);

    boolean canBeCollidedWith();

    boolean canBePushed();

    boolean isEntityAlive();

    boolean isEntityInsideOpaqueBlock();

    double getYOffset();

    double getMountedYOffset();

    void mountEntity(Entity entity);

    boolean isEating();

    void setEating(boolean var1);

    int getPortalCooldown();

    boolean isBurning();

    boolean isRiding();

    boolean isSneaking();

    void setSneaking(boolean var1);

    boolean isSprinting();

    void setSprinting(boolean var1);

    boolean isInvisible();

    void setInvisible(boolean var1);

    int getAir();

    void setAir(int var1);

    void setInWeb();

    String getCommandSenderName();

    Entity[] getParts();

    boolean isEntityEqual(Entity entity);

    float getRotationYawHead();

    boolean canAttackWithItem();

    void travelToDimension(int dimension);

    UUID getUniqueID();

    boolean isPushedByWater();
}
