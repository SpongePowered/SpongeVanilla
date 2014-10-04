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

    @Override
    public int getEntityId() {
        return (Integer) invoke("getEntityID");
    }

    //TODO: this is new in 1.8
    /*public void G() {
        invoke("G");
    }*/

    @Override
    public void setDead() {
        invoke("setDead");
    }

    @Override
    public void setSize(float width, float height) {
        invoke("n.m.entity.Entity", "setSize(float;float)", width, height);
    }

    @Override
    public void setFire(int seconds) {
        invoke("n.m.entity.Entity", "setFire(int)", seconds);
    }

    @Override
    public void extinguish() {
        invoke("n.m.entity.Entity", "extinguish");
    }

    @Override
    public void kill() {
        invoke("n.m.entity.Entity", "kill");
    }

    //TODO: What does this do?
    /*private void m() {
        invoke("m");
    }*/

    //TODO: What does this do?
    /*public void Q() {
        invoke("Q");
    }*/

    //TODO: What does this do?
    /*protected void a(dt var1, atr var2) {
        invoke("a", var1, var2);
    }*/

    //TODO: Work out what var1 and var2 do (volume/pitch, maybe inverse?)
    @Override
    public void playSound(String soundName, float var1, float var2) {
        invoke("n.m.entity.Entity", "playSound(String;float;float)", soundName, var1, var2);
    }

    @Override
    public boolean isImmuneToFire() {
        return (boolean) invoke("n.m.entity.Entity", "isImmuneToFire");
    }

    @Override
    public boolean isWet() {
        return (boolean) invoke("n.m.entity.Entity", "isWet");
    }

    @Override
    public boolean isInWater() {
        return (boolean) invoke("n.m.entity.Entity", "isInWater");
    }

    //TODO: What does this do?
    /*public void X() {
        invoke("X");
    }*/

    //TODO: What does this do?
    /*public void Y() {
        invoke("Y");
    }*/

    public String getSplashSound() {
        return (String) invoke("n.m.entity.Entity", "getSplashSound");
    }

    //TODO: Work out what var1 (class bof) is
    /*public boolean isInsideOfMaterial(bof var1) {
        return (boolean) invoke("isInsideOfMaterial");
    }*/

    @Override
    public void setWorld(World world) {
        invoke("n.m.entity.Entity", "setWorld(n.m.world.World)", ((GraniteWorld) world).parent);
    }

    //TODO: Work out what class dt is
    /*public void a(dt var1, float var2, float var3) {
        invoke("a", var1, var2, var3);
    }*/

    @Override
    public float getDistanceToEntity(Entity entity) {
        return (float) invoke("n.m.entity.Entity", "getDistanceToEntity(n.m.entity.Entity)", entity);
    }

    //TODO: Find out what class dt is
    /*public double b(dt var1) {
        return (double) invoke("b", var1);
    }*/

    //TODO: Find out what class dt is
    /*public double c(dt var1) {
        return (double) invoke("b", var1);
    }*/

    @Override
    public double getDistanceSqToEntity(Entity entity) {
        //Obf:
        return (double) invoke("n.m.entity.Entity", "getDistanceSqToEntity(n.m.entity.Entity)", entity);
    }

    @Override
    public void addVelocity(double x, double y, double z) {
        invoke("n.m.entity.Entity", "addVelocity(double;double;double)", x, y, z);
    }

    @Override
    public boolean canBeCollidedWith() {
        return (boolean) invoke("n.m.entity.Entity", "canBeCollidedWith");
    }

    @Override
    public boolean canBePushed() {
        return (boolean) invoke("n.m.entity.Entity", "canBePushed");
    }

    public String getEntityString() {
        return (String) invoke("n.m.entity.Entity", "getEntityString");
    }

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw s(alq var1, int var2) {
        return (adw) invoke("s", var1, var2);
    }*/

    //TODO: find out what adw and alq class is
    //TODO: find a good name for functions
    //TODO: what do the vars do?
    /*public adw a(alq var1, int var2, float var3) {
        return (adw) invoke("a", var1, var2, var3);
    }*/

    //TODO: find out what adw is
    //TODO: what do the var2 does?
    /*public adw entityDropItem(ItemStack itemStack, float var2) {
        return (adw) invoke("entityDropItem", var1, var2);
    }*/

    @Override
    public boolean isEntityAlive() {
        return (boolean) invoke("n.m.entity.Entity", "isEntityAlive");
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return (boolean) invoke("n.m.entity.Entity", "isEntityInsideOpaqueBlock");
    }

    @Override
    public void mountEntity(Entity entity) {
        invoke("n.m.entity.Entity", "mountEntity(n.m.entity.Entity)", entity);
    }

    @Override
    public boolean isEating() {
        return (boolean) invoke("n.m.entity.Entity", "isEating");
    }

    @Override
    public void setEating(boolean eating) {
        invoke("n.m.entity.Entity", "setEating(boolean)", eating);
    }

    //TODO: add this later
    /*public ItemStack[] getInventory() {
        //Obf: at
        return (ItemStack[]) invoke("getInventory");
    }*/

    @Override
    public void setCurrentItemOrArmor(int inventoryIndex, IItemStack itemStack) {
        //Obf: c
        invoke("n.m.entity.Entity", "setCurrentItemOrArmor(int;n.m.item.ItemStack)", inventoryIndex, itemStack);
    }

    @Override
    public boolean isBurning() {
        return (boolean) invoke("n.m.entity.Entity", "isBurning");
    }

    @Override
    public boolean isRiding() {
        return (boolean) invoke("n.m.entity.Entity", "isRiding");
    }

    @Override
    public boolean isSneaking() {
        return (boolean) invoke("n.m.entity.Entity", "isSneaking");
    }

    @Override
    public void setSneaking(boolean sneaking) {
        invoke("n.m.entity.Entity", "setSneaking(boolean)", sneaking);
    }

    @Override
    public boolean isSprinting() {
        return (boolean) invoke("n.m.entity.Entity", "isSprinting");
    }

    @Override
    public void setSprinting(boolean sprinting) {
        invoke("n.m.entity.Entity", "setSprinting(boolean)", sprinting);
    }

    @Override
    public boolean isInvisible() {
        return (boolean) invoke("n.m.entity.Entity", "isInvisible");
    }

    @Override
    public void setInvisible(boolean invisible) {
        invoke("n.m.entity.Entity", "isInvisible(boolean)", invisible);
    }

    //TODO: Work out what the flags are?
    /*public boolean getFlag(int flag) {
        return (boolean) invoke("getFlag", flag);
    }/*

    /*public void setFlag(int flag, boolean var2) {
        invoke("setFlag", flag, var2);
    }*/

    @Override
    public int getAir() {
        return (Integer) invoke("n.m.entity.Entity", "getAir");
    }

    @Override
    public void setAir(int amount) {
        invoke("n.m.entity.Entity", "setAir(int)", amount);
    }

    //TODO: Find out what class ads is
    /*public void onStruckByLightning(ads var1) {
        invoke("onStruckByLightning", var1);
    }*/

    //TODO: Work out a suitable name and what the vars do
    /*public boolean j(double var1, double var2, double var3) {
        return (boolean) invoke("j", var1, var2, var3);
    }*/

    @Override
    public void setInWeb() {
        invoke("n.m.entity.Entity", "setInWeb");
    }

    @Override
    public String getCommandSenderName() {
        return (String) invoke("n.m.entity.Entity", "getCommandSenderName");
    }

    @Override
    public Entity[] getParts() {
        Object[] nativeParts = (Object[]) invoke("n.m.entity.Entity", "getParts");
        Entity[] parts = new Entity[nativeParts.length];

        for (int i = 0; i < nativeParts.length; i++) {
            parts[i] = (Entity) MinecraftUtils.wrap(nativeParts[i]);
        }

        return parts;
    }

    @Override
    public boolean isEntityEqual(Entity entity) {
        return (boolean) invoke("n.m.entity.Entity", "isEntityEqual(n.m.entity.Entity)", entity);
    }

    @Override
    public boolean canAttackWithItem() {
        return (boolean) invoke("n.m.entity.Entity", "canAttackWithItem");
    }

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public float a(aqo var1, World world, dt var3, bec var4) {
        return (float) invoke("a", aqo var1, World world, dt var3, bec var4);
    }*/

    //TODO: Find suitable name and work out what the vars do and their classes
    /*public boolean a(aqo var1, World world, dt var3, bec var4, float var5) {
        return (boolean) invoke("a", aqo var1, World world, dt var3, bec var4, float var5);
    }*/

    public int getTeleportDirection() {
        return (Integer) invoke("n.m.entity.Entity", "getTeleportDirection");
    }

    public boolean doesEntityNotTriggerPressurePlate() {
        return (boolean) invoke("n.m.entity.Entity", "doesEntityNotTriggerPressurePlate");
    }

    @Override
    public UUID getUniqueID() {
        return (UUID) invoke("n.m.entity.Entity", "getUniqueID");
    }

    @Override
    public boolean isPushedByWater() {
        return (boolean) invoke("n.m.entity.Entity", "isPushedByWater");
    }

    //TODO: Find Suitable name and get ho class
    /*public ho e_() {
        return (ho) invoke("e_");
    }*/

    //TODO: These are new in 1.8?
    /*public void a(String var1) {
        invoke("a", var1);
    }

    public String aL() {
        return (String) invoke("aL")
    }

    public boolean k_() {
        return (boolean) invoke("k_");
    }

    public void g(boolean var1) {
        invoke("g", var1);
    }

    public boolean aM() {
        return (boolean) invoke("aM");
    }

    public void a(double var1, double var3, double var5) {
        invoke("a", var1, var3, var5);
    }

    public ej aO() {
        return (ej) invoke("aO");
    }

    protected hr aP() {
        return (hr) invoke("aO");
    }

    public boolean a(qw var1) {
        return (boolean) invoke("a");
    }

    //TODO: Returns bounding box! needed?
    public brt aQ() {
        return (brt) invoke("aQ");
    }

    //TODO: Sets bounding box! needed?
    public void a(brt var1) {
        invoke("a", var1);
    }

    public float aR() {
        return (float) invoke("aR");
    }

    public boolean aS() {
        return (boolean) invoke("aS");
    }

    public void h(boolean var1) {
        invoke("h", var1);
    }

    public boolean d(int var1, amj var2) {
        return (boolean) invoke("d", var1, var2);
    }

    public boolean a(int var1, String var2) {
        return (boolean) invoke("a", var1, var2);
    }

    public dt c() {
    return (dt) invoke("c");
    }

    //TODO: Returns bounding box! needed?
    public brw d() {
        return (brw) invoke("d");
    }*/

    public World getWorld() {
        return (World) MinecraftUtils.wrap(invoke("n.m.entity.Entity", "getWorld"));
    }

    /*public Entity f() {
        return (Entity) invoke("f");
    }

    public boolean t_() {
        return (boolean) invoke("t_");
    }

    public void a(ag var1, int var2) {
        invoke("a", var1, var2);
    }

    public af aT() {
        return (af) invoke("aT");
    }

    public void o(Entity var1) {
        invoke("o", var1);
    }

    public fn aU() {
        return (fn) invoke("aU");
    }

    public boolean a(ahd var1, brw var2) {
        return (boolean) invoke("a", var1, var2);
    }

    public boolean aV() {
        return (boolean) invoke("aV");
    }

    protected void a(xm var1, Entity var2) {
        invoke("a", var1, var2);
    }*/

    /*************************************************
     *                                               *
     *              Granite Methods                  *
     *                                               *
     *************************************************/

    @Override
    public void teleportToPlayer(Player player) {
        GranitePlayer player2 = (GranitePlayer) player;
        setLocation(new Location(player2.getWorld(), player2.getX(), player2.getY(), player2.getZ(), player2.getPitch(), player2.getYaw()));
    }

    @Override
    public double getDistanceToLocation(Location location) {
        if (getWorld().equals(location.getWorld())) {
            return (double) invoke("n.m.entity.Entity", "getDistanceToLocation(double;double;double)", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
    public double getDistanceSqToLocation(Location location) {
        if (getWorld().equals(location.getWorld())) {
            return (double) invoke("n.m.entity.Entity", "getDistanceSqToLocation(double;double;double)", location.getX(), location.getY(), location.getZ());
        }
        throw new RuntimeException("You cannot get the distance between different worlds");
    }

    @Override
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

    public void setX(double x) {
        fieldSet("n.m.entity.Entity", "posX", x);
    }

    public double getY() {
        return (double) fieldGet("n.m.entity.Entity", "posY");
    }

    public void setY(double y) {
        fieldSet("n.m.entity.Entity", "posY", y);
    }

    public double getZ() {
        return (double) fieldGet("n.m.entity.Entity", "posZ");
    }

    public void setZ(double z) {
        fieldSet("n.m.entity.Entity", "posZ", z);
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

    public Entity riddenByEntity() {
        return (Entity) MinecraftUtils.wrap(fieldGet("n.m.entity.Entity", "riddenByEntity"));
    }
    public Entity ridingEntity() {
        return (Entity) MinecraftUtils.wrap(fieldGet("n.m.entity.Entity", "ridingEntity"));
    }

}
