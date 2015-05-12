/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.vanilla.block;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.common.Sponge;
import org.spongepowered.common.service.persistence.NbtTranslator;
import org.spongepowered.common.util.VecHelper;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.UUID;

public final class VanillaBlockSnapshot implements BlockSnapshot {
    public static final int UPDATE_CLIENT_WITH_PHYSICS = 3;
    public static final int UPDATE_CLIENT = 2;
    public static final int UPDATE = 1;

    private final UUID worldUUID;
    @Nullable private final DataContainer data;
    private BlockPos pos;
    private Vector3i vecPos;
    private IBlockState blockState;
    private WeakReference<World> worldRef;
    private int updateFlag;

    public VanillaBlockSnapshot(World world, BlockPos pos, IBlockState blockState) {
        this.worldRef = new WeakReference<World>(world);
        this.worldUUID = ((org.spongepowered.api.world.World) world).getUniqueId();
        this.pos = pos;
        this.vecPos = VecHelper.toVector(pos);
        this.blockState = blockState;
        this.updateFlag = UPDATE_CLIENT_WITH_PHYSICS;
        final TileEntity te = world.getTileEntity(pos);
        if (te != null) {
            this.data = ((org.spongepowered.api.block.tile.TileEntity) te).toContainer();
        } else {
            this.data = null;
        }
    }

    public VanillaBlockSnapshot(World world, BlockPos pos, IBlockState blockState, NBTTagCompound nbt) {
        this.worldRef = new WeakReference<World>(world);
        this.worldUUID = ((org.spongepowered.api.world.World) world).getUniqueId();
        this.pos = pos;
        this.vecPos = VecHelper.toVector(pos);
        this.blockState = blockState;
        this.updateFlag = UPDATE_CLIENT_WITH_PHYSICS;
        this.data = NbtTranslator.getInstance().translateFrom(nbt);
    }

    public VanillaBlockSnapshot(World world, BlockPos pos, IBlockState blockState, int updateFlag) {
        this(world, pos, blockState);
        this.updateFlag = updateFlag;
    }

    @Override
    public BlockState getState() {
        return (BlockState) this.blockState;
    }

    @Override
    public void setBlockState(BlockState blockState) {
        this.blockState = (IBlockState) blockState;
    }

    @Override
    public Vector3i getLocation() {
        return this.vecPos;
    }

    @Override
    public void setLocation(Vector3i location) {
        this.pos = VecHelper.toBlockPos(location);
        this.vecPos = location;
    }

    @Nullable
    public World getWorld() {
        World world = this.worldRef.get();
        if (world == null) {
            Optional<org.spongepowered.api.world.World> optWorld = Sponge.getGame().getServer().getWorld(this.worldUUID);
            if (optWorld.isPresent()) {
                world = (World) optWorld.get();
                this.worldRef = new WeakReference<World>(world);
            }
        }
        return world;
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer container = new MemoryDataContainer()
                .set(DataQuery.of("X"), this.pos.getX())
                .set(DataQuery.of("Y"), this.pos.getY())
                .set(DataQuery.of("Z"), this.pos.getZ())
                .set(DataQuery.of("UpdateFlag"), this.updateFlag)
                .set(DataQuery.of("WorldUUID"), this.worldUUID)
                .set(DataQuery.of("BlockState"), this.blockState)
                .set(DataQuery.of("HasTE"), this.data != null);
        if (this.data != null) {
            container.set(DataQuery.of("Data"), this.data);
        }

        return container;
    }

    /**
     * Applies the snapshot back at the location
     * @return True if successful, false if not
     */
    public boolean apply() {
        return apply(false);
    }

    /**
     * Applies the snapshot back at the location and updates the client including physics.
     * @param force True forces the application of the snapshot
     * @return True if successful, false if not
     */
    public boolean apply(boolean force) {
        return apply(force, UPDATE_CLIENT_WITH_PHYSICS);
    }

    /**
     * Applies the snapshot back at the location with the update flag given.
     * @param force True forces the application of the snapshot
     * @param updateFlag The flag used to update the client.
     * @return True if successful, false if not
     */
    public boolean apply(boolean force, int updateFlag) {
        final World world = getWorld();
        return world != null && applyTo(world, this.pos, force, updateFlag);

    }

    /**
     * Applies the snapshot to a location in a world with the update flag given
     * @param world The world to apply the snapshot in
     * @param pos The position to apply the snapshot at
     * @param force True forces the application of the snapshot
     * @param updateFlag The flag used to update the client.
     * @return True if successful, false if not
     */
    public boolean applyTo(World world, BlockPos pos, boolean force, int updateFlag) {
        final IBlockState snapshotState = (IBlockState) getState();
        final IBlockState currentState = world.getBlockState(pos);
        if (snapshotState.getBlock() != currentState.getBlock() || snapshotState.getBlock().getMetaFromState(snapshotState)
                != currentState.getBlock().getMetaFromState(currentState)) {
            if (force) {
                world.setBlockState(pos, currentState, updateFlag);
                world.markBlockForUpdate(pos);
            } else {
                return false;
            }
        }
        if (this.data != null) {
            final TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                ((org.spongepowered.api.block.tile.TileEntity) te).setRawData(this.data);
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof VanillaBlockSnapshot)) {
            return false;
        }

        final VanillaBlockSnapshot that = (VanillaBlockSnapshot) o;

        return this.worldUUID == that.worldUUID && !(this.data != null ? !this.data.equals(that.data) : that.data != null)
                && this.pos.equals(that.pos);
    }

    @Override
    public int hashCode() {
        int result = this.pos.hashCode();
        result = 31 * result + this.worldUUID.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "VanillaBlockSnapshot{" +
                "worldUUID=" + worldUUID +
                ", data=" + data +
                ", pos=" + pos +
                ", blockState=" + blockState +
                ", updateFlag=" + updateFlag +
                '}';
    }
}
