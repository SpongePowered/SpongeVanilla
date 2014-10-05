package org.granitemc.granite.reflect;

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

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.block.BlockType;
import org.granitemc.granite.api.block.BlockTypes;
import org.granitemc.granite.api.event.block.EventBlockBreak;
import org.granitemc.granite.api.event.block.EventBlockPlace;
import org.granitemc.granite.api.event.player.EventPlayerInteract;
import org.granitemc.granite.api.item.IItemStack;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.block.GraniteBlockType;
import org.granitemc.granite.entity.player.GranitePlayer;
import org.granitemc.granite.item.GraniteItemType;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ItemInWorldComposite extends ProxyComposite {
    public ItemInWorldComposite(final Object world) {
        super(Mappings.getClass("n.m.server.management.ItemInWorldManager"), new Class[]{
                Mappings.getClass("n.m.world.World"),
        }, world);


        addHook("tryHarvestBlock(n.m.util.ChunkCoordinates)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                //if (!GraniteServerComposite.instance.isOnServerThread()) {
                Player p = (Player) MinecraftUtils.wrap(fieldGet("thisPlayerMP"));

                World w = p.getWorld();

                Block b = ((GraniteWorld) w).getBlock(args[0]);

                EventBlockBreak event = new EventBlockBreak(b, p);
                Granite.getEventQueue().fireEvent(event);

                if (event.isCancelled()) {
                    hook.setWasHandled(true);
                    ((GranitePlayer) p).sendBlockUpdate(b);
                    return false;
                }
                //}
                return true;
            }
        });

        // Holy mother of method signature
        addHook("activateBlockOrUseItem(n.m.entity.player.EntityPlayer;n.m.world.World;n.m.item.ItemStack;n.m.util.ChunkCoordinates;net.minecraft.block.Direction;float;float;float)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                if (GraniteServerComposite.instance.isOnServerThread()) {
                    IItemStack itemStack = (IItemStack) MinecraftUtils.wrap(args[2]);

                    //if (Mappings.getClass("n.m.item.ItemBlock").isInstance(item)) {

                    Player p = (Player) MinecraftUtils.wrap(args[0]);

                    World w = (World) MinecraftUtils.wrap(args[1]);

                    Object chunkCoordinates = args[3];
                    int preX = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getX");
                    int preY = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getY");
                    int preZ = (int) Mappings.invoke(chunkCoordinates, "n.m.util.ChunkCoordinates", "getZ");

                    int x = preX;
                    int y = preY;
                    int z = preZ;

                    Block oldBlock = w.getBlock(x, y, z);

                    int direction = ((Enum) args[4]).ordinal();

                    if (oldBlock.getType().typeEquals(BlockTypes.snow_layer) && (int) oldBlock.getType().getMetadata("layers") == 1) {
                        // Not sure what this is for, but it's in MC's source
                        direction = 1;
                    } else if (!(boolean) Mappings.invoke(
                            ((GraniteBlockType) oldBlock.getType()).getBlockObject(),
                            "n.m.block.Block",
                            "isReplaceable(n.m.world.World;n.m.util.ChunkCoordinates)",
                            ((GraniteWorld) w).parent,
                            MinecraftUtils.createChunkCoordinates(x, y, z)
                    )) {
                        switch (direction) {
                            case 0:
                                y--;
                                break;
                            case 1:
                                y++;
                                break;
                            case 2:
                                z--;
                                break;
                            case 3:
                                z++;
                                break;
                            case 4:
                                x--;
                                break;
                            case 5:
                                x++;
                                break;
                        }
                    }

                    BlockType oldBlockType = w.getBlock(x, y, z).getType();

                    // If the item used is an ItemBlock
                    if (Mappings.getClass("n.m.item.ItemBlock").isInstance(((GraniteItemType) itemStack.getType()).parent)) {
                        // Use the "new" method, which gets the BlockType supposed to be placed, places that, and if the event gets cancelled, return it to the old one
                        // Otherwise, return it to the old one anyway, and let Minecraft itself handle the rest
                        Method m = Mappings.getMethod("n.m.block.Block", "onBlockPlaced(n.m.world.World;n.m.util.ChunkCoordinates;n.m.block.Direction;float;float;float;int;n.m.entity.EntityLivingBase)");
                        try {
                            Object itemType = ((GraniteItemType) itemStack.getType()).parent;
                            Object blockType = Mappings.invoke(itemType, "n.m.item.ItemBlock", "getBlock");

                            BlockType bt = (BlockType) MinecraftUtils.wrap(m.invoke(blockType, args[1], args[3], args[4], args[5], args[6], args[7], 3, args[0]));


                            Block b = w.getBlock(x, y, z);

                            if (((GraniteBlockType) bt).parent != null && !bt.typeEquals(BlockTypes.air)) {
                                b.setType(bt);

                                EventBlockPlace event = new EventBlockPlace(b, p);
                                Granite.getEventQueue().fireEvent(event);

                                b.setType(oldBlockType);
                                if (event.isCancelled()) {
                                    ((GranitePlayer) p).sendBlockUpdate(b);
                                } else {
                                    hook.setWasHandled(false);
                                }
                            }
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else {
                        boolean retval = false;
                        try {
                            retval = (boolean) proxyCallback.invoke(self, args);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        if (retval) {
                            Block b = w.getBlock(x, y, z);
                            if (((GraniteBlockType) b.getType()).parent != null && !b.getType().typeEquals(BlockTypes.air)) {
                                EventBlockPlace event = new EventBlockPlace(b, p);
                                Granite.getEventQueue().fireEvent(event);

                                if (event.isCancelled()) {
                                    b.setType(oldBlockType);
                                    ((GranitePlayer) p).sendBlockUpdate(b);
                                }
                            }
                        }

                        hook.setWasHandled(true);
                        return retval;
                    }

                }

                // TODO: fix possible dupe glitch relating to cancellation and skeleton heads

                return true;
            }
        });

        addHook("tryUseItem(n.m.entity.player.EntityPlayer;n.m.world.World;n.m.item.ItemStack)", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                Player p = (Player) MinecraftUtils.wrap(args[0]);

                RayTraceResult rtr = p.rayTrace(3, false);
                if (rtr != null) {
                    EventPlayerInteract epi = new EventPlayerInteract(p, EventPlayerInteract.InteractType.RIGHT_CLICK_BLOCK, rtr.getBlock());
                    Granite.getEventQueue().fireEvent(epi);

                    if (epi.isCancelled()) {
                        hook.setWasHandled(true);
                    }
                }
                return null;
            }
        });
    }
}
