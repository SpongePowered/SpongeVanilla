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

package org.granitepowered.granite.impl.block.data;

import static org.granitepowered.granite.util.MinecraftUtils.wrap;

import com.flowpowered.math.vector.Vector3d;
import org.granitepowered.granite.composite.Composite;
import org.granitepowered.granite.impl.block.GraniteBlockLoc;
import org.granitepowered.granite.mc.MCTileEntity;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.data.TileEntity;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public abstract class GraniteTileEntity<T extends MCTileEntity> extends Composite<T> implements TileEntity {

    public GraniteTileEntity(Object obj) {
        super(obj);
    }

    @Override
    public World getWorld() {
        return wrap(obj.worldObj);
    }

    @Override
    public BlockLoc getBlock() {
        return new GraniteBlockLoc(new Location(getWorld(), new Vector3d(obj.pos.x, obj.pos.y, obj.pos.z)));
    }

    @Override
    public abstract DataContainer toContainer();
}
