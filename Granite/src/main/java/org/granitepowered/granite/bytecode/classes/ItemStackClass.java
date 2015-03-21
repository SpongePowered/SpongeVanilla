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

import org.granitepowered.granite.bytecode.BytecodeClass;
import org.granitepowered.granite.bytecode.Proxy;
import org.granitepowered.granite.bytecode.ProxyCallbackInfo;
import org.granitepowered.granite.impl.block.GraniteBlockState;
import org.granitepowered.granite.impl.entity.player.GranitePlayer;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mc.*;
import org.spongepowered.api.block.BlockState;

import static org.granitepowered.granite.util.MinecraftUtils.unwrap;
import static org.granitepowered.granite.util.MinecraftUtils.wrap;

public class ItemStackClass extends BytecodeClass {

    public ItemStackClass() {
        super("ItemStack");
    }

    @Proxy(methodName = "onItemUse")
    public Object onItemUse(ProxyCallbackInfo<MCItemStack> info) throws Throwable {
        MCEntityPlayerMP mcPlayer = (MCEntityPlayerMP) info.getArguments()[0];
        MCWorld mcWorld = (MCWorld) info.getArguments()[1];
        MCBlockPos posClicked = (MCBlockPos) info.getArguments()[2];
        MCEnumFacing face = (MCEnumFacing) info.getArguments()[3];

        GranitePlayer player = wrap(mcPlayer);
        GraniteWorld world = wrap(mcWorld);

        BlockState clickedBlock = world.getBlock(posClicked.x, posClicked.y, posClicked.z);

        MCBlockPos posPlaced = posClicked;
        if (!unwrap((GraniteBlockState) clickedBlock).block.isReplaceable(mcWorld, posClicked)) {
            posPlaced = posClicked.offset(face, 1);
        }

        BlockState loc = world.getBlock(posPlaced.x, posPlaced.y, posPlaced.z);

        /*BlockSnapshot oldSnapshot = loc.getSnapshot();
        // TODO: Send PR to Sponge devs with hitX, hitY, hitZ (args[4 - 6])

        boolean ret = (boolean) info.callback();

        BlockSnapshot newSnapshot = loc.getSnapshot();

        if (!oldSnapshot.equals(newSnapshot)) {
            GranitePlayerPlaceBlockEvent event = new GranitePlayerPlaceBlockEvent(loc, player, oldSnapshot);
            Granite.getInstance().getEventManager().post(event);

            if (event.isCancelled()) {
                loc.replaceWith(oldSnapshot);

                MCPacket clickedUpdate = Instantiator.get().newPacketBlockChange(mcPlayer.fieldGet$worldObj(), posClicked);
                player.obj.fieldGet$playerNetServerHandler().sendPacket(clickedUpdate);

                MCPacket placedUpdate = Instantiator.get().newPacketBlockChange(mcPlayer.fieldGet$worldObj(), posPlaced);
                player.obj.fieldGet$playerNetServerHandler().sendPacket(placedUpdate);
            }
            return !event.isCancelled();
        } else {*/
        return loc;

    }
}
