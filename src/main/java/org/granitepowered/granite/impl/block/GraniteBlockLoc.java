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
    @Override
    public Extent getExtent() {
        return null;
    }

    @Override
    public Vector3i getPosition() {
        return null;
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getZ() {
        return 0;
    }

    @Override
    public BlockType getType() {
        return null;
    }

    @Override
    public BlockState getState() {
        return null;
    }

    @Override
    public BlockSnapshot getSnapshot() {
        return null;
    }

    @Override
    public byte getLuminanceFromGround() {
        return 0;
    }

    @Override
    public boolean isPowered() {
        return false;
    }

    @Override
    public boolean isIndirectlyPowered() {
        return false;
    }

    @Override
    public boolean isFacePowered(Direction direction) {
        return false;
    }

    @Override
    public boolean isFaceIndirectlyPowered(Direction direction) {
        return false;
    }

    @Override
    public Collection<Direction> getPoweredFaces() {
        return null;
    }

    @Override
    public Collection<Direction> getIndirectlyPoweredFaces() {
        return null;
    }

    @Override
    public byte getLuminanceFromSky() {
        return 0;
    }

    @Override
    public boolean dig() {
        return false;
    }

    @Override
    public boolean digWith(ItemStack itemStack) {
        return false;
    }

    @Override
    public int getDigTime() {
        return 0;
    }

    @Override
    public int getDigTimeWith(ItemStack itemStack) {
        return 0;
    }

    @Override
    public byte getLuminance() {
        return 0;
    }

    @Override
    public void replaceWith(BlockState state) {

    }

    @Override
    public void replaceWith(BlockType type) {

    }

    @Override
    public void replaceWith(BlockSnapshot snapshot) {

    }

    @Override
    public void interact() {

    }

    @Override
    public void interactWith(ItemStack itemStack) {

    }

    @Override
    public <T> Optional<T> getData(Class<T> dataClass) {
        return null;
    }

    !Compile
}
