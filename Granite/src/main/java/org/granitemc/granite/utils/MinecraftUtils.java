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

import org.granitemc.granite.api.chat.ChatComponent;
import org.granitemc.granite.api.utils.Location;
import org.granitemc.granite.api.utils.Rotations;
import org.granitemc.granite.api.utils.Vector;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.entity.GraniteEntity;
import org.granitemc.granite.entity.item.GraniteEntityItem;
import org.granitemc.granite.entity.player.GraniteEntityPlayer;
import org.granitemc.granite.inventory.GraniteInventory;
import org.granitemc.granite.inventory.GranitePlayerInventory;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.item.GraniteItemType;
import org.granitemc.granite.reflect.GraniteServerComposite;
import org.granitemc.granite.reflect.PlayServerComposite;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;

public class MinecraftUtils {
    public static Object wrap(Object object) {
        if (Mappings.getClass("MinecraftServer").isInstance(object)) {
            return GraniteServerComposite.instance;
        } else if (Mappings.getClass("EntityPlayerMP").isInstance(object)) {
            return GraniteEntityPlayer.new_(object, GraniteEntityPlayer.class);
        } else if (Mappings.getClass("EntityItem").isInstance(object)) {
            return GraniteEntityItem.new_(object, GraniteEntityItem.class);
        } else if (Mappings.getClass("Entity").isInstance(object)) {
            return GraniteEntity.new_(object, GraniteEntity.class);
        } else if (Mappings.getClass("InventoryPlayer").isInstance(object)) {
            return GranitePlayerInventory.new_(object, GranitePlayerInventory.class);
        } else if (Mappings.getClass("IInventory").isInstance(object)) {
            return GraniteInventory.new_(object, GraniteInventory.class);
        } else if (Mappings.getClass("IBlockWithMetadata").isInstance(object)) {
            return GraniteBlockType.new_(object, GraniteBlockType.class);
        } else if (Mappings.getClass("Item").isInstance(object)) {
            return GraniteItemType.new_(object, GraniteItemType.class);
        } else if (Mappings.getClass("World").isInstance(object)) {
            return GraniteWorld.new_(object, GraniteWorld.class);
        } else if (Mappings.getClass("ItemStack").isInstance(object)) {
            return GraniteItemStack.new_(object, GraniteItemStack.class);
        } else if (Mappings.getClass("NetHandlerPlayServer").isInstance(object)) {
            return PlayServerComposite.new_(object, PlayServerComposite.class);
        }
        return null;
    }

    public static Object toMinecraftLocation(Location graniteLocation) {
        try {
            return Mappings.getClass("BlockPos").getConstructor(int.class, int.class, int.class).newInstance((int) graniteLocation.getX(), (int) graniteLocation.getY(), (int) graniteLocation.getZ());
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toMinecraftLocation(int x, int y, int z) {
        try {
            return Mappings.getClass("BlockPos").getConstructor(int.class, int.class, int.class).newInstance(x, y, z);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location fromMinecraftLocation(World w, Object minecraftLocation) {
        try {
            return new Location(w,
                    (float) Mappings.getField("BlockPos", "x").get(minecraftLocation),
                    (float) Mappings.getField("BlockPos", "y").get(minecraftLocation),
                    (float) Mappings.getField("BlockPos", "z").get(minecraftLocation)
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object toMinecraftRotations(Rotations graniteRotations) {
        try {
            return Mappings.getClass("Rotations").getConstructor(float.class, float.class, float.class)
                    .newInstance(graniteRotations.getXRotation(), graniteRotations.getYRotation(), graniteRotations.getZRotation());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Rotations fromMinecraftRotations(Object minecraftRotations) {
        try {
            return new Rotations(
                    (float) Mappings.getField("Rotations", "x").get(minecraftRotations),
                    (float) Mappings.getField("Rotations", "y").get(minecraftRotations),
                    (float) Mappings.getField("Rotations", "z").get(minecraftRotations)
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Vector fromMinecraftVector(Object minecraftVector) {
        try {
            return new Vector(
                    (double) Mappings.getField("Vec3", "xCoord").get(minecraftVector),
                    (double) Mappings.getField("Vec3", "yCoord").get(minecraftVector),
                    (double) Mappings.getField("Vec3", "zCoord").get(minecraftVector)
            );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Object toMinecraftVector(Vector graniteVector) {
        try {
            return Mappings.getClass("Vec3").getConstructor(double.class, double.class, double.class)
                    .newInstance(graniteVector.getX(), graniteVector.getY(), graniteVector.getZ());
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTranslation(String key, Object... args) {
        return (String) Mappings.invoke(Mappings.invokeStatic("StringTranslate", "getInstance"), "translateKeyFormat", key, args);
    }

    public static Object toMinecraftChatComponent(ChatComponent component) {
        String json = component.toJson();
        return Mappings.invokeStatic("IChatComponent$Serializer", "jsonToComponent", json);
    }
}
