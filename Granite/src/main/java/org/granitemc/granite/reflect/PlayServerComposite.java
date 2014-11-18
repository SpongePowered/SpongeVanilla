package org.granitemc.granite.reflect;

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
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import org.granitemc.granite.api.Granite;
import org.granitemc.granite.api.block.Block;
import org.granitemc.granite.api.entity.player.Player;
import org.granitemc.granite.api.event.inventory.EventInventoryClick;
import org.granitemc.granite.api.event.inventory.EventInventoryHotbarMove;
import org.granitemc.granite.api.event.player.EventPlayerChat;
import org.granitemc.granite.api.event.player.EventPlayerInteract;
import org.granitemc.granite.api.item.ItemStack;
import org.granitemc.granite.api.utils.RayTraceResult;
import org.granitemc.granite.api.world.World;
import org.granitemc.granite.entity.player.GraniteEntityPlayer;
import org.granitemc.granite.inventory.GranitePlayerInventory;
import org.granitemc.granite.item.GraniteItemStack;
import org.granitemc.granite.reflect.composite.Hook;
import org.granitemc.granite.reflect.composite.HookListener;
import org.granitemc.granite.reflect.composite.ProxyComposite;
import org.granitemc.granite.utils.Mappings;
import org.granitemc.granite.utils.MinecraftUtils;
import org.granitemc.granite.world.GraniteWorld;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayServerComposite extends ProxyComposite {
    public PlayServerComposite(GraniteServerComposite server, Object networkManager, GraniteEntityPlayer entityPlayer) {
        super(Mappings.getClass("NetHandlerPlayServer"), new Class[]{
                Mappings.getClass("MinecraftServer"),
                Mappings.getClass("NetworkManager"),
                Mappings.getClass("EntityPlayerMP")
        }, server.parent, networkManager, entityPlayer.parent);

        addHook("processAnimation", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) throws InvocationTargetException, IllegalAccessException {
                if (GraniteServerComposite.instance.isOnServerThread()) {
                    Player p = (Player) MinecraftUtils.wrap(fieldGet("playerEntity"));

                    RayTraceResult rtr = p.rayTrace(4, false);
                    EventPlayerInteract epi;
                    if (rtr != null) {
                        epi = new EventPlayerInteract(p, EventPlayerInteract.InteractType.LEFT_CLICK_BLOCK, rtr.getBlock());
                    } else {
                        epi = new EventPlayerInteract(p, EventPlayerInteract.InteractType.LEFT_CLICK_AIR, null);
                    }

                    Granite.getEventQueue().fireEvent(epi);

                    if (epi.isCancelled()) {
                        hook.setWasHandled(true);
                    }
                }
                return null;
            }
        });

        addHook("processClickWindow", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) throws InvocationTargetException, IllegalAccessException {
                if (GraniteServerComposite.instance.isOnServerThread()) {
                    int windowId = (int) fieldGet(args[0], "windowId");
                    ItemStack stackInSlot = (ItemStack) MinecraftUtils.wrap(fieldGet(args[0], "clickedItem"));
                    int slot = (int) fieldGet(args[0], "slotId");

                    if (slot > 0 && stackInSlot == null) {
                        List slots = (List) fieldGet(fieldGet(getMCPlayer(), "openContainer"), "inventorySlots");

                        if (slots.get(slot) != null) {
                            if (Mappings.invoke(slots.get(slot), "getStack") != null) {
                                stackInSlot = (ItemStack) MinecraftUtils.wrap(Mappings.invoke(slots.get(slot), "getStack"));
                            }
                        }
                    }
                    ItemStack stackInHand = ((GranitePlayerInventory) getGranitePlayer().getPlayerInventory()).getItemStack();

                    if (windowId == (int) fieldGet(fieldGet(getMCPlayer(), "openContainer"), "windowId")) {
                        int mode = (int) fieldGet(args[0], "mode");
                        int button = (int) fieldGet(args[0], "usedButton");

                        Player p = getGranitePlayer();

                        EventInventoryClick evt = null;
                        switch (mode) {
                            case 0:
                                if (stackInSlot == null) {
                                    if (stackInHand == null) {
                                        evt = new EventInventoryClick(p, null, slot, EventInventoryClick.Action.CLICK_BLANK, EventInventoryClick.MouseButton.values()[button]);
                                    } else {
                                        evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.PUT_DOWN, EventInventoryClick.MouseButton.values()[button]);
                                    }
                                } else {
                                    evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.PICK_UP, EventInventoryClick.MouseButton.values()[button]);
                                }
                                break;
                            case 1:
                                evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.QUICK_MOVE, EventInventoryClick.MouseButton.values()[button]);
                                break;
                            case 2:
                                evt = new EventInventoryHotbarMove(p, stackInSlot, slot, button);
                                break;
                            case 3:
                                if ((boolean) fieldGet(((GraniteEntityPlayer) p).fieldGet("capabilities"), "isCreativeMode")) {
                                    if (stackInSlot == null) {
                                        evt = new EventInventoryClick(p, null, slot, EventInventoryClick.Action.CLICK_BLANK, EventInventoryClick.MouseButton.values()[button]);
                                    } else {
                                        evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.CREATIVE_NEW_FULL_STACK, EventInventoryClick.MouseButton.values()[button]);
                                    }
                                }
                                break;
                            case 4:
                                if (slot == -999) {
                                    if (button == 0) {
                                        evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.CLICK_OUTSIDE, EventInventoryClick.MouseButton.LEFT_MOUSE);
                                    } else if (button == 1) {
                                        evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.CLICK_OUTSIDE, EventInventoryClick.MouseButton.RIGHT_MOUSE);
                                    }
                                } else {
                                    if (button == 0) {
                                        evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.DROP_ITEM, EventInventoryClick.MouseButton.NONE);
                                    } else if (button == 1) {
                                        evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.DROP_STACK, EventInventoryClick.MouseButton.NONE);
                                    }
                                }
                                break;
                            case 5:
                                // TODO: create specialized drag event
                                if (button == 0) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.START_DIVIDE_DRAG, EventInventoryClick.MouseButton.LEFT_MOUSE);
                                } else if (button == 4) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.START_PLACE_DRAG, EventInventoryClick.MouseButton.RIGHT_MOUSE);
                                } else if (button == 1) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.PAINT_DIVIDE_DRAG, EventInventoryClick.MouseButton.LEFT_MOUSE);
                                } else if (button == 5) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.PAINT_PLACE_DRAG, EventInventoryClick.MouseButton.RIGHT_MOUSE);
                                } else if (button == 3) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.END_DIVIDE_DRAG, EventInventoryClick.MouseButton.LEFT_MOUSE);
                                } else if (button == 6) {
                                    evt = new EventInventoryClick(p, stackInHand, slot, EventInventoryClick.Action.END_PLACE_DRAG, EventInventoryClick.MouseButton.RIGHT_MOUSE);
                                }
                                break;
                            case 6:
                                if (stackInSlot == null) {
                                    return null;
                                } else {
                                    evt = new EventInventoryClick(p, stackInSlot, slot, EventInventoryClick.Action.COLLECT_STACK, EventInventoryClick.MouseButton.LEFT_MOUSE);
                                }
                                break;
                        }

                        if (evt == null) {
                            return null;
                        }
                        evt.setItemStackInHand(stackInHand);
                        evt.setItemStackInSlot(stackInSlot);

                        Granite.getEventQueue().fireEvent(evt);

                        System.out.println(evt.getButton() + " " + evt.getAction() + " " + (evt.getRelatedStack() == null ? "null" : ((GraniteItemStack) evt.getRelatedStack()).parent) +
                                " (Hand: " + (evt.getItemStackInHand() == null ? "null" : ((GraniteItemStack) evt.getItemStackInHand()).parent) + ", " +
                                "Slot: " + (evt.getItemStackInSlot() == null ? "null" : ((GraniteItemStack) evt.getItemStackInSlot()).parent) + ", " +
                                "Slot ID: " + evt.getSlot() + ")");

                        if (!evt.isCancelled()) {
                            proxyCallback.invoke(self, args);

                            if (evt.getItemStackInSlot() != stackInSlot) {
                                Object newItemStackInSlot = evt.getItemStackInSlot() != null ? ((GraniteItemStack) evt.getItemStackInSlot()).parent : null;
                                fieldSet(args[0], "clickedItem", newItemStackInSlot);
                            }

                            if (evt.getItemStackInHand() != stackInHand) {
                                ((GranitePlayerInventory) getGranitePlayer().getPlayerInventory()).setItemStack(evt.getItemStackInHand());
                            }

                            hook.setWasHandled(true);
                        } else {
                            // Reset
                            List<Object> resp = new ArrayList<>();

                            Collection slots = (Collection) fieldGet(fieldGet(getMCPlayer(), "openContainer"), "inventorySlots");
                            for (Object o : slots) {
                                resp.add(Mappings.invoke(o, "getStack"));
                            }

                            Mappings.invoke(getMCPlayer(), "updateCraftingInventory", fieldGet(getMCPlayer(), "openContainer"), resp);

                            hook.setWasHandled(true);
                        }
                    }
                }
                return null;
            }
        });

        addHook("processPlayerBlockPlacement", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) {
                if (GraniteServerComposite.instance.isOnServerThread()) {
                    Player p = (Player) MinecraftUtils.wrap(fieldGet("playerEntity"));

                    World w = p.getWorld();

                    Block b = ((GraniteWorld) w).getBlock(Mappings.invoke(args[0], "getPosition"));

                    RayTraceResult rtr = p.rayTrace(4, false);
                    EventPlayerInteract epi;
                    if (rtr != null) {
                        epi = new EventPlayerInteract(p, EventPlayerInteract.InteractType.RIGHT_CLICK_BLOCK, b);
                    } else {
                        epi = new EventPlayerInteract(p, EventPlayerInteract.InteractType.RIGHT_CLICK_AIR, null);
                    }

                    Granite.getEventQueue().fireEvent(epi);

                    if (epi.isCancelled()) {
                        hook.setWasHandled(true);
                    }
                }
                return null;
            }
        });

        addHook("processChatMessage", new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) throws InvocationTargetException, IllegalAccessException {
                if (GraniteServerComposite.instance.isOnServerThread()) {
                    EventPlayerChat epc = new EventPlayerChat(getGranitePlayer(), (String) fieldGet(args[0], "message"));
                    Granite.getEventQueue().fireEvent(epc);

                    if (epc.isCancelled()) {
                        hook.setWasHandled(true);
                    } else {
                        fieldSet(args[0], "message", epc.getMessage());
                    }
                }
                return null;
            }
        });

        /*addHook(new HookListener() {
            @Override
            public Object activate(Object self, Method method, Method proxyCallback, Hook hook, Object[] args) throws InvocationTargetException, IllegalAccessException {
                try {
                    if (method.getParameterCount() == 1 &&
                            !method.getParameterTypes()[0].equals(Class.forName("mg")) &&
                            !method.getParameterTypes()[0].equals(Class.forName("id"))) {
                        System.out.println(method);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });*/
    }

    public GraniteEntityPlayer getGranitePlayer() {
        return (GraniteEntityPlayer) MinecraftUtils.wrap(fieldGet("playerEntity"));
    }

    public Object getMCPlayer() {
        return fieldGet("playerEntity");
    }
}
