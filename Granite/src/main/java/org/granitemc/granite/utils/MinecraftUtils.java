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

package org.granitemc.granite.utils;

import org.granitemc.granite.api.utils.Rotations;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.chat.GraniteChatComponentText;
import org.granitemc.granite.chat.GraniteChatComponentTranslation;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.entity.item.GraniteEntityItem;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.inventory.GraniteInventory;
import org.granitemc.granite.inventory.GranitePlayerInventory;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.item.GraniteItemType;
import org.granitemc.granite.reflect.GraniteServerComposite;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;

public class MinecraftUtils {
    public static Object wrap(Object object) {
        if (Mappings.getClass("n.m.server.MinecraftServer").isInstance(object)) {
            return GraniteServerComposite.instance;
        } else if (Mappings.getClass("n.m.entity.player.EntityPlayerMP").isInstance(object)) {
            return GraniteItemStack.new_(object, GranitePlayer.class);
        } else if (Mappings.getClass("n.m.entity.EntityItem").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteEntityItem.class);
        } else if (Mappings.getClass("n.m.entity.Entity").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteEntity.class);
        } else if (Mappings.getClass("n.m.entity.player.InventoryPlayer").isInstance(object)) {
            return GraniteItemStack.new_(object, GranitePlayerInventory.class);
        } else if (Mappings.getClass("n.m.inventory.IInventory").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteInventory.class);
        } else if (Mappings.getClass("n.m.block.IBlockWithMetadata").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteBlockType.class);
        } else if (Mappings.getClass("n.m.item.Item").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteItemType.class);
        } else if (Mappings.getClass("n.m.world.World").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteWorld.class);
        } else if (Mappings.getClass("n.m.util.ChatComponentText").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteChatComponentText.class);
        } else if (Mappings.getClass("n.m.util.ChatComponentTranslation").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteChatComponentTranslation.class);
        } else if (Mappings.getClass("n.m.item.ItemStack").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteItemStack.class);
        }
        return null;
    }

    public static Object createChunkCoordinates(int x, int y, int z) {
        Class<?> ccClass = Mappings.getClass("n.m.util.ChunkCoordinates");
        try {
            return ccClass.getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toMinecraftRotations(Rotations graniteRotations) {
        try {
            return Mappings.getClass("n.m.util.Rotations").getConstructor(float.class, float.class, float.class)
                    .newInstance(graniteRotations.getXRotation(), graniteRotations.getYRotation(), graniteRotations.getZRotation());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Rotations fromMinecraftRotations(Object minecraftRotations) {
        try {
            return new Rotations(
                    (float) Mappings.getField("n.m.util.Rotations", "x").get(minecraftRotations),
                    (float) Mappings.getField("n.m.util.Rotations", "y").get(minecraftRotations),
                    (float) Mappings.getField("n.m.util.Rotations", "z").get(minecraftRotations)
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vector fromMinecraftVector(Object minecraftVector) {
        try {
            return new Vector(
                    (float) Mappings.getField("n.m.util.Vec3", "xCoord").get(minecraftVector),
                    (float) Mappings.getField("n.m.util.Vec3", "yCoord").get(minecraftVector),
                    (float) Mappings.getField("n.m.util.Vec3", "zCoord").get(minecraftVector)
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object toMinecraftVector(Vector graniteVector) {
        try {
            return Mappings.getClass("n.m.util.Vec3").getConstructor(double.class, double.class, double.class)
                    .newInstance(graniteVector.getX(), graniteVector.getY(), graniteVector.getZ());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
