/*
 * License (MIT)
 *
 * Copyright (c) 2014-2015 Granite Team
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

package org.granitepowered.granite.bytecode.classes;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.Granite;
import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.impl.block.GraniteBlockLoc;
import org.granitepowered.granite.impl.block.GraniteBlockSnapshot;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.event.player.GranitePlayerBreakBlockEvent;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.mc.MCBlockPos;
import org.granitepowered.granite.mc.MCItemInWorldManager;
import org.granitepowered.granite.mc.MCPacket;
import org.granitepowered.granite.util.Instantiator;
import org.granitepowered.granite.util.MinecraftUtils;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.Location;

public class ItemInWorldManagerClass extends BytecodeClass {

    public ItemInWorldManagerClass() {
        super("ItemInWorldManager");

        proxy("func_180237_b", new BytecodeClass.ProxyHandler() {
            @Override
            protected Object handle(Object caller, Object[] args, BytecodeClass.ProxyHandlerCallback callback) throws Throwable {
                MCItemInWorldManager thisIiwm = (MCItemInWorldManager) caller;

                MCBlockPos mcBlockPos = (MCBlockPos) args[0];
                Vector3d pos = new Vector3d(mcBlockPos.fieldGet$x(), mcBlockPos.fieldGet$y(), mcBlockPos.fieldGet$z());

                GranitePlayer player = wrap(thisIiwm.fieldGet$thisPlayerMP());

                GraniteBlockLoc loc = new GraniteBlockLoc(new Location((GraniteWorld) wrap(thisIiwm.fieldGet$theWorld()), pos));
                GraniteBlockSnapshot next = new GraniteBlockSnapshot((GraniteBlockState) BlockTypes.AIR.getDefaultState());

                GranitePlayerBreakBlockEvent event = new GranitePlayerBreakBlockEvent(loc, player, next);
                Granite.getInstance().getServer().getEventManager().post(event);

                if (!event.isCancelled()) {
                    return callback.invokeParent(args);
                } else {
                    MCPacket p = Instantiator.get().newPacketBlockChange(thisIiwm.fieldGet$theWorld(), mcBlockPos);
                    player.sendPacket(p);
                    return false;
                }
            }
        });
    }
}
