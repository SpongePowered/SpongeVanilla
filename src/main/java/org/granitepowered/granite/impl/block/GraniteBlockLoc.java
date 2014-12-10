/*
 * License (MIT)
 *
 * Copyright (c) 2014 Granite Team
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

package org.granitepowered.granite.impl.block;

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Optional;
import org.apache.commons.lang3.NotImplementedException;
import org.granitepowered.granite.impl.world.GraniteWorld;
import org.granitepowered.granite.mappings.Mappings;
import org.granitepowered.granite.utils.MinecraftUtils;
import org.spongepowered.api.block.BlockLoc;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.extent.Extent;

import java.util.Collection;

public class GraniteBlockLoc implements BlockLoc {
    Location location;

    @Override
    public Extent getExtent() {
        return location.getExtent();
    }

    @Override
    public Vector3i getPosition() {
        return location.getPosition().toInt();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getX() {
        return location.getPosition().getFloorX();
    }

    @Override
    public int getY() {
        return location.getPosition().getFloorY();
    }

    @Override
    public int getZ() {
        return location.getPosition().getFloorZ();
    }

    @Override
    public BlockType getType() {
        return getState().getType();
    }

    @Override
    public BlockState getState() {
        return (BlockState) MinecraftUtils.wrapComposite(getWorld().invoke("getBlockState", getBlockPos()));
    }

    @Override
    public BlockSnapshot getSnapshot() {
        // TODO: Snapshot API
        throw new NotImplementedException("");
    }

    @Override
    public byte getLuminanceFromGround() {
        return (byte) getWorld().invoke("getLightFor", Mappings.getClass("EnumSkyBlock").getEnumConstants()[1], getBlockPos());
    }

    @Override
    public boolean isPowered() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isIndirectlyPowered() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isFacePowered(Direction direction) {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public boolean isFaceIndirectlyPowered(Direction direction) {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Direction> getPoweredFaces() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredFaces() {
        // TODO: Get clarification on what indirectly means
        throw new NotImplementedException("");
    }

    @Override
    public byte getLuminanceFromSky() {
        return (byte) getWorld().invoke("getLightFor", Mappings.getClass("EnumSkyBlock").getEnumConstants()[0], getBlockPos());
    }

    @Override
    public boolean dig() {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public boolean digWith(ItemStack itemStack) {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public int getDigTime() {
        // TODO: See what unit this is in (Item.getStrVsBlock())
        throw new NotImplementedException("");
    }

    @Override
    public int getDigTimeWith(ItemStack itemStack) {
        // TODO: See what unit this is in (Item.getStrVsBlock())
        throw new NotImplementedException("");
    }

    @Override
    public byte getLuminance() {
        return (byte) ((GraniteBlockType) getType()).fieldGet("lightValue");
    }

    @Override
    public void replaceWith(BlockState state) {
        getWorld().invoke("setBlockState", getBlockPos(), ((GraniteBlockState) state).parent);
    }

    @Override
    public void replaceWith(BlockType type) {
        replaceWith(type.getDefaultState());
    }

    @Override
    public void replaceWith(BlockSnapshot snapshot) {
        // TODO: Snapshot API
        throw new NotImplementedException("");
    }

    @Override
    public void interact() {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public void interactWith(ItemStack itemStack) {
        // TODO: Figure out if this is possible to simulate
        throw new NotImplementedException("");
    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass) {
        // TODO: Data API
        throw new NotImplementedException("");
    }

    public GraniteWorld getWorld() {
        return ((GraniteWorld) getExtent());
    }

    public Object getBlockPos() {
        return MinecraftUtils.graniteToMinecraftBlockPos(getPosition());
    }
}
